package com.roc.service;

import com.alibaba.fastjson.JSONObject;
import com.roc.enity.Game;

import java.util.List;
import java.util.Map;

/**
 * Created by roc on 2016/10/20.
 */
public interface GameService {
    /**
     * 新增/修改房间
     * @param game
     * @return
     */
    int save(Game game);

    /**
     * 获取我的房间列表信息
     * @param uid
     * @return
     */
    List<JSONObject> getGameList(long uid);

    /**
     * 根据id获取房间
     * @param id
     * @return
     */
    Game getGame(long id);

    /**
     * 关闭游戏时清空相关缓存
     * @param id
     */
    void clearMember(long id);

    /**
     * 判断昵称是否存在
     * @param gameId
     * @param name
     * @return
     */
    boolean existName(long gameId,String name);

    /**
     * 是否已报名满
     * @param game
     * @return
     */
    boolean isFull(Game game);

    /**
     * 报名
     * @param name
     * @param gameId
     * @return
     */
    long addMember(String name,long gameId);

    /**
     * 获取参与者人名
     * @param id
     * @return
     */
    List<String> getMember(long id);

    /**
     * 查看角色，1能查看，-1报名不足，0异常
     * @param gameId
     * @param uid
     * @return
     */
    int showRole(long gameId,long uid);

    /**
     * 安排角色
     * @param game
     */
    void putRole(Game game);

    /**
     * 是否报名
     * @param gameId
     * @param uid
     * @return
     */
    boolean hasJoin(long gameId,long uid);

    /**
     * 显示身份对应功能
     * @param gameId
     * @param uid
     */
    Map<String,Object> showBtn(long gameId,long uid);

    /**
     * 杀人
     * @param uid
     * @param kill
     * @return
     */
    int werewolf(long uid,long kill);

    /**
     * 验人
     * @param uid
     * @param guess
     * @return
     */
    Map<String,Object> prophet(long uid,long guess);

    /**
     * 守人
     * @param uid
     * @param protect
     * @return
     */
    int guard(long uid,long protect);

    /**
     * 女巫
     * @param uid save为1是被救人id，save为0是被毒人id（选不毒人id为0）
     * @param witch
     * @param save 0没救人，1救人
     * @return
     */
    int witch(long uid,long witch,int save);

    /**
     * 丘比特
     * @param uid
     * @param lover1
     * @param lover2
     * @param code
     * @return
     */
    int cupid(long uid,long lover1,long lover2,int code);

    /**
     * 被狼刀的人，平安夜uid放0
     * @param gameId
     * @return
     */
    Map<String,Object> killByWolf(long gameId);

    /**
     * 查看情侣
     * @param uid
     * @return
     */
    Map<String,Object> isLover(long uid);

    /**
     * 昨夜死的人
     * @param gameId
     * @return
     */
    Map<String,Object> showResult(long gameId);

    /**
     * 获取昨夜详情
     * @param gameId
     * @return
     */
    List<String> getNightInfo(long gameId);
}
