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

        System.out.println(request.getRequestURI()+"!!!!");
        String str = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1);
        System.out.println(str+"!!!");
        if(hostHolder.getUser()==null)
        {
            response.sendRedirect("/reglogin?next="+request.getRequestURI());
            return false;
        }
        return true;
    }
}
