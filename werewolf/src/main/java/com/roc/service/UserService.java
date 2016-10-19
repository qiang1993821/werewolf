package com.roc.service;


import com.roc.enity.User;

/**
 * Created by Administrator on 2016/4/20.
 */
public interface UserService {
    /**
     * 新增/修改用户
     * @param user
     * @return
     */
    int save(User user);

    /**
     * 根据邮箱获取用户
     * @param mail
     * @return
     */
    User getUserByMail(String mail);
}
