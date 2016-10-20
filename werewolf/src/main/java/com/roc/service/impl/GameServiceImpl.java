package com.roc.service.impl;

import com.roc.dao.GameDao;
import com.roc.enity.Game;
import com.roc.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

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
}
