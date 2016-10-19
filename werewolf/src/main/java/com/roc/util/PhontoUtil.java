package com.roc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Administrator on 2016/8/18.
 */
public class PhontoUtil {
    private static final Logger logger = LoggerFactory.getLogger(PhontoUtil.class);
    public static final String IMG_URL = "E:\\joke\\";

    public static boolean savePhonto(MultipartFile file, long alertId){
        String filePath = IMG_URL;
        try {
            File localFile = new File(filePath+alertId+".jpg");
            file.transferTo(localFile);//MultipartFile自带的方法
            //裁剪图片
            BufferedImage input = ImageIO.read(localFile);
            BufferedImage inputbig = new BufferedImage(300, 300, BufferedImage.TYPE_INT_BGR);
            Graphics2D g = (Graphics2D) inputbig.getGraphics();
            g.drawImage(input, 0, 0,300,300,null); //画图
            g.dispose();
            inputbig.flush();
            ImageIO.write(inputbig, "jpg", new File(filePath+alertId+".jpg"));
            return true;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }
    }

}
