package com.ccy.easyzhihu.controller;

import com.ccy.easyzhihu.Service.FollowService;
import com.ccy.easyzhihu.Service.QuestionService;
import com.ccy.easyzhihu.Service.SearchService;
import com.ccy.easyzhihu.Service.UserService;
import com.ccy.easyzhihu.model.EntityType;
import com.ccy.easyzhihu.model.Question;
import com.ccy.easyzhihu.model.VeiwObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    private static final Logger logger= LoggerFactory.getLogger(SearchController.class);
    @Autowired
    SearchService searchService;
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/search"},method = {RequestMethod.GET,RequestMethod.POST})
    public String search(Model model,
                         @RequestParam(value="q") String keyWord,
                         @RequestParam(value="offset",defaultValue = "0")int offset,
                         @RequestParam(value="count",defaultValue = "10")int count)

    {
        try {
            List<Question> questionList=searchService.searchQuestion(keyWord,offset,count,"<font>","</font>");
            List<VeiwObject> vos=new ArrayList<>();
            for(Question question:questionList)
            {
                Question q=questionService.getQuestionById(question.getId()+"");
                VeiwObject vo=new VeiwObject();
                if(question.getContent()!=null)
                {
                    q.setContent(question.getContent());
                }
                if(question.getTitle()!=null)
                {
                    q.setTitle(question.getTitle());
                }
                vo.set("question",q);
                vo.set("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION,question.getId()));
                vo.set("user",userService.getUser(q.getUserId()));
                vos.add(vo);
            }
            model.addAttribute("vos",vos);
            model.addAttribute("keyword",keyWord);
        }catch (Exception e)
        {
           logger.error("error happened"+e.getMessage());
        }
        return "result";
    }
}
