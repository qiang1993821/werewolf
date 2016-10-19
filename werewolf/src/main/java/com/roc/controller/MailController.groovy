package com.roc.controller

import com.roc.service.impl.UserServiceImpl
import com.roc.util.CacheUtil
import com.roc.util.MailUtil
import groovy.json.JsonBuilder
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Created by roc on 2016/5/13.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/mail")
class MailController {
    private final Logger logger = LoggerFactory.getLogger(MailController.class);
    @Autowired
    private UserServiceImpl userService

    //发送密码提醒邮件
    @RequestMapping(value = "/forget")
    String forget(@RequestParam(value = "mail") String mail){
        def map = [:]
        def code = 0
        def result = ""
        if (StringUtils.isBlank(mail)){
            map.put("code",0)
            map.put("result","邮箱为空")
            return new JsonBuilder(map).toString()
        }
        if (CacheUtil.getCache("wforget"+mail)==null)
            CacheUtil.putCache("wforget"+mail,0,CacheUtil.MEMCACHED_ONE_DAY)
        int times = CacheUtil.getCache("wforget"+mail)
        def user = userService.getUserByMail(mail)
        if (user==null){
            result = "该邮箱尚未注册，直接设定密码登录即可"
        }else if (times<5){
            def msg = "您的密码为："+user.pwd
            def title = "狼人杀首夜上帝密码找回"
            if (MailUtil.sendMail(MailUtil.ustbMail,MailUtil.ustbPwd,mail,title,msg)){
                result = "邮件已发送，请尽快登录邮箱查看密码！"
                code = 1
            }else {
                result = "验证邮件发送失败！"
            }
            times++
            CacheUtil.putCache("wforget"+mail,times,CacheUtil.MEMCACHED_ONE_DAY)
        }else {
            result = "已达今日密码找回邮件发送次数上限"
        }
        map.put("code",code)
        map.put("result",result)
        return new JsonBuilder(map).toString()
    }
}
