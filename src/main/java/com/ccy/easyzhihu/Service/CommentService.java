package com.ccy.easyzhihu.Service;

import com.ccy.easyzhihu.Dao.CommentDAO;
import com.ccy.easyzhihu.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.Service
 * @date 2019/4/14
 */
@Service
public class CommentService {

    @Autowired
    private CommentDAO commentDAO;

    public List<Comment>  getCommentsByEntity(int entityId,int entityType)
    {
        return commentDAO.selectByEntity(entityId,entityType);
    }

    public int addComment(Comment comment)
    {
        return commentDAO.addComment(comment);
    }
    public int getCommmentCount(int entityId,int entityType)
    {
        return commentDAO.getCommentCount(entityId,entityType);
    }
    public void deleteComment(int entityId,int entityType)
    {
        commentDAO.updateStatus(entityId,entityType,1);
    }
}
