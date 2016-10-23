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
    @Column(nullable = true, name="result")
    private String result;
    @Column(nullable = true, name="name")
    private String name;
    @Column(nullable = true, name="players")
    private String players;
    @Column(nullable = true, name="civilian")
    private int civilian;
    @Column(nullable = true, name="werewolf")
    private int werewolf;
    @Column(nullable = true, name="prophet")
    private int prophet;
    @Column(nullable = true, name="guard")
    private int guard;
    @Column(nullable = true, name="witch")
    private int witch;
    @Column(nullable = true, name="hunter")
    private int hunter;
    @Column(nullable = true, name="idiot")
    private int idiot;
    @Column(nullable = true, name="cupid")
    private int cupid;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public String getPlayers() {
        return players;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public int getCivilian() {
        return civilian;
    }

    public void setCivilian(int civilian) {
        this.civilian = civilian;
    }

    public int getWerewolf() {
        return werewolf;
    }

    public void setWerewolf(int werewolf) {
        this.werewolf = werewolf;
    }

    public int getProphet() {
        return prophet;
    }

    public void setProphet(int prophet) {
        this.prophet = prophet;
    }

    public int getGuard() {
        return guard;
    }

    public void setGuard(int guard) {
        this.guard = guard;
    }

    public int getWitch() {
        return witch;
    }

    public void setWitch(int witch) {
        this.witch = witch;
    }

    public int getHunter() {
        return hunter;
    }

    public void setHunter(int hunter) {
        this.hunter = hunter;
    }

    public int getIdiot() {
        return idiot;
    }

    public void setIdiot(int idiot) {
        this.idiot = idiot;
    }

    public int getCupid() {
        return cupid;
    }

    public void setCupid(int cupid) {
        this.cupid = cupid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
