package com.ccy.easyzhihu.controller;

import com.ccy.easyzhihu.Service.FeedService;
import com.ccy.easyzhihu.Service.FollowService;
import com.ccy.easyzhihu.model.EntityType;
import com.ccy.easyzhihu.model.Feed;
import com.ccy.easyzhihu.model.HostHolder;
import com.ccy.easyzhihu.util.JedisAdapter;
import com.ccy.easyzhihu.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {

   @Autowired
   FeedService feedService;
   @Autowired
   FollowService followService;
   @Autowired
   HostHolder hostHolder;
   @Autowired
   JedisAdapter jedisAdapter;

   @RequestMapping(path = {"/pullfeeds"},method = {RequestMethod.GET,RequestMethod.POST})
   public String getPullFeeds(Model model)
   {
       int localUserId=hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
       List<Integer> followees=new ArrayList<>();
       if(localUserId!=0)
       {
           followees=followService.getFollowees(localUserId, EntityType.ENTITY_USER,Integer.MAX_VALUE);
       }
       List<Feed> feeds =feedService.getUserFeeds(Integer.MAX_VALUE,followees,10);
       model.addAttribute("feeds",feeds);
       return "feeds";
   }
   @RequestMapping(path = {"/pushfeeds"},method = {RequestMethod.GET,RequestMethod.POST})
   public String getPushFeeds(Model model)
   {
       int localUserId=hostHolder.getUser()!=null?hostHolder.getUser().getId():0;
       List<String> feedIds=jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId),0,10);
       List<Feed> feeds=new ArrayList<>();
       for (String feedId:feedIds)
       {
           Feed feed = feedService.getById(Integer.parseInt(feedId));
           if(feed!=null)
           {
               feeds.add(feed);
           }
       }
       model.addAttribute("feeds",feeds);
       return "feeds";
   }
}
