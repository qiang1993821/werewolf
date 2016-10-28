package com.roc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.roc.dao.GameDao;
import com.roc.dao.MemberDao;
import com.roc.enity.Game;
import com.roc.enity.Player;
import com.roc.service.GameService;
import com.roc.util.CacheUtil;
import com.roc.util.GameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@Service
public class GameServiceImpl implements GameService{
    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);
    @Autowired
    private GameDao gameDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    public int save(Game game) {
        try {
            gameDao.save(game);
            return 1;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public List<JSONObject> getGameList(long uid) {
        try {
            List<JSONObject> infoList = new ArrayList<JSONObject>();
            List<Game> gameList = gameDao.getGameList(uid);
            for (Game game:gameList){
                JSONObject json = new JSONObject();
                json.put("id",game.getId());
                json.put("name",game.getName());
                json.put("start",game.getStatus());
                GameUtil.putMember(game.getRole(),json);
                infoList.add(json);
            }
            return infoList;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Game getGame(long id) {
        return gameDao.findOne(id);
    }

    @Override
    public boolean existName(long gameId, String name) {
        try {
            List<Player> playerList = memberDao.getOneOfMember(gameId,name);
            if (playerList.size() > 0)
                return true;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isFull(Game game) {
        try {
            List<Player> playerList = memberDao.getAllMember(game.getId());
            if (playerList.size() == game.getNum())
                return true;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public long addMember(String name, long gameId) {
        try {
            Player member = new Player();
            member.setGid(gameId);
            member.setName(name);
            member.setStatus(0);
            member.setDied(0);
            member.setNight("");
            memberDao.save(member);
            return member.getId();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public void clearMember(long gameId) {
        //从member表获取id清缓存,删除member表里的数据
        try {
            List<Player> memList = memberDao.getAllMember(gameId);
            for (Player player:memList){
                if (CacheUtil.getCache(gameId+"-"+player.getId()) != null){
                    CacheUtil.del(gameId+"-"+player.getId());
                }
                memberDao.delete(player.getId());
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
    }

    @Override
    public List<String> getMember(long id) {
        List<String> memList = null;
        try {
            memList = new ArrayList<String>();
            List<Player> playerList = memberDao.getAllMember(id);
            for (Player player:playerList){
                memList.add(player.getName());
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return memList;
    }

    @Override
    public int showRole(long gameId, long uid) {
        try {
            Game game = gameDao.findOne(gameId);
            Player player = memberDao.findOne(uid);
            if (player.getGid() == game.getId()){
                if (game.getNum() == memberDao.getAllMember(gameId).size()){
                    return 1;
                }else {
                    return -1;
                }
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean hasJoin(long gameId, long uid) {
        try {
            if(CacheUtil.getCache(gameId+"-"+uid) != null){
                return true;
            }else {
                Game game = gameDao.findOne(gameId);
                Player member = memberDao.findOne(uid);
                if (game !=null && member != null && game.getStatus() > 0 && member.getGid() == gameId){
                    CacheUtil.putCache(gameId+"-"+uid,1,CacheUtil.MEMCACHED_ONE_DAY);
                    return true;
                }
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public void putRole(Game game) {
        try {
            String[] roles = game.getRole().split("-");
            List<Player> memberList = memberDao.getAllMember(game.getId());
            if (roles.length == memberList.size() && roles.length == game.getNum()){
                Collections.shuffle(memberList);//乱序，分配身份
                for (int i=0;i<roles.length;i++){
                    Player member = memberList.get(i);
                    member.setRole(roles[i]);
                    memberDao.save(member);
                    if (roles[i].equals("丘比特")){
                        CacheUtil.putCache("lover-"+game.getId(),"-",CacheUtil.MEMCACHED_ONE_DAY);
                    }
                }
            }else {
                logger.error("putRole|人数不对应|role:" + roles.length + ",num:" + game.getNum() + ",member:" + memberList.size());
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
    }
}
