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
            gameService.clearMember(game.id)
            if (CacheUtil.getCache("lover-"+gameId) != null){//清除丘比特标识缓存
                CacheUtil.del("lover-"+gameId)
            }
            game.status = 0
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
                   @RequestParam(value = "uid") long uid){
        def map = [:]
        try {
            if (gameService.hasJoin(gameId,uid)) {//不加横杠的话区分不出1-23，和12-3
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
            if (game.status != 1){
                map.put("code", 0)
                map.put("msg","房间当前状态不允许报名！")
                return new JsonBuilder(map).toString()
            }
            if (gameService.existName(gameId,name)){
                map.put("code", 0)
                map.put("msg","昵称已经存在，请更换！")
                return new JsonBuilder(map).toString()
            }
            if (gameService.isFull(game)){
                map.put("code", 0)
                map.put("msg","报名人数已达上限")
                return new JsonBuilder(map).toString()
            }
            def memberId = gameService.addMember(name,gameId)
            if (memberId > 0){
                CacheUtil.putCache(gameId+"-"+memberId,1,CacheUtil.MEMCACHED_ONE_DAY)
                map.put("code", 1)
                map.put("uid",memberId)
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

    //查看身份
    @RequestMapping(value = "/showRole")
    String showRole(@RequestParam(value = "gameId") long gameId,
                   @RequestParam(value = "uid") long uid){
        def map = [:]
        try {
            map.put("code",gameService.showRole(gameId,uid))
        }catch (Exception e){
            logger.error(e.message)
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    //显示身份功能
    @RequestMapping(value = "/showBtn")
    String showBtn(@RequestParam(value = "gameId") long gameId,
                    @RequestParam(value = "uid") long uid){
        def map = [:]
        try {
            map = gameService.showBtn(gameId,uid)
        }catch (Exception e){
            logger.error(e.message)
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    //狼人杀人
    @RequestMapping(value = "/werewolf")
    String werewolf(@RequestParam(value = "kill") long kill,
                   @RequestParam(value = "uid") long uid){
        def map = [:]
        try {
            map.put("code",gameService.werewolf(uid,kill))
        }catch (Exception e){
            logger.error(e.message)
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    //预言家验人
    @RequestMapping(value = "/prophet")
    String prophet(@RequestParam(value = "guess") long guess,
                    @RequestParam(value = "uid") long uid){
        def map = [:]
        try {
            map = gameService.prophet(uid,guess)
        }catch (Exception e){
            logger.error(e.message)
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    //守卫守人
    @RequestMapping(value = "/guard")
    String guard(@RequestParam(value = "protect") long protect,
                   @RequestParam(value = "uid") long uid){
        def map = [:]
        try {
            map.put("code",gameService.guard(uid,protect))
        }catch (Exception e){
            logger.error(e.message)
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    //查狼人杀了谁
    @RequestMapping(value = "/killByWolf")
    String killByWolf(@RequestParam(value = "gameId") long gameId){
        def map = [:]
        try {
            map = gameService.killByWolf(gameId)
        }catch (Exception e){
            logger.error(e.message)
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    //女巫
    @RequestMapping(value = "/witch")
    String witch(@RequestParam(value = "witch") long witch,
                 @RequestParam(value = "save") int save,
                 @RequestParam(value = "uid") long uid){
        def map = [:]
        try {
            map.put("code",gameService.witch(uid,witch,save))
        }catch (Exception e){
            logger.error(e.message)
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    //丘比特
    @RequestMapping(value = "/cupid")
    String cupid(@RequestParam(value = "lover1") long lover1,
                 @RequestParam(value = "lover2") long lover2,
                 @RequestParam(value = "code") int code,
                 @RequestParam(value = "uid") long uid){
        def map = [:]
        try {
            map.put("code",gameService.cupid(uid,lover1,lover2,code))
        }catch (Exception e){
            logger.error(e.message)
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }

    //查看情侣
    @RequestMapping(value = "/lover")
    String lover(@RequestParam(value = "uid") long uid){
        def map = [:]
        try {
            map = gameService.isLover(uid)
        }catch (Exception e){
            logger.error(e.message)
            map.put("code",0)
        }
        return new JsonBuilder(map).toString()
    }
}
