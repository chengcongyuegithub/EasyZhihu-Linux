package com.ccy.easyzhihu.controller;

import com.ccy.easyzhihu.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.controller
 * @date 2019/3/29
 */
@Controller
public class LoginController {

     private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

     @Autowired
     UserService userService;


     //登录
     @RequestMapping(path = {"/login/"},method = {RequestMethod.POST,RequestMethod.GET})
     public String login(Model model, @RequestParam("account") String username,
                         @RequestParam("password") String password,
                         @RequestParam(value="next",required = false) String next,
                         @RequestParam(value = "remember_me",defaultValue = "false") boolean rememberMe,
                         HttpServletResponse response)
     {
         try {
            Map<String,Object> map=userService.login(username,password);
            if(map.containsKey("ticket"))
            {
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                if(rememberMe)
                {
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if(!StringUtils.isEmpty(next))
                {
                return "redirect:"+next;
                }
                return "redirect:/";
            }else
            {
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
         }catch (Exception e)
         {
             logger.error("登录异常"+e.getMessage());
             return "login";
         }
     }
     //注册
     @RequestMapping(path = {"/reg/"},method = {RequestMethod.POST,RequestMethod.GET})
     public String reg(@RequestParam("account")String username, @RequestParam("password")String password, Model model)
     {
         Map<String, Object> map = userService.register(username, password);
         try {
             //有异常
             if(map.containsKey("msg"))
             {
                model.addAttribute("msg",map.get("msg"));
                return "login";
             }else{
                return "redirect:/";
             }
         }catch (Exception e)
         {
             logger.error("注册出现了错误!!!");
             e.printStackTrace();
             return "login";
         }

     }
     @RequestMapping(path = {"/relogin"},method = {RequestMethod.GET,RequestMethod.POST})
     public String login(@RequestParam(value="next",required = false) String next,Model model)
     {
         model.addAttribute("next",next);
         return "login";
     }

     @RequestMapping(path = {"/logout"} ,method={RequestMethod.GET,RequestMethod.POST})
     public String logout(@CookieValue("ticket")String ticket)
     {
         userService.logout(ticket);
         return "redirect:/";
     }
}
