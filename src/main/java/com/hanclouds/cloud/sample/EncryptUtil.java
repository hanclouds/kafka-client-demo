package com.hanclouds.cloud.sample;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @description: 加解密工具类
 * @author: hanclouds
 * @create: 2018-09-13
 **/
public class EncryptUtil {

    private final static String AES_CBC = "AES/CBC/PKCS5Padding";
    private final static int AES_KEY_SIZE = 16;
    private final static String HMACSHA1 = "HmacSHA1";
    private final static String AES = "AES";

    /**
     * 解密数据
     *
     * @param secret        秘钥
     * @param contentString 密文
     * @return  明文数据
     */
    public static String decodeWithAesCbc(String secret,String contentString) {
        if (secret.length() < AES_KEY_SIZE) {
            return null;
        }
        if (secret.length() > AES_KEY_SIZE) {
            secret = secret.substring(0, AES_KEY_SIZE);
        }
        try {
            byte[] content = Base64.decodeBase64(contentString);
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8.name()), AES);
            Cipher cipher = Cipher.getInstance(AES_CBC);
            byte[] ivBytes = new byte[16];
            System.arraycopy(content, 0, ivBytes, 0, 16);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] encBytes = new byte[content.length - 16];
            System.arraycopy(content, 16, encBytes, 0, encBytes.length);
            byte[] result = cipher.doFinal(encBytes);
            return new String(result);
        } catch (Exception e) {
           e.printStackTrace();

        }
        return null;
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
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8.name());
            SecretKey secretKey = new SecretKeySpec(keyBytes, HMACSHA1);
            Mac mac = Mac.getInstance(HMACSHA1);
            mac.init(secretKey);
            byte[] rawHmac = mac.doFinal(content.getBytes(StandardCharsets.UTF_8.name()));
            return (new BASE64Encoder()).encode(rawHmac);
        } catch (Exception var) {
            var.printStackTrace();
        }
        return null;
    }
}
