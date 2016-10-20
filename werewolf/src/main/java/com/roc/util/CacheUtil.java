package com.roc.util;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/18.
 */
@EnableAutoConfiguration
public class CacheUtil {
    public final static int MEMCACHED_ONE_DAY = 60 * 60 * 24;
    public final static int MEMCACHED_ONE_MONTH = 30 * 60 * 60 * 24;
    private static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);
    private static MemcachedClientBuilder builder;
    private static MemcachedClient memcachedClient;
    static {
        builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("localhost:11211"));
        builder.setFailureMode(true);
        try {
            memcachedClient = builder.build();
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }
    /**
     * 存放到缓存里
     *
     * @param key
     * @param obj
     * @param time
     * @return
     */
    public static boolean putCache(String key, Object obj, int time) {
        boolean result = false;
        try {
            if (obj != null) {
                memcachedClient.setWithNoReply(key, time, obj);
                result = true;
            }
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        if (!result) {
            logger.warn("CacheUtil.putCache failed. key[" + key + "] obj[" + obj.toString()+"]");
        }
        return result;
    }
    /**
     * 通过Key获得缓存对象
     *
     * @param key
     * @return
     */
    public static Serializable getCache(String key) {
        try {
            return memcachedClient.get(key, 10000L);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        return null;
    }
}
