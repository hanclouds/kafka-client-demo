/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.junrzz.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.acl.AccessControlEntry;
import org.apache.kafka.common.acl.AclBinding;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.ResourcePattern;
import org.apache.kafka.common.resource.ResourceType;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.ForeachAction;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Produced;

import javax.security.auth.login.Configuration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * Demonstrates, using the high-level KStream DSL, how to read data from a source (input) topic and how to
 * write data to a sink (output) topic.
 *
 * In this example, we implement a simple "pipe" program that reads from a source topic "streams-file-input"
 * and writes the data as-is (i.e. unmodified) into a sink topic "streams-pipe-output".
 *
 * Before running this example you must create the input topic and the output topic (e.g. via
 * bin/kafka-topics.sh --create ...), and write some data to the input topic (e.g. via
 * bin/kafka-console-producer.sh). Otherwise you won't see any data arriving in the output topic.
 */
public class PipeDemo {
    static {
       // System.setProperty("java.security.auth.login.config", "com.junrzz.kafka.simple.SaslConfig");
       // Configuration.setConfiguration(new SaslConfig("admin","admin" ));
    }
/**
* @Description: kafka streams 测试，试图连接两个不同的kafka集群，一个开启认证，一个不开启认证，但是失败了。
 *  就算制定了consumer的bootstrapService 为localhost:9092 但是似乎最终结果consumer还是会变为127.0.0.1:9092，
 *  似乎跟kafka Streams初始化了一个单例的adminClient有关
* @Param: [args]
* @return: void
* @Date: 2018/8/23
*/

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
//        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
//        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        // setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(StreamsConfig.CONSUMER_PREFIX + "sasl.mechanism", "PLAIN");
        props.put(StreamsConfig.CONSUMER_PREFIX + "security.protocol", "SASL_PLAINTEXT");
        props.put(StreamsConfig.CONSUMER_PREFIX + SaslConfigs.SASL_JAAS_CONFIG, String.format(
                PlainLoginModule.class.getName()+ " required username=\"%s\" " + "password=\"%s\";",
                "admin",
                "admin"
        ));

        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.100:9092");
        props.put(StreamsConfig.PRODUCER_PREFIX + "sasl.mechanism", "PLAIN");
        props.put(StreamsConfig.PRODUCER_PREFIX + "security.protocol", "SASL_PLAINTEXT");
     //   props.put(StreamsConfig.PRODUCER_PREFIX + "sasl.kerberos.service.name", "kafka");
        props.put(StreamsConfig.PRODUCER_PREFIX + "group.id", "test-consumer-group");
        props.put(StreamsConfig.PRODUCER_PREFIX + SaslConfigs.SASL_JAAS_CONFIG, String.format(
                PlainLoginModule.class.getName()+ " required username=\"%s\" " + "password=\"%s\";",
                "admin",
                "admin"
        ));

        //props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.100:9092");
        props.put( AdminClientConfig.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        props.put(StreamsConfig.ADMIN_CLIENT_PREFIX+ "sasl.mechanism",  "PLAIN");
        props.put(StreamsConfig.ADMIN_CLIENT_PREFIX + SaslConfigs.SASL_JAAS_CONFIG, String.format(
                PlainLoginModule.class.getName()+ " required username=\"%s\" " + "password=\"%s\";",
                "admin",
                "admin"
        ));

        //      props.put(StreamsConfig.PRODUCER_PREFIX + "System.java.security.auth.login.config", "com.junrzz.kafka.simple.SaslConfig");
        StreamsBuilder builder = new StreamsBuilder();
//        Properties adminProps = new Properties();
//        adminProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
  //      adminProps.put( AdminClientConfig.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
     //   adminProps.put("sasl.mechanism", "PLAIN");
//        adminProps.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(
//                PlainLoginModule.class.getName()+ " required username=\"%s\" " + "password=\"%s\";",
//                "admin",
//                "admin"
//        ));
//        AdminClient admin = AdminClient.create(adminProps);
        builder.stream("streams-plaintext-input").to("test");
        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-pipe-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
