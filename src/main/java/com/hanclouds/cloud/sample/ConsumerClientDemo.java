package com.hanclouds.cloud.sample;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program: kafka-client-demo
 * @description: kafka consumer client demo
 * @author: hanclouds
 * @create: 2018-09-13
 **/
public class ConsumerClientDemo {
    /**
     * @param USER_NAME == productKey
     * PASSWORD = EncryptUtil.encryptPassword(USER_NAME, QUERY_KEY, QUERY_SECRET)
     */
    private static final String PRODUCT_KEY = "wOPt7cgT";
    private static final String QUERY_KEY = "d6HLCE41";
    private static final String QUERY_SECRET = "geTqIWgHaOPV8Crr";
    private static final String USER_NAME = PRODUCT_KEY;
    private static final String KAFKA_SERVERS = "172.16.20.60:9292";
    private static final String GROUP = "group-" + PRODUCT_KEY;
    private static final String TOPIC = PRODUCT_KEY;

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP);
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
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(
                PlainLoginModule.class.getName() + " required username=\"%s\" " + "password=\"%s\";",
                USER_NAME,
                password
        ));
        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        final AtomicBoolean isShuttingDown = new AtomicBoolean(false);
        //订阅topic
        consumer.subscribe(Collections.singletonList(TOPIC));
        //添加ShutdownHook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            isShuttingDown.set(true);
            //consumer 非线程安全
            synchronized (consumer) {
                consumer.close();
            }
        }));
        try {
            while (!isShuttingDown.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    DeviceData deviceData = JSON.parseObject(EncryptUtil.decrypt(QUERY_SECRET, record.value()), DeviceData.class);
                    if (deviceData != null) {
                        System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), deviceData.toString());
                    }
                }
            }
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
