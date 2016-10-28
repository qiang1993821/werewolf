package com.roc.service;

import com.alibaba.fastjson.JSONObject;
import com.roc.enity.Game;

import java.util.List;

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
}
