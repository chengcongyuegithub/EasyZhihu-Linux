package com.ccy.easyzhihu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

@Service
public class MailSender implements InitializingBean{

    private static final Logger logger= LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public boolean sendWithHTMLTemplate(String to,String subject,String template,
                                        Map<String,Object> model)
    {
        try {

            String fromUser= MimeUtility.encodeText("fromUser");
            InternetAddress from = new InternetAddress(fromUser+"<chengcongyue@sina.com>");
            MimeMessage mimeMessage=mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
            //create mail content
            Context context = new Context();
            context.setVariables(model);
            String content = this.templateEngine.process(template, context);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content,true);
            mailSender.send(mimeMessage);
            return true;
        }catch (Exception e)
        {
            logger.error("send mail failed"+e.getMessage());
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("chengcongyue@sina.com");
        mailSender.setPassword("ccy980305");
        mailSender.setHost("smtp.sina.com");
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable",true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
