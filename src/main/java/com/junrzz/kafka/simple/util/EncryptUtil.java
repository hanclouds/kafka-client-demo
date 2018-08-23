package com.junrzz.kafka.simple.util;

import com.hanclouds.util.CryptoUtils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @program: kafka-client-demo
 * @description: 加密password 工具类
 * @author: liujj
 * @create: 2018-08-23 10:31
 **/
public class EncryptUtil {
    /**
    * @Description:
    * @Param: [productKey, queryKey, querySecret]
    * @return: java.lang.String
    * @Date: 2018/8/23
    */

    public static String encryptPassword(String productKey,String queryKey,String querySecret){
       final int randomNumber = ThreadLocalRandom.current().nextInt(100);
       long timestamp = System.currentTimeMillis();
        String content = productKey + "-" + queryKey + "-" + randomNumber + "-"+ timestamp;
        String tempSignature = CryptoUtils.signWithHmacsh1(querySecret, content);
        return tempSignature + "-" + randomNumber + "-" + timestamp;
    }
    public static void main(String[] args){
        System.out.println(encryptPassword("WL4odQnH","mCVmZAka","fS4EKMWTYKS1zZ84"));
    }
}
