package com.roc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.roc.enity.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUtil {
    private static final Logger logger = LoggerFactory.getLogger(GameUtil.class);

    public static void putGod(Game game,JSONObject json){
        String godName = "";
        int godNum = 0;
        if (game.getCupid()==1){
            godName += "丘比特 ";
            godNum++;
        }
        if (game.getGuard()==1){
            godName += "守卫 ";
            godNum++;
        }
        if (game.getHunter()==1){
            godName += "猎人 ";
            godNum++;
        }
        if (game.getIdiot()==1){
            godName += "白痴 ";
            godNum++;
        }
        if (game.getProphet()==1){
            godName += "预言家 ";
            godNum++;
        }
        if (game.getWitch()==1){
            godName += "女巫 ";
            godNum++;
        }
        json.put("godNum",godNum);
        json.put("god",godName);
    }

    public static String putRole(Game game){
        JSONObject json = new JSONObject();
        List<String> role = new ArrayList<String>();
        if (game.getCupid()==1){
            role.add("丘比特");
        }
        if (game.getGuard()==1){
            role.add("守卫");
        }
        if (game.getHunter()==1){
            role.add("猎人");
        }
        if (game.getIdiot()==1){
            role.add("白痴");
        }
        if (game.getProphet()==1){
            role.add("预言家");
        }
        if (game.getWitch()==1){
            role.add("女巫");
        }
        for (int i=0;i<game.getCivilian();i++){
            role.add("平民");
        }
        for (int i=0;i<game.getWerewolf();i++){
            role.add("狼人");
        }
        json.put("role",role);
        return json.toJSONString();
    }

    public static boolean isFull(Game game){
        JSONObject json = JSON.parseObject(game.getPlayers());
        int num = game.getCivilian()+game.getWerewolf()+game.getCupid()+game.getGuard()+game.getHunter()+game.getIdiot()+game.getProphet()+game.getWitch();
        if (json.keySet().size()-1 >= num){
            return true;
        }
        return false;
    }

    public static String addMember(String name,String players){
        JSONObject json = JSON.parseObject(players);
        json.put(name,"报名");
        return json.toJSONString();
    }

    public static List<String> getMember(String players){
        JSONObject json = JSON.parseObject(players);
        List<String> member = new ArrayList<String>();
        for (String key:json.keySet()){
            if (!key.equals("role")){
                member.add(key);
            }
        }
        return member;
    }
}
