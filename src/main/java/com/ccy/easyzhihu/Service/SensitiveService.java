package com.ccy.easyzhihu.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chengcongyue
 * @version 1.0
 * @description 过滤敏感词
 * @date 2019/4/6
 */
@Service
public class SensitiveService implements InitializingBean{

    private static final Logger logger= LoggerFactory.getLogger(SensitiveService.class);

    private static final String DEFAULT_REPLACEMENT="敏感词";

    //字典树的结点定义
    private class TrieNode
    {
        //当前是否是结尾的结点
        private boolean end=false;
        //下一个结点
        private Map<Character,TrieNode> subNodes=new HashMap<>();
        //添加结点
        void addSubNode(Character key,TrieNode node)
        {
            subNodes.put(key,node);
        }
        //得到下一个结点
        TrieNode getSubNode(Character key)
        {
            return subNodes.get(key);
        }

        boolean isKeywordEnd()
        {
            return end;
        }
        void setKeywordEnd(boolean end)
        {
            this.end=end;
        }
        public int getSubNodeCount()
        {
            return subNodes.size();
        }
    }

    private TrieNode rootNode=new TrieNode();


    @Override
    //根据文件生成字典树
    public void afterPropertiesSet() throws Exception {
        rootNode=new TrieNode();
        try {
            InputStream is =Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read=new InputStreamReader(is);
            BufferedReader bufferedReader=new BufferedReader(read);
            String lineTxt;
            while((lineTxt=bufferedReader.readLine())!=null)
            {
                lineTxt=lineTxt.trim();
                addWord(lineTxt);
            }
            read.close();
        }catch (Exception e)
        {
            logger.error("读取敏感词文件失败"+e.getMessage());
        }
    }

    private void addWord(String lineTxt)
    {
        TrieNode tempNode=rootNode;
        for(int i=0;i<lineTxt.length();i++)
        {
            Character c=lineTxt.charAt(i);
            TrieNode node=tempNode.getSubNode(c);
            if(node==null)
            {
                node = new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode=node;
            if(i==lineTxt.length()-1)
            {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    public String filter(String text)
    {
        if(StringUtils.isEmpty(text))
        {
            return text;
        }
        String replacement=DEFAULT_REPLACEMENT;
        StringBuilder res=new StringBuilder();

        TrieNode tempNode=rootNode;
        int begin=0;
        int position=0;
        while(position<text.length())
        {
            char c=text.charAt(position);
            //空格直接跳过
            if(isSymbol(c))
            {
                if(tempNode==rootNode)
                {
                    res.append(c);
                    begin++;
                }
                ++position;
                continue;
            }
            tempNode = tempNode.getSubNode(c);
            if(tempNode==null)
            {
                res.append(text.charAt(begin));
                position=begin+1;
                begin=position;
                tempNode=rootNode;
            }//匹配上了并且是最后一个
            else if(tempNode.isKeywordEnd())
            {
                res.append(replacement);
                position=position+1;
                begin=position;
                tempNode=rootNode;
            }
            else
            {
                position++;
            }
        }
        res.append(text.substring(begin));
        return res.toString();
    }

    //加强敏感词的判断
    private boolean isSymbol(char c)
    {
        String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";//其他需要，直接修改正则表达式就好
        return !String.valueOf(c).matches(regex);
    }

    public static void main(String[] args) {
        SensitiveService s=new SensitiveService();
        s.addWord("黄天浩");
        s.addWord("萌哥");
        System.out.println(s.filter("    你好,黄>>>天>>>浩,我是萌哥"));
    }
}
