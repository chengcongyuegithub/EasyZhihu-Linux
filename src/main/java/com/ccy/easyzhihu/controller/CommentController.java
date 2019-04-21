package com.ccy.easyzhihu.controller;

import com.ccy.easyzhihu.Service.CommentService;
import com.ccy.easyzhihu.Service.QuestionService;
import com.ccy.easyzhihu.Service.SensitiveService;
import com.ccy.easyzhihu.model.Comment;
import com.ccy.easyzhihu.model.EntityType;
import com.ccy.easyzhihu.model.HostHolder;
import com.ccy.easyzhihu.util.ZhiHuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.controller
 * @date 2019/4/14
 */
@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    QuestionService questionService;
    @RequestMapping(path={"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId")int questionId,@RequestParam("content")String content)
    {
        try {
            content= HtmlUtils.htmlEscape(content);
            content= sensitiveService.filter(content);
            Comment comment=new Comment();
            if(hostHolder.getUser()!=null)
            {
                comment.setUserId(hostHolder.getUser().getId());
            }else
            {
                comment.setUserId(ZhiHuUtil.ANONYMOUS_USERID);
            }
            comment.setContent(content);
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);
            int count = commentService.getCommmentCount(comment.getEntityId(), comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(), count);
        }catch (Exception e)
        {
            logger.error("添加评论错误"+e.getMessage());
        }

        return "redirect:/question/"+questionId;
    }

}
