package com.ccy.easyzhihu.interceptor;

import com.ccy.easyzhihu.Dao.LoginTicketDAO;
import com.ccy.easyzhihu.Dao.UserDAO;
import com.ccy.easyzhihu.model.HostHolder;
import com.ccy.easyzhihu.model.LoginTicket;
import com.ccy.easyzhihu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.interceptor
 * @date 2019/4/1
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{

    @Autowired
    private LoginTicketDAO loginTicketDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket=null;
        if(request.getCookies()!=null)
        {
            for (Cookie cookie:request.getCookies())
            {
                if(cookie.getName().equals("ticket"))
                {
                    ticket=cookie.getValue();
                    break;
                }
            }
        }
        if(ticket!=null)
        {
            LoginTicket loginTicket=loginTicketDAO.selectByTicket(ticket);
            if(loginTicket==null||loginTicket.getExpired().before(new Date())
                    ||loginTicket.getStatus()!=0)
            {
                   return true;
            }
            User user = userDAO.selectByid(loginTicket.getUserId());
            hostHolder.setUsers(user);
        }
        return true;
    }
    //放入前后文中
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
            hostHolder.clear();
    }
}
