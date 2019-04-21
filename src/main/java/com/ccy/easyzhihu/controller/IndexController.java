package com.ccy.easyzhihu.controller;

import com.ccy.easyzhihu.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.*;
import java.util.*;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.controller
 * @date 2019/3/23
 */
//@Controller
public class IndexController {

    @RequestMapping(path={"/"})
    @ResponseBody
    public String index(HttpSession session)
    {

        return "hello "+session.getAttribute("words");
    }

    @RequestMapping("/hello")
    public String redictHome(Model model)
    {
        model.addAttribute("name","huangtianhao" );
        List<String> colors= Arrays.asList("red","green","blue");
        model.addAttribute("colors",colors );
        Map<Integer,String> map=new HashMap<>();
        map.put(1,"hello");
        map.put(2,"no hello");
        map.put(3,"right");
        model.addAttribute("myMap",map);
        User user=new User();
        user.setName("huangtianhao");
        model.addAttribute("user",user);
        return "hello";
    }

    @RequestMapping(path = {"/request"})
    @ResponseBody
    public String request(Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session,@CookieValue("JSESSIONID")String cookieId)
    {

           StringBuilder sb=new StringBuilder();
           sb.append(cookieId+"<br>");
           sb.append(request.getMethod()+"<br>");//get
           sb.append(request.getQueryString()+"<br>");//null
           sb.append(request.getPathInfo()+"<br>");//null
           sb.append(request.getRequestURI()+"<br>");//request
           Enumeration<String> headers = request.getHeaderNames();
           while(headers.hasMoreElements())
           {
               String header=headers.nextElement();
               sb.append("header: "+header+" "+request.getHeader(header)+"<br>");
           }
           //传递cookie
           if(request.getCookies()!=null)
           {
               for(Cookie cookie:request.getCookies())
               {
                   sb.append("Cookie:"+cookie.getName()+" value:"+cookie.getValue());
               }
           }
           //添加response的字段
           response.addHeader("chengcongyue","hello");
           //添加cookie
           response.addCookie(new Cookie("cookie","huangtianhao"));
           return sb.toString();
    }

    //重定向301的跳转
    @RequestMapping(path = {"/redirect/{code}"},method = RequestMethod.GET)
    public RedirectView redirect(@PathVariable("code") int code, HttpSession session)
    {
        session.setAttribute("words","jump from redirect method");
        RedirectView redirectView = new RedirectView("/", true);
        if(code==301)
        {
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return redirectView;
    }
    @ExceptionHandler
    @ResponseBody
    public String error(Exception e)
    {
        return "error:"+e.getMessage();
    }
    //测试异常捕获
    @RequestMapping(path = {"/admin"},method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key")String key)
    {
        if("admin".equals(key))
        {
            return "hello admin";
        }
        throw new RuntimeException("参数不对");
    }

}

