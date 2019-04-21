package com.ccy.easyzhihu.controller;

import com.ccy.easyzhihu.Dao.QuestionDAO;
import com.ccy.easyzhihu.Dao.UserDAO;
import com.ccy.easyzhihu.Service.QuestionService;
import com.ccy.easyzhihu.Service.UserService;
import com.ccy.easyzhihu.aspect.LogAspect;
import com.ccy.easyzhihu.model.Question;
import com.ccy.easyzhihu.model.VeiwObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.controller
 * @date 2019/3/27
 */
@Controller
public class HomeController {
    private static final Logger logger= LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;

    @RequestMapping(path={"/","index"},method = {RequestMethod.GET, RequestMethod.POST})
    //@ResponseBody
    public String index(Model model)
    {
        model.addAttribute("vos",getQuestions(0,0,10));
        return "index";
    }
    private List<VeiwObject> getQuestions(int userId,int offset,int limit)
    {
        System.out.println(userId+" "+offset+" "+limit);
        List<Question> questionList=questionService.getSelectLatestQuestions(userId,offset,limit);
        //System.out.println(questionList.size());
        //model.addAttribute("questionList",questionList);
        List<VeiwObject> vos=new ArrayList<>();
        for(Question question:questionList)
        {
            VeiwObject vo=new VeiwObject();
            vo.set("question",question);
            vo.set("user",userService.getUser(question.getUserId()));
            System.out.println(userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
