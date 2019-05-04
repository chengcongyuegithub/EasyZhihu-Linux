package com.ccy.easyzhihu.Service;

import com.ccy.easyzhihu.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    private static final String SOLR_URL="http://127.0.0.1:8081/solr/easyzhihu";
    private SolrServer solrServer=new HttpSolrServer(SOLR_URL);
    private static final String ZH_TITLE_FIELD="zh_title";
    private static final String ZH_CONTENT_FIELD="zh_content";
    private static final String ZH_TC_COPYFIELD="zh_tc";


    public List<Question> searchQuestion(String keyWord,int offset,int count,String hlPre,
                                         String hlPos) throws Exception
    {
        List<Question> questionList=new ArrayList<>();
        SolrQuery solrQuery=new SolrQuery();
        solrQuery.set("q",ZH_TC_COPYFIELD+":"+keyWord);
        solrQuery.setRows(count);
        solrQuery.setStart(offset);
        solrQuery.setHighlight(true);
        solrQuery.setHighlightSimplePre(hlPre);
        solrQuery.setHighlightSimplePost(hlPos);
        solrQuery.set("hl.fl",ZH_TITLE_FIELD+","+ZH_CONTENT_FIELD);
        QueryResponse response = solrServer.query(solrQuery);
        for(Map.Entry<String,Map<String,List<String>>> entry:response.getHighlighting().entrySet())
        {
            Question q=new Question();
            q.setId(Integer.parseInt(entry.getKey()));
            if(entry.getValue().containsKey(ZH_CONTENT_FIELD))
            {
                List<String> contentList = entry.getValue().get(ZH_CONTENT_FIELD);
                if (contentList.size() > 0) {
                    q.setContent(contentList.get(0));
                }
            }
            if(entry.getValue().containsKey(ZH_TITLE_FIELD))
            {
                List<String> titleList = entry.getValue().get(ZH_TITLE_FIELD);
                if (titleList.size() > 0) {
                    q.setTitle(titleList.get(0));
                }
            }

            questionList.add(q);
        }

        return questionList;
    }


    public boolean indexQuestion(int qid,String title,String content) throws Exception
    {
        SolrInputDocument doc=new SolrInputDocument();
        doc.setField("id",qid);
        doc.setField(ZH_TITLE_FIELD,title);
        doc.setField(ZH_CONTENT_FIELD,content);
        UpdateResponse response=solrServer.add(doc,1000);
        return response!=null&&response.getStatus()==0;
    }
}
