package com.hanclouds.kafka.simple;


import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: kafkaclientdemo
 * @description:
 * @author: liujj
 * @create: 2018-08-14 18:01
 **/
public class SaslConfig extends Configuration {
    private String userName;    //登录认证用户名 （==productKey)
    private String password;    //登录认证密码   (==加密后的字符串-随机数-时间戳)

    public SaslConfig(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        Map<String, String> options = new HashMap<String, String>();
        options.put("username", userName);
        options.put("password", password);
        AppConfigurationEntry entry = new AppConfigurationEntry(
                "org.apache.kafka.common.security.plain.PlainLoginModule",
                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options);
        AppConfigurationEntry[] configurationEntries = new AppConfigurationEntry[1];
        configurationEntries[0] = entry;
        return configurationEntries;
    }
}