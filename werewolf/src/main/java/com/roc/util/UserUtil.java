package com.roc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class UserUtil {
    private static final Logger logger = LoggerFactory.getLogger(UserUtil.class);

    public static int getRondomNum(){
        Random random = new Random();
        return random.nextInt(100000);
    }
}
