package com.roc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/4/26.
 */
public class TimeUtil {
    private static final Logger logger = LoggerFactory.getLogger(TimeUtil.class);
    /**
     * 获取当前时间
     * @return
     */
    public static String getNowTime(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

}
