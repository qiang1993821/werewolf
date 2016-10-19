package com.roc.enity;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/4/20.
 */
@Entity
@Table(name = "werewolf_user")
public class User {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = true, name="regtime")
    private String regTime;
    @Column(nullable = true, name="mail")
    private String mail;
    @Column(nullable = true, name="pwd")
    private String pwd;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
