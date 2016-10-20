package com.roc.controller

import com.roc.enity.Game
import com.roc.service.impl.GameServiceImpl
import com.roc.util.CacheUtil
import groovy.json.JsonBuilder
import org.apache.commons.lang.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@EnableAutoConfiguration
@RequestMapping("/game")
class GameController {
    private final Logger logger = LoggerFactory.getLogger(GameController.class);
    @Autowired
    private GameServiceImpl gameService;

    @RequestMapping(value = "/addGame")
    String addGame(Game game){
        def map = [:]
        try {
            gameService.save(game)
            map.put("msg","房间创建成功！")
            map.put("type",1)
        }catch (Exception e){
            logger.error(e.message)
            map.put("msg","房间创建失败！")
            map.put("type",0)
        }
        return new JsonBuilder(map).toString()
    }
}
