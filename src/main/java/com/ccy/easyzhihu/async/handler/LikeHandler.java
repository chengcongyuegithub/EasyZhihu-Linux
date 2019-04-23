package com.ccy.easyzhihu.async.handler;

import com.ccy.easyzhihu.Service.MessageService;
import com.ccy.easyzhihu.Service.UserService;
import com.ccy.easyzhihu.async.EventHandler;
import com.ccy.easyzhihu.async.EventModel;
import com.ccy.easyzhihu.async.EventType;
import com.ccy.easyzhihu.model.Message;
import com.ccy.easyzhihu.model.User;
import com.ccy.easyzhihu.util.ZhiHuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler{

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandler(EventModel model) {
        Message message=new Message();
        message.setFromId(model.getActorId());// ni ming
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user =userService.getUser(model.getActorId());
        message.setContent("user "+user.getName()+" like your comment,http://127.0.0.1" +
                ":8080/question/"+model.getExt("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
