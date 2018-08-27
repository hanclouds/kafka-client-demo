package com.hanclouds.kafka.simple;

import com.hanclouds.kafka.simple.util.EncryptUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

/**
 * @program: kafka-client-demo
 * @description: kafka consumer client demo
 * @author: liujj
 * @create: 2018-08-14 14:21
 **/
public class ConsumerClientDemo {
    /**
     *
     * @param USER_NAME == productKey
     *        PASSWORD = EncryptUtil.encryptPassword(USER_NAME, QUERY_KEY, QUERY_SECRET)
     */

    private static final String USER_NAME = "vh6luFPT";
    private static final String QUERY_KEY = "vobOqKPM";
    private static final String QUERY_SECRET = "r5dsZxFdxvVxJ6qa";


    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.100:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, USER_NAME);
        //指定序列化和反序列化类，也可以自定义
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        //开启SASL_PLAINTEXT
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        //获取加密后的密码
        String password = EncryptUtil.encryptPassword(USER_NAME, QUERY_KEY, QUERY_SECRET);

        //等同于 Configuration.setConfiguration(new SaslConfig("userName","password")); 这种配置方式
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(
                PlainLoginModule.class.getName()+ " required username=\"%s\" " + "password=\"%s\";",
                USER_NAME,
                password
        ));
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //需订阅的topic
        consumer.subscribe(Collections.singletonList("test"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }

        }

    }
}
