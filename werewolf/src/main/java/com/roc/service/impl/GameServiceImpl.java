package com.roc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.roc.dao.GameDao;
import com.roc.dao.MemberDao;
import com.roc.enity.Game;
import com.roc.enity.Player;
import com.roc.service.GameService;
import com.roc.util.CacheUtil;
import com.roc.util.GameUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.util.*;

@SpringBootApplication
@Service
public class GameServiceImpl implements GameService{
    private final Logger logger = LoggerFactory.getLogger(GameServiceImpl.class);
    @Autowired
    private GameDao gameDao;
    @Autowired
    private MemberDao memberDao;

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

    @Override
    public List<JSONObject> getGameList(long uid) {
        try {
            List<JSONObject> infoList = new ArrayList<JSONObject>();
            List<Game> gameList = gameDao.getGameList(uid);
            for (Game game:gameList){
                JSONObject json = new JSONObject();
                json.put("id",game.getId());
                json.put("name",game.getName());
                json.put("start",game.getStatus());
                GameUtil.putMember(game.getRole(),json);
                infoList.add(json);
            }
            return infoList;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Game getGame(long id) {
        return gameDao.findOne(id);
    }

    @Override
    public boolean existName(long gameId, String name) {
        try {
            List<Player> playerList = memberDao.getOneOfMember(gameId,name);
            if (playerList.size() > 0)
                return true;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean isFull(Game game) {
        try {
            List<Player> playerList = memberDao.getAllMember(game.getId());
            if (playerList.size() == game.getNum())
                return true;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public long addMember(String name, long gameId) {
        try {
            Player member = new Player();
            member.setGid(gameId);
            member.setName(name);
            member.setStatus(0);
            member.setDied(0);
            member.setGuarded(0);
            member.setNight("");
            memberDao.save(member);
            return member.getId();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public void clearMember(long gameId) {
        //从member表获取id清缓存,删除member表里的数据
        try {
            List<Player> memList = memberDao.getAllMember(gameId);
            for (Player player:memList){
                if (CacheUtil.getCache(gameId+"-"+player.getId()) != null){
                    CacheUtil.del(gameId+"-"+player.getId());
                }
                memberDao.delete(player.getId());
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
    }

    @Override
    public List<String> getMember(long id) {
        List<String> memList = null;
        try {
            memList = new ArrayList<String>();
            List<Player> playerList = memberDao.getAllMember(id);
            for (Player player:playerList){
                memList.add(player.getName());
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return memList;
    }

    @Override
    public int showRole(long gameId, long uid) {
        try {
            Game game = gameDao.findOne(gameId);
            Player player = memberDao.findOne(uid);
            if (player.getGid() == game.getId()){
                if (game.getNum() == memberDao.getAllMember(gameId).size()){
                    return 1;
                }else {
                    return -1;
                }
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean hasJoin(long gameId, long uid) {
        try {
            if(CacheUtil.getCache(gameId+"-"+uid) != null){
                return true;
            }else {
                Game game = gameDao.findOne(gameId);
                Player member = memberDao.findOne(uid);
                if (game !=null && member != null && game.getStatus() > 0 && member.getGid() == gameId){
                    CacheUtil.putCache(gameId+"-"+uid,1,CacheUtil.MEMCACHED_ONE_DAY);
                    return true;
                }
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public void putRole(Game game) {
        try {
            String[] roles = game.getRole().split("-");
            List<Player> memberList = memberDao.getAllMember(game.getId());
            if (roles.length == memberList.size() && roles.length == game.getNum()){
                Collections.shuffle(memberList);//乱序，分配身份
                for (int i=0;i<roles.length;i++){
                    Player member = memberList.get(i);
                    member.setRole(roles[i]);
                    memberDao.save(member);
                    if (roles[i].equals("丘比特")){
                        CacheUtil.putCache("lover-"+game.getId(),"-",CacheUtil.MEMCACHED_ONE_DAY);
                    }
                }
            }else {
                logger.error("putRole|人数不对应|role:" + roles.length + ",num:" + game.getNum() + ",member:" + memberList.size());
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
    }

    @Override
    public Map<String,Object> showBtn(long gameId, long uid) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            Player player = memberDao.findOne(uid);
            //是否行使过能力
            map.put("isShow",player.getStatus());
            //是否有功能按钮
            String role = player.getRole();
            //角色名
            map.put("roleName",role);
            map.put("lover",0);
            if (role.equals("平民") || role.equals("白痴") || role.equals("猎人")){
                map.put("hasBtn",0);
                if (CacheUtil.getCache("lover-"+gameId) == null){//有缓存用缓存，没有去查库
                    List<Player> memberList = memberDao.getAllMember(gameId);
                    for (Player member:memberList){
                        if (member.getRole().equals("丘比特")){
                            map.put("lover",1);
                            CacheUtil.putCache("lover-"+gameId,"-",CacheUtil.MEMCACHED_ONE_DAY);
                            break;
                        }
                    }
                }else {
                    map.put("lover",1);
                }
            }else {
                map.put("role",role.equals("狼人")?"#werewolf":role.equals("预言家")?"#prophet":role.equals("守卫")?"#guard":role.equals("女巫")?"#witch":"#cupid");
                map.put("hasBtn",1);
                List<Player> memberList = memberDao.getAllMember(gameId);
                List<JSONObject> userList = new ArrayList<JSONObject>();
                if (role.equals("女巫")){
                    JSONObject json = new JSONObject();
                    json.put("id", 0);
                    json.put("name","不毒人");
                    userList.add(json);
                }
                for (Player member:memberList){
                    if (member.getId() != uid) {
                        JSONObject json = new JSONObject();
                        json.put("id", member.getId());
                        json.put("name",member.getName());
                        userList.add(json);
                    }
                    if (member.getRole().equals("丘比特")){
                        map.put("lover",1);
                    }
                }
                if (!role.equals("丘比特")){//丘比特不能连自己，其他可以自刀，自毒，自守，自验
                    JSONObject json = new JSONObject();
                    json.put("id", player.getId());
                    json.put("name",player.getName());
                    userList.add(json);
                }
                map.put("member",userList);
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error("showBtn|"+e.getMessage());
        }
        return map;
    }

    @Override
    public int werewolf(long uid, long kill) {
        try {
            Player werewolf = memberDao.findOne(uid);
            Player diedMan = memberDao.findOne(kill);
            werewolf.setDied(diedMan.getId());
            werewolf.setNight("狼人" + werewolf.getName() + "杀了" + diedMan.getName());
            werewolf.setStatus(1);
            memberDao.save(werewolf);
            return 1;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public Map<String,Object> prophet(long uid, long guess) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            Player prophet = memberDao.findOne(uid);
            Player guessMan = memberDao.findOne(guess);
            if (guessMan.getRole().equals("狼人")) {
                map.put("msg", guessMan.getName() + "是狼人");
            }else {
                map.put("msg", guessMan.getName() + "是好人");
            }
            prophet.setNight("预言家" + prophet.getName() + "验了" + guessMan.getName() + "," + map.get("msg"));
            prophet.setStatus(1);
            memberDao.save(prophet);
            map.put("code",1);
            return map;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        map.put("code",0);
        return map;
    }

    @Override
    public int guard(long uid, long protect) {
        try {
            Player guard = memberDao.findOne(uid);
            Player user = memberDao.findOne(protect);
            guard.setNight("守卫" + guard.getName() + "守护了" + user.getName());
            guard.setStatus(1);
            if (uid == protect){
                guard.setGuarded(guard.getGuarded()+1);
            }else {
                user.setGuarded(user.getGuarded()+1);
                memberDao.save(user);
            }
            memberDao.save(guard);
            return 1;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public Map<String, Object> killByWolf(long gameId) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            List<Long> diedList = memberDao.killByWolf(gameId);
            Map<Long,Integer> num = new HashMap<Long, Integer>();
            for (long id:diedList){
                if (num.get(id) == null){
                    num.put(id,1);
                }else {
                    num.put(id,num.get(id)+1);
                }
            }
            int repeat = 0;
            long maxId = 0;
            int max = 0;
            for (long id:num.keySet()){
                if (num.get(id) > max && id != 0){
                    repeat = 0;
                    maxId = id;
                    max = num.get(id);
                }else if (num.get(id) == max && id != 0){
                    repeat = 1;
                }
            }
            if (repeat == 1 || maxId == 0){
                map.put("uid",0);
                map.put("name","无人死亡");
            }else {
                map.put("uid",maxId);
                map.put("name",memberDao.findOne(maxId).getName());
            }
            map.put("code",1);
            return map;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        map.put("code",0);
        return map;
    }

    @Override
    public int witch(long uid, long witch, int save) {
        try {
            Player user = memberDao.findOne(witch);
            if (uid > 0){
                Player player = memberDao.findOne(uid);
                if (save == 1){//解药+1毒药+2
                    player.setGuarded(player.getGuarded()+1);
                    user.setNight("女巫"+user.getName()+"救了"+player.getName());
                }else {
                    player.setGuarded(player.getGuarded()+2);
                    user.setNight("女巫"+user.getName()+"毒了"+player.getName());
                }
                memberDao.save(player);
            }else {
                user.setNight("女巫"+user.getName()+"没有用药");
            }
            user.setStatus(1);
            memberDao.save(user);
            return 1;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public int cupid(long uid, long lover1, long lover2, int code) {
        try {
            Player user = memberDao.findOne(uid);
            Player loverOne = memberDao.findOne(lover1);
            Player loverTwo = memberDao.findOne(lover2);
            if (code == 1 && user.getGid() == loverOne.getGid() && user.getGid() == loverTwo.getGid()){
                CacheUtil.putCache("lover-"+user.getGid(),lover1+"-"+lover2,CacheUtil.MEMCACHED_ONE_DAY);
                user.setNight("丘比特"+user.getName()+"连了情侣"+loverOne.getName()+"和"+loverTwo.getName());
            }else {
                user.setNight("丘比特"+user.getName()+"没有连情侣");
            }
            user.setStatus(1);
            memberDao.save(user);
            return 1;
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public Map<String, Object> isLover(long uid) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("code",1);
        try {
            Player user = memberDao.findOne(uid);
            String lovers = String.valueOf(CacheUtil.getCache("lover-" + user.getGid()));
            if (lovers != null && String.valueOf(lovers).split("-").length == 2){
                String[] couple = String.valueOf(lovers).split("-");
                if (String.valueOf(uid).equals(couple[0]) || String.valueOf(uid).equals(couple[1])){
                    Player myLover = memberDao.findOne(Long.valueOf(String.valueOf(uid).equals(couple[0])?couple[1]:couple[0]));
                    map.put("title","你被连为情侣");
                    map.put("msg","你的情侣是："+ myLover.getName());
                    return map;
                }
            }
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
        }
        map.put("title","你不是情侣");
        map.put("msg","");
        return map;
    }

    @Override
    public Map<String, Object> showResult(long gameId) {
        Map<String,Object> map = new HashMap<String, Object>();
        try {
            //被狼刀的人
            List<Long> diedList = memberDao.killByWolf(gameId);
            Map<Long,Integer> num = new HashMap<Long, Integer>();
            for (long id:diedList){
                if (num.get(id) == null){
                    num.put(id,1);
                }else {
                    num.put(id,num.get(id)+1);
                }
            }
            int repeat = 0;
            long maxId = 0;
            int max = 0;
            for (long id:num.keySet()){
                if (num.get(id) > max && id != 0){
                    repeat = 0;
                    maxId = id;
                    max = num.get(id);
                }else if (num.get(id) == max && id != 0){
                    repeat = 1;
                }
            }
            Player killByWolf = memberDao.findOne(maxId);
            if (repeat == 0 && maxId > 0){//有人被狼杀死
                if (killByWolf.getGuarded() > 1) {//被刀的人又被女巫和守卫弄死（双救或毒）
                    map.put("title", "昨夜死的人是:");
                    map.put("msg", killByWolf.getName());
                }else {
                    List<Player> killByWith = memberDao.killByWitch(gameId);
                    if (killByWith != null && killByWith.size() > 0){//毒
                        map.put("title", "昨夜双死:");
                        map.put("msg", killByWolf.getName()+"和"+killByWith.get(0).getName()+"死亡");
                    }else if (killByWolf.getGuarded() == 1){//救|守
                        map.put("title", "昨夜平安夜");
                        map.put("msg", "没有人死亡");
                    }else {
                        map.put("title", "昨夜死的人是:");
                        map.put("msg", killByWolf.getName());
                    }
                }
            }else {//狼没杀人
                List<Player> killByWith = memberDao.killByWitch(gameId);
                if (killByWith != null && killByWith.size() > 0){//毒
                    map.put("title", "昨夜死的人是:");
                    map.put("msg", killByWith.get(0).getName());
                }else {
                    map.put("title", "昨夜平安夜");
                    map.put("msg", "没有人死亡");
                }
            }
            map.put("code",1);
        }catch (Exception e){//处理了异常可能无法触发事物
            logger.error(e.getMessage());
            map.put("code", 0);
        }
        return map;
    }

    @Override
    public List<String> getNightInfo(long gameId) {
        List<Player> member = memberDao.getAllMember(gameId);
        List<String> nightInfo = new ArrayList<String>();
        List<String> wolfInfo = new ArrayList<String>();
        List<String> godInfo = new ArrayList<String>();
        for (Player user : member){
            if (user.getRole().equals("猎人") || user.getRole().equals("白痴")){
                nightInfo.add(user.getName()+"是"+user.getRole());
            }else if (StringUtils.isNotBlank(user.getNight())){
                if (user.getRole().equals("狼人")){
                    wolfInfo.add(user.getNight());
                }else {
                    godInfo.add(user.getNight());
                }
            }
        }
        nightInfo.addAll(wolfInfo);
        nightInfo.addAll(godInfo);
        return nightInfo;
    }
}
