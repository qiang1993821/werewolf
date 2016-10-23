package com.roc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by roc on 2016/5/13.
 * 由于springboot是运行一个jar包，所以写在资源文件里也要上线，还是在这里写死吧
 */
public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
    public static final String ustbMail = "";
    public static final String ustbPwd = "";

    public static boolean sendMail(final String fromMail,final String password,String toMail,String subject,String msg){
        try {
            final Properties props = new Properties();
            // 表示SMTP发送邮件，需要进行身份验证
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", "smtp.163.com");
            // 发件人的账号
            props.put("mail.user", fromMail);
            // 访问SMTP服务时需要提供的密码
            props.put("mail.password", password);
            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            InternetAddress from = new InternetAddress(props.getProperty("mail.user")+"@163.com");
            message.setFrom(from);

            // 设置收件人
            InternetAddress to = new InternetAddress(toMail);
            message.setRecipient(Message.RecipientType.TO, to);

//            // 设置抄送
//            InternetAddress cc = new InternetAddress("luo_aaaaa@yeah.net");
//            message.setRecipient(Message.RecipientType.CC, cc);
//
//            // 设置密送，其他的收件人不能看到密送的邮件地址
//            InternetAddress bcc = new InternetAddress("aaaaa@163.com");
//            message.setRecipient(Message.RecipientType.CC, bcc);

            // 设置邮件标题
            message.setSubject(subject);

            // 设置邮件的内容体
            message.setContent(msg, "text/html;charset=UTF-8");

            // 发送邮件
            Transport.send(message);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("SEND_MAIL_ERROR|fromMail:"+fromMail+"|toMail:"+toMail+"|subject:"+subject+"|msg:"+msg);
            return false;
        }
    }
}
