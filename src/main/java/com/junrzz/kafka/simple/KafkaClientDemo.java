package com.junrzz.kafka.simple;

import com.junrzz.kafka.simple.util.EncryptUtil;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.streams.StreamsConfig;

import javax.security.auth.login.Configuration;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @program: kafkaclientdemo
 * @description:
 * @author: liujj
 * @create: 2018-08-14 14:21
 **/
public class KafkaClientDemo {
    /**
     * 使用producer 注释掉consumer部分的代码及 group.id，key.deserializer，value.deserializer 属性
     *
     * @param USER_NAME == productKey,password = EncryptUtil.encryptPassword(USER_NAME, QUERY_KEY, QUERY_SECRET)
     */

    private static final String USER_NAME = "WL4odQnH";
    private static final String QUERY_KEY = "mCVmZAka";
    private static final String QUERY_SECRET = "fS4EKMWTYKS1zZ84";


    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.100:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("group.id", "test-consumer-group");
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("client.id", USER_NAME+"-test-1");
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        props.put("sasl.mechanism", "PLAIN");
        //获取加密后的密码
        String password = EncryptUtil.encryptPassword(USER_NAME, QUERY_KEY, QUERY_SECRET);

        //等同于 Configuration.setConfiguration(new SaslConfig("userName","password")); 这种配置方式
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(
                PlainLoginModule.class.getName()+ " required username=\"%s\" " + "password=\"%s\";",
                USER_NAME,
                password
        ));
  //      Configuration.setConfiguration(new SaslConfig("admin","admin"));

//        Producer<String, String> producer = new KafkaProducer<>(props);
//        for (int i = 0; i < 100; i++){
//            producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), Integer.toString(i)));
//        }
//        producer.close();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("adminClient"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }

        }

    }
}
