package com.roc.service.impl;

import com.roc.dao.UserDao;
import com.roc.enity.User;
import com.roc.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@SpringBootApplication
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    @Override
    public int save(User user) {
        try {
            userDao.save(user);
            return 1;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public User getUserByMail(String mail) {
        User user = null;
        try {
            user = userDao.getUserByMail(mail).get(0);
        }catch (Exception e){
            logger.error("getUserByMail|"+e.getMessage());
        }
        return user;
    }
}
