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
     * 产品Key
     */
    private static final String PRODUCT_KEY = "productKey";
    /**
     * 产品查询Key
     */
    private static final String QUERY_KEY = "queryKey";
    /**
     * 产品查询secret
     */
    private static final String QUERY_SECRET = "querySecret";
    /**
     * kafka认证所需的用户名
     * USER_NAME == productKey
     */
    private static final String USER_NAME = PRODUCT_KEY;
    /**
     * kafka认证所需的密码
     */
    private static final String PASSWORD = EncryptUtil.encryptPassword(USER_NAME, QUERY_KEY, QUERY_SECRET);
    /**
     * 数据加解密所需的密码
     */
    private static final String DATA_SECRET = "dataSecret";
    /**
     * kafka服务器
     */
    private static final String KAFKA_SERVERS = "cloud-access.hanclouds.com:9292,cloud-access.hanclouds.com:9293,cloud-access.hanclouds.com:9294";
    /**
     * kafka group
     */
    private static final String GROUP = "group-" + PRODUCT_KEY;
    /**
     * kafka topic
     */
    private static final String TOPIC = PRODUCT_KEY;

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, USER_NAME);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 开启SASL_PLAINTEXT
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        props.put(SaslConfigs.SASL_MECHANISM, "PLAIN");

        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(
                PlainLoginModule.class.getName() + " required username=\"%s\" " + "password=\"%s\";",
                USER_NAME,
                PASSWORD
        ));
        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // 订阅topic
        consumer.subscribe(Collections.singletonList(TOPIC));

        // 添加钩子，确保程序退出时（比如按下Ctrl+C），consumer正确关闭
        final AtomicBoolean isShuttingDown = new AtomicBoolean(false);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            isShuttingDown.set(true);
            //consumer 非线程安全
            synchronized (consumer) {
                consumer.close();
            }
        }));
        try {
            while (!isShuttingDown.get()) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    DeviceData deviceData = JSON.parseObject(EncryptUtil.decodeWithAesCbc(DATA_SECRET, record.value()), DeviceData.class);
                    if (deviceData != null) {
                        System.out.printf("topic=%s, partition=%s, offset = %d, key = %s, value = %s%n",
                                record.topic(), record.partition(),record.offset(), record.key(), deviceData.toString());
                    }
                }
            }
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
