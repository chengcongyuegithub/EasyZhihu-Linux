package com.ccy.easyzhihu.controller;

import com.ccy.easyzhihu.Service.CommentService;
import com.ccy.easyzhihu.Service.FollowService;
import com.ccy.easyzhihu.Service.QuestionService;
import com.ccy.easyzhihu.Service.UserService;
import com.ccy.easyzhihu.async.EventModel;
import com.ccy.easyzhihu.async.EventProducer;
import com.ccy.easyzhihu.async.EventType;
import com.ccy.easyzhihu.model.*;
import com.ccy.easyzhihu.util.ZhiHuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FollowController {

    @Autowired
    FollowService followService;
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/followUser"},method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public String followUser(@RequestParam("userId")int userId)
    {
        if(hostHolder.getUser()==null)
        {
           return ZhiHuUtil.getObjectJson(999);
        }
        boolean ret = followService.follow(hostHolder.getUser().getId(),
                EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId()).setEntityType(EntityType.ENTITY_USER).setEntityId(userId).setEntityOwnerId(userId));
        return ZhiHuUtil.getJSONString(ret?0:1,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }


    @RequestMapping(path = {"/unfollowUser"},method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowUser(@RequestParam("userId")int userId)
    {
        if(hostHolder.getUser()==null)
        {
            return ZhiHuUtil.getObjectJson(999);
        }
        boolean ret=followService.unfollow(hostHolder.getUser().getId(),
                EntityType.ENTITY_USER,userId);

        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
        .setActorId(hostHolder.getUser().getId()).setEntityId(userId)
        .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));

        return ZhiHuUtil.getJSONString(ret?0:1,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }
    @RequestMapping(path = {"/followQuestion"},method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam("questionId")int questionId)
    {
        if(hostHolder.getUser()==null)
        {
            return ZhiHuUtil.getObjectJson(999);
        }
        Question question=questionService.getQuestionById(questionId+"");
        if(question==null)
        {
            return ZhiHuUtil.getJSONString(1,"the question not exists");
        }
        boolean res=followService.follow(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
        .setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(question.getUserId()));

        Map<String,Object> info=new HashMap<>();
        info.put("id",hostHolder.getUser().getId());
        info.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return ZhiHuUtil.getJSONString(res?0:1,String.valueOf(info));
    }
    @RequestMapping(path = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unfollowQuestion(@RequestParam("questionId") int questionId) {

        if(hostHolder.getUser()==null)
        {
            return ZhiHuUtil.getObjectJson(999);
        }
        Question question=questionService.getQuestionById(questionId+"");
        if(question==null)
        {
            return ZhiHuUtil.getJSONString(1,"the question not exists");
        }
        boolean res=followService.unfollow(hostHolder.getUser().getId(),EntityType.ENTITY_QUESTION,questionId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
                .setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION).setEntityOwnerId(question.getUserId()));

        Map<String,Object> info=new HashMap<>();
        info.put("id",hostHolder.getUser().getId());
        info.put("count",followService.getFollowerCount(EntityType.ENTITY_QUESTION, questionId));
        return ZhiHuUtil.getJSONString(res?0:1,String.valueOf(info));
    }
    @RequestMapping(path = {"/user/{uid}/followers"},method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid")int userId)
    {
        List<Integer> followedIds=followService.getFollowers(EntityType.ENTITY_USER,userId,0,10);
        if(hostHolder.getUser()!=null)
        {
            model.addAttribute("followers",getUsersInfo(hostHolder.getUser().getId(),followedIds));
        }else
        {
            model.addAttribute("followers",getUsersInfo(0,followedIds));
        }
        model.addAttribute("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,userId));
        model.addAttribute("curUser",userService.getUser(userId));
        return "followers";
    }
    @RequestMapping(path = {"/user/{uid}/followees"},method = {RequestMethod.GET})
    public String followees(Model model,@PathVariable("uid")int userId)
    {
        List<Integer> followeeIds=followService.getFollowees(userId,EntityType.ENTITY_USER,0,10);
        if(hostHolder.getUser()!=null)
        {
            model.addAttribute("followees",getUsersInfo(hostHolder.getUser().getId(),followeeIds));
        }else
        {
            model.addAttribute("followees",getUsersInfo(0,followeeIds));
        }
        model.addAttribute("followeeCount",followService.getFolloweeCount(userId,EntityType.ENTITY_USER));
        model.addAttribute("curUser",userService.getUser(userId));
        return "followees";
    }

    private List<VeiwObject> getUsersInfo(int localUserId,List<Integer> userIds)
    {
        List<VeiwObject> userInfos=new ArrayList<VeiwObject>();
        for(Integer uid:userIds)
        {
            User user=userService.getUser(uid);
            if(user==null)
            {
                continue;
            }
            VeiwObject vo=new VeiwObject();
            vo.set("user",user);
            vo.set("comment",commentService.getUserCommentCount(uid));
            vo.set("followerCount",followService.getFollowerCount(EntityType.ENTITY_USER,uid));
            vo.set("followeeCount",followService.getFolloweeCount(uid,EntityType.ENTITY_USER));
            if(localUserId!=0)
            {
                vo.set("followed",followService.isFollower(localUserId,EntityType.ENTITY_USER,uid));
            }else
            {
                vo.set("followed",false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }
}
