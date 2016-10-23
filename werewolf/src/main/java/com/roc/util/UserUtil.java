package com.roc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserUtil {
    private static final Logger logger = LoggerFactory.getLogger(UserUtil.class);

    public static int getRondomNum(){
        Random random = new Random();
        return random.nextInt(100000);
    }
}
