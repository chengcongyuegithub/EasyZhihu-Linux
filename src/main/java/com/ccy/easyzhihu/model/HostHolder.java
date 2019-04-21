package com.ccy.easyzhihu.model;

import org.springframework.stereotype.Component;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.model
 * @date 2019/4/1
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal();

    public User getUser() {
        return users.get();
    }

    public  void setUsers(User user) {
        users.set(user);
    }

    public void clear()
    {
        users.remove();
    }
}
