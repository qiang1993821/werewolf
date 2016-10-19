package com.roc.dao;


import com.roc.enity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by roc on 2016/4/20.
 */
@Repository
public interface UserDao extends CrudRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.mail = :mail")
    List<User> getUserByMail(@Param("mail") String mail);

}
