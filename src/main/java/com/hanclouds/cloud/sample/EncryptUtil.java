package com.hanclouds.cloud.sample;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.UUID;

/**
 * @description: 加解密工具类
 * @author: hanclouds
 * @create: 2018-09-13
 **/
public class EncryptUtil {

    private static final String AES = "AES";

    /**
     * 解密数据
     *
     * @param secret        秘钥
     * @param secretContent 密文
     * @return  明文数据
     */
    public static String decrypt(String secret, String secretContent) {
        try {
            Key key = new SecretKeySpec(secret.getBytes(), AES);
            Cipher c = Cipher.getInstance(AES);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedValue = Base64.decodeBase64(secretContent);
            byte[] decValue = c.doFinal(decodedValue);
            return StringUtils.newStringUtf8(decValue);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

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
