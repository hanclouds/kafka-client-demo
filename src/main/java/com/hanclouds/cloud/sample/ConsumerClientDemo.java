package com.hanclouds.cloud.sample;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.errors.SaslAuthenticationException;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

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
    private static final String PRODUCT_KEY = "K0lB89AQ";
    /**
     * 产品查询Key
     */
    private static final String QUERY_KEY = "HiuI8tDo";
    /**
     * 产品查询secret
     */
    private static final String QUERY_SECRET = "rjZbYTQZSCxSsmy1";
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
    private static final String DATA_SECRET = "IWeENaZsMW5tgIUR";
    /**
     * kafka服务器
     */
    private static final String KAFKA_SERVERS = "cloud-access.hanclouds.com:9292,cloud-access.hanclouds.com:9293,cloud-access.hanclouds.com:9294";
    /**
     * kafka group
     */
    private static final String GROUP = "group-" + PRODUCT_KEY;
    /**
     * 设备数据 topic
     */
    private static final String DATA_TOPIC = PRODUCT_KEY;
    /**
     * 设备命令 topic
     */
    private static final String CMD_TOPIC = "cmd-" + PRODUCT_KEY;
    /**
     * 设备事件 topic
     */
    private static final String CONN_TOPIC = "conn-" + PRODUCT_KEY;



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
        consume(props);
    }

    private static void consume(Properties props){
        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // 订阅topic，这里根据开通的服务填写，如果开通了设备事件及命令则需要加入对应的topic,如果没有开通额外服务，则只需保留DATA_TOPIC即可
        consumer.subscribe(Arrays.asList(DATA_TOPIC,CMD_TOPIC,CONN_TOPIC));
            while (true) {
                try {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records) {
                        if (DATA_TOPIC.equals(record.topic())) {
                            DeviceData deviceData = JSON.parseObject(EncryptUtil.decodeWithAesCbc(DATA_SECRET, record.value()), DeviceData.class);
                            if (deviceData != null) {
                                System.out.printf("topic=%s, partition=%s, offset = %d, key = %s, value = %s%n",
                                        record.topic(), record.partition(), record.offset(), record.key(), deviceData.toString());
                            }
                        }
                        if (CMD_TOPIC.equals(record.topic())) {
                            CmdData cmdData = JSON.parseObject(EncryptUtil.decodeWithAesCbc(DATA_SECRET, record.value()), CmdData.class);
                            if (cmdData != null) {
                                System.out.printf("topic=%s, partition=%s, offset = %d, key = %s, value = %s%n",
                                        record.topic(), record.partition(), record.offset(), record.key(), cmdData.toString());
                            }
                        }
                        if (CONN_TOPIC.equals(record.topic())) {
                            ConnData connData = JSON.parseObject(EncryptUtil.decodeWithAesCbc(DATA_SECRET, record.value()), ConnData.class);
                            if (connData != null) {
                                System.out.printf("topic=%s, partition=%s, offset = %d, key = %s, value = %s%n",
                                        record.topic(), record.partition(), record.offset(), record.key(), connData.toString());
                            }
                        }
                    }
                }catch (SaslAuthenticationException e){
                    e.printStackTrace();
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                    System.exit(1);
                }
            }
            //关闭客户端休眠5秒后进行重连
            consumer.close();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            consume(props);
        }
    }
