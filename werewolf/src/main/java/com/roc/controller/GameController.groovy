package com.roc.controller

import com.roc.enity.Game
import com.roc.service.impl.GameServiceImpl
import com.roc.util.CacheUtil
import com.roc.util.GameUtil
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
            map.put("code",1)
        }catch (Exception e){
            logger.error(e.message)
            map.put("msg","房间创建异常！")
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/closeGame")
    String closeGame(@RequestParam(value = "gameId") long gameId){
        def map = [:]
        try {
            def game = gameService.getGame(gameId)
            gameService.clearMember(game.id,game.players)
            game.status = 0
            game.result = ""
            game.players = ""
            gameService.save(game)
            map.put("code",1)
        }catch (Exception e){
            logger.error(e.message)
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    //是否已报名
    @RequestMapping(value = "/hasJoin")
    String hasJoin(@RequestParam(value = "gameId") long gameId,
                   @RequestParam(value = "name") String name){
        def map = [:]
        try {
            if (CacheUtil.getCache(gameId+"-"+name) != null) {//不加横杠的话区分不出1-23，和12-3
                map.put("code", 1)
            }else {
                map.put("code", 0)
            }
        }catch (Exception e){
            logger.error(e.message)
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    @RequestMapping(value = "/joinGame")
    String joinGame(@RequestParam(value = "gameId") long gameId,
                   @RequestParam(value = "name") String name){
        def map = [:]
        try {
            def game = gameService.getGame(gameId)
            if (game == null){
                map.put("code", 0)
                map.put("msg","房间不存在")
                return new JsonBuilder(map).toString()
            }
            if (StringUtils.isBlank(name)){
                map.put("code", 0)
                map.put("msg","昵称为空")
                return new JsonBuilder(map).toString()
            }
            if (name.equals("role")){
                map.put("code", 0)
                map.put("msg","这个昵称不许用，不服你来咬我！")
                return new JsonBuilder(map).toString()
            }
            if (CacheUtil.getCache(gameId+"-"+name) != null){
                map.put("code", 0)
                map.put("msg","昵称已经存在，请更换！")
                return new JsonBuilder(map).toString()
            }
            if (GameUtil.isFull(game)){
                map.put("code", 0)
                map.put("msg","报名人数已达上限")
                return new JsonBuilder(map).toString()
            }
            game.players = GameUtil.addMember(name,game.players)
            if (gameService.save(game) == 1){
                CacheUtil.putCache(gameId+"-"+name,1,CacheUtil.MEMCACHED_ONE_DAY)
                map.put("code", 1)
            }else {
                map.put("code", 0)
                map.put("msg","报名失败")
            }

        }catch (Exception e){
            logger.error(e.message)
            map.put("code",0)
            map.put("msg","保存异常")
        }
        return new JsonBuilder(map).toString()
    }
}
