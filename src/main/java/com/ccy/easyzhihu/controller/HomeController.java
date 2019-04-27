package com.ccy.easyzhihu.controller;

import com.ccy.easyzhihu.Dao.QuestionDAO;
import com.ccy.easyzhihu.Dao.UserDAO;
import com.ccy.easyzhihu.Service.CommentService;
import com.ccy.easyzhihu.Service.FollowService;
import com.ccy.easyzhihu.Service.QuestionService;
import com.ccy.easyzhihu.Service.UserService;
import com.ccy.easyzhihu.aspect.LogAspect;
import com.ccy.easyzhihu.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
    @Autowired
    private CommentService commentService;
    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path={"/","index"},method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model)
    {
        model.addAttribute("vos",getQuestions(0,0,10));
        return "index";
    }

    @RequestMapping(path={"/user/{userId}"},method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model,@PathVariable("userId")int userId)
    {
        model.addAttribute("vos",getQuestions(userId,0,10));

        User user=userService.getUser(userId);
        VeiwObject vo=new VeiwObject();
        vo.set("user",user);
        vo.set("commentCount",commentService.getUserCommentCount(userId));
        vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,userId));
        vo.set("followeeCount",followService.getFolloweeCount(userId,EntityType.ENTITY_USER));
        if(hostHolder.getUser()!=null)
        {
            vo.set("followed",followService.isFollower(hostHolder.getUser().getId(),EntityType.ENTITY_USER,userId));
        }else
        {
            vo.set("followed",false);
        }
        model.addAttribute("profileUser",vo);
        return "profile";
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
