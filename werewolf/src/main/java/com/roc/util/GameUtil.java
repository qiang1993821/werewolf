package com.roc.util;

import com.alibaba.fastjson.JSONObject;
import com.roc.enity.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
