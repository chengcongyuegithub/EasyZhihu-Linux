package com.ccy.easyzhihu.Service;

import com.ccy.easyzhihu.Dao.LoginTicketDAO;
import com.ccy.easyzhihu.Dao.UserDAO;
import com.ccy.easyzhihu.model.LoginTicket;
import com.ccy.easyzhihu.model.User;


import com.ccy.easyzhihu.util.ZhiHuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.*;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id) {
        return userDAO.selectByid(id);
    }

    public Map<String,Object> register(String username, String password)
    {
        Map<String,Object> map=new HashMap<String,Object>();
        if(StringUtils.isEmpty(username))
        {
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password))
        {
            map.put("msg","密码不能为空");
            return map;
        }
        User user= userDAO.selectByName(username);
        //System.out.println(user.getName()+"!!!");
        if(user!=null)
        {
            map.put("msg","用户名已经被注册");
            return map;
        }
        user =new User();
        user.setName(username);
        //设置盐
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String head=String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000));
        user.setHeadUrl(head);
        //设置密码
        user.setPassword(ZhiHuUtil.MD5(password+user.getSalt()));
        //添加
        userDAO.addUser(user);
        //登录

        return map;
    }

    public Map<String,Object> login(String username,String password)
    {
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isEmpty(username))
        {
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isEmpty(password))
        {
            map.put("msg","密码不能为空");
            return map;
        }
        User user=userDAO.selectByName(username);
        if(user==null)
        {
            map.put("msg","用户名不存在");
            return map;
        }
        if(!ZhiHuUtil.MD5(password+user.getSalt()).equals(user.getPassword()))
        {
            map.put("msg","密码不正确");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        map.put("userId", user.getId());
        return map;
    }

    private String addLoginTicket(int userId)
    {
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date=new Date();
        System.out.println(date.getTime()+1000*3600*24);
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addLoginTicket(ticket);
        return ticket.getTicket();
    }
    public void logout(String ticket)
    {
        loginTicketDAO.updateStatus(ticket,1);
    }

    public User selectByName(String name)
    {
        return userDAO.selectByName(name);
    }
}
