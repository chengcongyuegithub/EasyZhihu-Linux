package com.ccy.easyzhihu.interceptor;

import com.ccy.easyzhihu.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.interceptor
 * @date 2019/4/2
 */
@Component
public class LoginInterceptor implements HandlerInterceptor{

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(hostHolder.getUser()==null)
        {
            response.sendRedirect("/relogin?next="+request.getRequestURI());
        }
        return false;//不能设置为true,否则 Cannot call sendError() after the response has been committed
    }
}
