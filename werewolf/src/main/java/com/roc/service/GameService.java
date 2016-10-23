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
}
