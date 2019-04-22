package com.ccy.easyzhihu.Service;

import com.ccy.easyzhihu.Dao.MessageDAO;
import com.ccy.easyzhihu.model.Message;
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
public class MessageService {

    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message)
    {
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId,int offset,int limit)
    {
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getConversationUnreadCount(int userId, String conversationId)
    {
        return messageDAO.getConversationUnreadCount(userId, conversationId);
    }
}
