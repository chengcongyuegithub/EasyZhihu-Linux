package com.ccy.easyzhihu.interceptor;

import com.ccy.easyzhihu.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ConversationInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String str=request.getParameter("conversationId");
        if(hostHolder.getUser()==null
                ||!str.contains(hostHolder.getUser().getId()+""))
        {
            response.sendRedirect("/reglogin?next="+request.getRequestURI());
            return false;
        }
        return true;
    }
}
