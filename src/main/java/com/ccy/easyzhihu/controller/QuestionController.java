package com.ccy.easyzhihu.controller;

import com.ccy.easyzhihu.Dao.QuestionDAO;
import com.ccy.easyzhihu.Service.CommentService;
import com.ccy.easyzhihu.Service.LikeService;
import com.ccy.easyzhihu.Service.QuestionService;
import com.ccy.easyzhihu.Service.UserService;
import com.ccy.easyzhihu.model.*;
import com.ccy.easyzhihu.util.ZhiHuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.controller
 * @date 2019/4/3
 */
@Controller
public class QuestionController {

    private static final Logger logger= LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;
    @RequestMapping(path = {"/question/add"},method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title")String title,@RequestParam("content")String content)
    {
        try {
           Question question=new Question();
           question.setContent(content);
           question.setCreatedDate(new Date());
           question.setTitle(title);
           if(hostHolder.getUser()==null)
           {
               question.setUserId(ZhiHuUtil.ANONYMOUS_USERID);
           }else
           {
               question.setUserId(hostHolder.getUser().getId());
           }
           if(questionService.addQuestion(question)>0)
           {
               return ZhiHuUtil.getObjectJson(0);
           }
        }catch (Exception e)
        {
             logger.error("增加题目失败"+e.getMessage());
        }
        return ZhiHuUtil.getJSONString(1,"失败");
    }

    @RequestMapping(path = {"/question/{id}"},method = RequestMethod.GET)
    public String questionDetail(Model model,@PathVariable("id")String id)
    {
        //System.out.println("question id is "+id);
        Question question = questionService.getQuestionById(id);
        model.addAttribute("question",question);
        List<Comment> commentList = commentService.getCommentsByEntity(Integer.parseInt(id), EntityType.ENTITY_QUESTION);
        List<VeiwObject> vos=new ArrayList<>();
        for(Comment comment:commentList)
        {
            long count = likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId());
            VeiwObject vo=new VeiwObject();
            if (hostHolder.getUser() == null) {
                vo.set("liked", 0);
            } else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            vo.set("likeCount",count);
            vos.add(vo);
        }
        model.addAttribute("comments",vos);
        return "detail";
    }
}
