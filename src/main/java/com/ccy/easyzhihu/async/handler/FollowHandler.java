package com.ccy.easyzhihu.async.handler;

import com.ccy.easyzhihu.Service.MessageService;
import com.ccy.easyzhihu.Service.UserService;
import com.ccy.easyzhihu.async.EventHandler;
import com.ccy.easyzhihu.async.EventModel;
import com.ccy.easyzhihu.async.EventType;
import com.ccy.easyzhihu.model.EntityType;
import com.ccy.easyzhihu.model.Message;
import com.ccy.easyzhihu.model.User;
import com.ccy.easyzhihu.util.ZhiHuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Component
public class FollowHandler implements EventHandler {

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandler(EventModel model) {
        Message message=new Message();
        message.setFromId(ZhiHuUtil.ANONYMOUS_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user=userService.getUser(model.getActorId());

        if(model.getEntityType()== EntityType.ENTITY_QUESTION)
        {
            message.setContent("user "+ user.getName()+" follow your question");
        }else if(model.getEntityType()==EntityType.ENTITY_USER)
        {
            message.setContent("user "+user.getName()+" follow you");
        }
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.FOLLOW);
    }
}
