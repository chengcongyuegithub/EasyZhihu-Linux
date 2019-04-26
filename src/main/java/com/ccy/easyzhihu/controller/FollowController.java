package com.ccy.easyzhihu.controller;

import com.ccy.easyzhihu.Service.CommentService;
import com.ccy.easyzhihu.Service.FollowService;
import com.ccy.easyzhihu.Service.QuestionService;
import com.ccy.easyzhihu.Service.UserService;
import com.ccy.easyzhihu.async.EventModel;
import com.ccy.easyzhihu.async.EventProducer;
import com.ccy.easyzhihu.async.EventType;
import com.ccy.easyzhihu.model.EntityType;
import com.ccy.easyzhihu.model.HostHolder;
import com.ccy.easyzhihu.util.ZhiHuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId()).setEntityId(EntityType.ENTITY_USER).setEntityId(userId));
        return ZhiHuUtil.getJSONString(ret?0:1,String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }


    @RequestMapping(path = {"/unfollowerUser"},method = {RequestMethod.POST})
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



}
