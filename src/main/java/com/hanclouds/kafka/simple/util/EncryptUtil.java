package com.hanclouds.kafka.simple.util;

import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

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

    public static String encryptPassword(String productKey, String queryKey, String querySecret) {
        final String bar = "\u002d";
        final String nonce = UUID.randomUUID().toString().replaceAll(bar, "");
        long timestamp = System.currentTimeMillis();
        String content = String.format("%s%s%s%s%s%s%s", productKey, bar, queryKey, bar, nonce, bar, timestamp);
        String tempSignature = signWithHmacsh1(querySecret, content);
        System.out.println("1:" + String.format("%s%s%s%s%s", tempSignature, bar, nonce, bar, timestamp));
        return String.format("%s%s%s%s%s", tempSignature, bar, nonce, bar, timestamp);
    }

    private static String signWithHmacsh1(String secret, String content) {
        try {
            byte[] keyBytes = secret.getBytes("utf-8");
            SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            byte[] rawHmac = mac.doFinal(content.getBytes("utf-8"));
            String hmacSHA1Encode = (new BASE64Encoder()).encode(rawHmac);
            return hmacSHA1Encode;
        } catch (Exception var) {
            var.printStackTrace();
        }
        return null;
    }
}
