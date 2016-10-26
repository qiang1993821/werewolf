package com.roc.enity;

import javax.persistence.*;

@Entity
@Table(name = "werewolf_member")
public class Player {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = true, name="gid")
    private long gid;
    @Column(nullable = true, name="name")
    private String name;
    @Column(nullable = true, name="status")
    private int status;
    @Column(nullable = true, name="kill")
    private int kill;
    @Column(nullable = true, name="night")
    private String night;

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getKill() {
        return kill;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    public String getNight() {
        return night;
    }

    public void setNight(String night) {
        this.night = night;
    }
}
