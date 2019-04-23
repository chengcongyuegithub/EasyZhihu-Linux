package com.ccy.easyzhihu.controller;

import com.ccy.easyzhihu.Service.CommentService;
import com.ccy.easyzhihu.Service.LikeService;
import com.ccy.easyzhihu.async.EventModel;
import com.ccy.easyzhihu.async.EventProducer;
import com.ccy.easyzhihu.async.EventType;
import com.ccy.easyzhihu.model.Comment;
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
public class LikeController {

    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    EventProducer eventProducer;
    @RequestMapping(path ={"/like"},method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId")int commentId)
    {
       if(hostHolder.getUser()==null)
       {
           return ZhiHuUtil.getObjectJson(999);
       }
       Comment comment=commentService.getCommentById(commentId);
       eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.getUser().getId())
       .setEntityId(commentId).setEntityId(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
               .setExt("questionId",String.valueOf(comment.getEntityId()))
       );

       long count=likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
       return ZhiHuUtil.getJSONString(0,String.valueOf(count));
    }


    @RequestMapping(path ={"/dislike"},method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId")int commentId)
    {
        if(hostHolder.getUser()==null)
        {
            return ZhiHuUtil.getObjectJson(999);
        }
        long count=likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
        return ZhiHuUtil.getJSONString(0,String.valueOf(count));
    }

}
