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

    public static void putMember(String role,JSONObject json){
        String[] roles = role.split("-");
        int cNum = 0;
        int wNum = 0;
        String godName = "";
        if (roles.length>0){
            for (int i=0;i<roles.length;i++){
                if (roles[i].equals("平民")){
                    cNum++;
                }else if (roles[i].equals("狼人")){
                    wNum++;
                }else if (roles[i].equals("预言家")){
                    godName += "预言家 ";
                }else if (roles[i].equals("女巫")){
                    godName += "女巫 ";
                }else if (roles[i].equals("猎人")){
                    godName += "猎人 ";
                }else if (roles[i].equals("守卫")){
                    godName += "守卫 ";
                }else if (roles[i].equals("白痴")){
                    godName += "白痴 ";
                }else if (roles[i].equals("丘比特")){
                    godName += "丘比特 ";
                }
            }
        }
        json.put("cNum",cNum);
        json.put("wNum",wNum);
        json.put("godNum",roles.length-cNum-wNum);
        json.put("god",godName);
    }
}
