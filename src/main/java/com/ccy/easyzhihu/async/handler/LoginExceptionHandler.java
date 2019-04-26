package com.ccy.easyzhihu.async.handler;

import com.ccy.easyzhihu.async.EventHandler;
import com.ccy.easyzhihu.async.EventModel;
import com.ccy.easyzhihu.async.EventType;
import com.ccy.easyzhihu.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginExceptionHandler implements EventHandler{

    @Autowired
    MailSender mailSender;


    @Override
    public void doHandler(EventModel model) {
        Map<String,Object> map=new HashMap<>();
        map.put("username",model.getExt("username"));
        map.put("subject","login sucess");
        mailSender.sendWithHTMLTemplate(model.getExt("email"),"login has exception",
                "mail/mail.html",map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
