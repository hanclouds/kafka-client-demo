package com.liujj.kafka;

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
    @Override
    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        String appKey = "WL4odQnH";    //登陆认证用户名
        String secretKey = "mCVmZAka-fS4EKMWTYKS1zZ84"; //登陆认证密码
        Map<String, String> options = new HashMap<String, String>();
        options.put("username", appKey);
        options.put("password", secretKey);
        AppConfigurationEntry entry = new AppConfigurationEntry(
                "org.apache.kafka.common.security.plain.PlainLoginModule",
                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options);
        AppConfigurationEntry[] configurationEntries = new AppConfigurationEntry[1];
        configurationEntries[0] = entry;
        return configurationEntries;
    }
}
