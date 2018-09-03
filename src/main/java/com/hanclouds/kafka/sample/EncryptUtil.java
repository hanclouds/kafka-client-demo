package com.hanclouds.kafka.sample;

import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.UUID;

/**
 * @program: kafka-client-demo
 * @description: 加密password 工具类
 * @author: hanclouds
 * @create: 2018-09-13
 **/
public class EncryptUtil {


    /**
     * 加密密码
     *
     * @param productKey    产品key
     * @param queryKey      产品的查询Key
     * @param querySecret   产品的查询Secret
     * @return
     */
    public static String encryptPassword(String productKey, String queryKey, String querySecret) {
        final String bar = "\u002d";
        final String nonce = UUID.randomUUID().toString().replaceAll(bar, "");
        long timestamp = System.currentTimeMillis();
        String content = String.format("%s%s%s%s%s%s%s", productKey, bar, queryKey, bar, nonce, bar, timestamp);
        String tempSignature = signWithHmacsha1(querySecret, content);
        System.out.println("1:" + String.format("%s%s%s%s%s", tempSignature, bar, nonce, bar, timestamp));
        return String.format("%s%s%s%s%s", tempSignature, bar, nonce, bar, timestamp);
    }

    /**
     * HMACSHA1签名
     *
     * @param secret    秘钥
     * @param content   明文
     * @return  密文
     */
    private static String signWithHmacsha1(String secret, String content) {
        try {
            byte[] keyBytes = secret.getBytes("utf-8");
            SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
            byte[] rawHmac = mac.doFinal(content.getBytes("utf-8"));
            return (new BASE64Encoder()).encode(rawHmac);
        } catch (Exception var) {
            var.printStackTrace();
        }
        return null;
    }
}
