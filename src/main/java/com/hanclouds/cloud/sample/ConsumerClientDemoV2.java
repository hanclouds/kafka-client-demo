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
import java.util.concurrent.TimeUnit;

/**
 * @program: kafka-client-demo
 * @description:
 * @author: liujj
 * @create: 2019-09-29 15:40
 **/
public class ConsumerClientDemoV2 {
    /**
     * 产品Key
     */
    private static final String PRODUCT_KEY = "zfkSOHDk";
    /**
     * 产品查询Key
     */
    private static final String QUERY_KEY = "b3ZoadaF";
    /**
     * 产品查询secret
     */
    private static final String QUERY_SECRET = "pJr133l3SOexDKTr";
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
    private static final String DATA_SECRET = "RRGGVjjado83o9P2";
    /**
     * kafka服务器
     */
    private static final String KAFKA_SERVERS = "172.16.20.62:9292";
    /**
     * kafka group
     */
    private static final String GROUP = "group-" + PRODUCT_KEY;
    /**
     * 设备数据 topic
     */
    private static final String DATA_TOPIC = PRODUCT_KEY;



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
                PRODUCT_KEY,
                PASSWORD
        ));
        consume(props);
    }

    private static void consume(Properties props){
        final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // 订阅topic
        consumer.subscribe(Collections.singletonList(DATA_TOPIC));
        while (true) {
            try {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                         PushServiceData accessData = JSON.parseObject(EncryptUtil.decodeWithAesCbc(DATA_SECRET, record.value()), PushServiceData.class);
                         if (accessData != null){
                             if (accessData.getDataType().equals(PushServiceDataTypeEnum.DEVICE_DATA.intValue())){
                                 DeviceData deviceData = JSON.parseObject(accessData.getData(),DeviceData.class);
                                 System.out.printf("topic=%s, partition=%s, offset = %d, key = %s, value = %s%n",
                                         record.topic(), record.partition(), record.offset(), record.key(), deviceData.toString());
                             }
                             if (accessData.getDataType().equals(PushServiceDataTypeEnum.DEVICE_CONN.intValue())){
                                 ConnData connData = JSON.parseObject(accessData.getData(),ConnData.class);
                                 System.out.printf("topic=%s, partition=%s, offset = %d, key = %s, value = %s%n",
                                         record.topic(), record.partition(), record.offset(), record.key(), connData.toString());
                             }
                             if (accessData.getDataType().equals(PushServiceDataTypeEnum.DEVICE_CMD.intValue())){
                                 CmdData cmdData = JSON.parseObject(accessData.getData(),CmdData.class);
                                 System.out.printf("topic=%s, partition=%s, offset = %d, key = %s, value = %s%n",
                                         record.topic(), record.partition(), record.offset(), record.key(), cmdData.toString());
                             }
                         }

                }
            }catch (Exception e){
                e.printStackTrace();
                break;
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
