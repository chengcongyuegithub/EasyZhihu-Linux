package com.ccy.easyzhihu.configuration;

import com.ccy.easyzhihu.interceptor.LoginInterceptor;
import com.ccy.easyzhihu.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author chengcongyue
 * @version 1.0
 * @description com.ccy.easyzhihu.configuration
 * @date 2019/4/1
 */
@Component
public class ZhiHuConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    PassportInterceptor passportInterceptor;
    @Autowired
    LoginInterceptor loginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginInterceptor).addPathPatterns("/user/*");
        super.addInterceptors(registry);
    }
}
