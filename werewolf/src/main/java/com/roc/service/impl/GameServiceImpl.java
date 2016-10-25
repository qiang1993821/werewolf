package com.roc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.roc.dao.GameDao;
import com.roc.enity.Game;
import com.roc.service.GameService;
import com.roc.util.CacheUtil;
import com.roc.util.GameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Service
public class GameServiceImpl implements GameService{
    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);
    @Autowired
    private GameDao gameDao;

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
                json.put("cNum",game.getCivilian());
                json.put("wNum",game.getWerewolf());
                json.put("start",game.getStatus());
                GameUtil.putGod(game,json);
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
    public void clearMember(long gameId,String players) {
        try {
            JSONObject json = JSON.parseObject(players);
            for (String key:json.keySet()){
                if (CacheUtil.getCache(gameId+"-"+key) != null){
                    CacheUtil.del(gameId+"-"+key);
                }
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
    }
}
