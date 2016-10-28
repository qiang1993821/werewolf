package com.roc.enity;

import javax.persistence.*;

/**
 * Created by roc on 2016/10/19.
 */
@Entity
@Table(name = "werewolf_game")
public class Game {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false, name="uid")
    private long uid;
    @Column(nullable = true, name="status")
    private int status;
    @Column(nullable = true, name="role")
    private String role;
    @Column(nullable = true, name="name")
    private String name;
    @Column(nullable = false, name="num")
    private int num;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
