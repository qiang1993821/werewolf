package com.roc.service;

import com.roc.enity.Game;

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
}
