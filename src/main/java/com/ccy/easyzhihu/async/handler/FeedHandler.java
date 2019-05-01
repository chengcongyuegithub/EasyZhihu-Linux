package com.ccy.easyzhihu.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.ccy.easyzhihu.Dao.QuestionDAO;
import com.ccy.easyzhihu.Service.FeedService;
import com.ccy.easyzhihu.Service.FollowService;
import com.ccy.easyzhihu.Service.QuestionService;
import com.ccy.easyzhihu.Service.UserService;
import com.ccy.easyzhihu.async.EventHandler;
import com.ccy.easyzhihu.async.EventModel;
import com.ccy.easyzhihu.async.EventType;
import com.ccy.easyzhihu.model.EntityType;
import com.ccy.easyzhihu.model.Feed;
import com.ccy.easyzhihu.model.Question;
import com.ccy.easyzhihu.model.User;
import com.ccy.easyzhihu.util.JedisAdapter;
import com.ccy.easyzhihu.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler{

    private static final Logger log= LoggerFactory.getLogger(FeedHandler.class);
    @Autowired
    FollowService followService;
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FeedService feedService;
    @Autowired
    JedisAdapter jedisAdapter;
    private String buildFeedData(EventModel model)
    {
       Map<String,String> map=new HashMap<>();
       User actor=userService.getUser(model.getActorId());
       if(actor==null)
       {
           return null;
       }
       map.put("userId",String.valueOf(actor.getId()));
       map.put("userHead",actor.getHeadUrl());
       map.put("userName",actor.getName());
       if(model.getType()==EventType.COMMENT
               ||
       (model.getType()==EventType.FOLLOW
       &&model.getEntityType()== EntityType.ENTITY_QUESTION))
       {
           Question question=questionService.getQuestionById(model.getEntityId()+"");
           if(question==null)
           {
               return null;
           }
           map.put("questionId",String.valueOf(question.getId()));
           map.put("questionTitle",question.getTitle());
           return JSONObject.toJSONString(map);
       }
       return null;
    }
    @Override
    public void doHandler(EventModel model) {
        try {
            Feed feed=new Feed();
            feed.setCreatedDate(new Date());
            feed.setType(model.getType().getValue());
            feed.setUserId(model.getActorId());
            feed.setData(buildFeedData(model));
            if(feed.getData()==null)
            {
                return ;
            }
            feedService.addFeed(feed);
            List<Integer> followers=followService.getFollowers(EntityType.ENTITY_USER,
                    model.getActorId(),Integer.MAX_VALUE);
            followers.add(0);
            for(int follower:followers)
            {
                String timelineKey= RedisKeyUtil.getTimelineKey(follower);
                jedisAdapter.lpush(timelineKey,String.valueOf(feed.getId()));
            }

        }catch (Exception e)
        {
            log.error("error happened"+e.getMessage());
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT,EventType.FOLLOW});
    }
}
