package com.roc.controller

import com.roc.service.impl.UserServiceImpl
import com.roc.util.CacheUtil
import com.roc.util.MailUtil
import com.roc.util.UserUtil
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
 * Created by Administrator on 2016/4/26.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/user")
class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserServiceImpl userService


    /**
     * 登录接口
     * @param username
     * @return
     */
    @RequestMapping(value = "/login")
    String login(@RequestParam(value = "username") String username,
                 @RequestParam(value = "pwd") String pwd){
        def map = [:]
        if (StringUtils.isBlank(username) || StringUtils.isBlank(pwd)){
            map.put("result","微信登录异常，请重新登录！")
            map.put("code",0)
        }else {
            def user = userService.getUserByMail(username)
            def type = 0
            def result
            def random = UserUtil.getRondomNum()//随机数，增大安全性，否则不登陆邮箱知道注册激活链接也能注册
            if (user == null){
                if (CacheUtil.getCache("wregTimes"+username)==null)
                    CacheUtil.putCache("wregTimes"+username,0,CacheUtil.MEMCACHED_ONE_DAY)
                int times = CacheUtil.getCache("wregTimes"+username)
                if (times<5) {
                    CacheUtil.putCache("w" + username, pwd, CacheUtil.MEMCACHED_ONE_DAY * 3)
                    CacheUtil.putCache("wrandom-" + username, random, CacheUtil.MEMCACHED_ONE_DAY * 3)
                    def msg = "少年，开启你的伟大航路吧，<a href=\"http://localhost:821/reg?mail=" + username + "&random=" + random + "\">点击完成注册</a>，若非本人操作请忽略！（此邮件三日内有效）"
                    def title = "狼人杀首夜辅助注册激活"
                    if (MailUtil.sendMail(MailUtil.ustbMail, MailUtil.ustbPwd, username, title, msg)) {
                        result = "此邮箱尚未注册，已发送注册邮件，请尽快登录完成注册！"
                    } else {
                        result = "此邮箱尚未注册，注册邮件发送失败！"
                    }
                    times++
                    CacheUtil.putCache("jregTimes"+username,times,CacheUtil.MEMCACHED_ONE_DAY)
                }else {
                    result = "此邮箱尚未注册，注册邮件发送次数已达今日上限！"
                }
            }else {
                if (user.pwd == pwd){
                    type = 1
                    result = user.id
                }else {
                    result = "密码错误！"
                }
            }
            map.put("code",type)
            map.put("result",result)
        }
        return new JsonBuilder(map).toString()
    }
}
