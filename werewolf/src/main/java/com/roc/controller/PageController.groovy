package com.roc.controller

import com.roc.enity.User
import com.roc.service.impl.UserServiceImpl
import com.roc.util.CacheUtil
import com.roc.util.TimeUtil
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@EnableAutoConfiguration
public class PageController {
    private final Logger logger = LoggerFactory.getLogger(PageController.class);
    @Autowired
    private UserServiceImpl userService

    //注册
    @RequestMapping(value = "/reg")
    String reg(Map<String, Object> model,
               @RequestParam(value = "mail") String mail,
               @RequestParam(value = "random") int random){
        try {
            def pwd = CacheUtil.getCache("w"+mail)
            def r = CacheUtil.getCache("wrandom-"+mail)
            def user = userService.getUserByMail(mail)
            if (StringUtils.isNotBlank(pwd) && user==null && r == random) {
                user = new User()
                user.mail = mail
                user.pwd = pwd
                user.regTime = TimeUtil.getNowTime()
                def type = userService.save(user)
                if (type == 1) {
                    model.put("result", "注册成功！");
                    logger.error("werewolf|LOGIN|uid:" + user.id + ",name:" + mail + ",time:" + TimeUtil.getNowTime())
                } else {
                    model.put("result", "创建用户异常");
                }
            }else {
                model.put("result", "邮件已过期！");
            }
        }catch (Exception e){
            model.put("result","注册失败，请重试，若已尝试多次，请返回平台再次发送注册邮件！");
            logger.error("reg|"+e.message)
        }
        return "result"
    }

    //登录页
    @RequestMapping(value = "/login")
    public String login(Map<String, Object> model) {
        return "login"
    }

    //主页
    @RequestMapping(value = "/index")
    public String index(Map<String, Object> model) {
        return "index"
    }

    //添加房间
    @RequestMapping(value = "/addGame")
    public String addGame(Map<String, Object> model) {
        return "addGame"
    }

}