package com.junrzz.kafka.simple;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.acl.AccessControlEntry;
import org.apache.kafka.common.acl.AclBinding;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.acl.AclPermissionType;
import org.apache.kafka.common.resource.PatternType;
import org.apache.kafka.common.resource.Resource;
import org.apache.kafka.common.resource.ResourcePattern;
import org.apache.kafka.common.resource.ResourceType;

import javax.security.auth.login.Configuration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: kafka-client-demo
 * @description:
 * @author: liujj
 * @create: 2018-08-15 15:20
 **/
public class AdminClientDemo {
    public static AdminClient adminClient;
    static {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.1.100:9092");
        properties.put( AdminClientConfig.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        properties.put("sasl.mechanism", "PLAIN");
        Configuration.setConfiguration(new SaslConfig("admin","admin"));
        adminClient = AdminClient.create(properties);
    }
    /**
     * Principal P is [Allowed/Denied] Operation O From Host H on any Resource R matching ResourcePattern RP
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建topic
//        NewTopic newTopic = new NewTopic("adminClient1", 3, (short) 1);
//        CreateTopicsResult ret = adminClient.createTopics(Collections.singletonList(newTopic));
//        ret.all().get();

       //授予只读权限
        AclBinding group = new AclBinding(new ResourcePattern(ResourceType.GROUP, "test-consumer-group", PatternType.LITERAL),
              new AccessControlEntry("User:WL4odQnH", "*", AclOperation.READ, AclPermissionType.ALLOW ) );
        AclBinding topic = new AclBinding(new ResourcePattern(ResourceType.TOPIC, "test", PatternType.LITERAL),
                new AccessControlEntry("User:WL4odQnH", "*", AclOperation.READ, AclPermissionType.ALLOW ) );
        CreateAclsResult createAclsResult = adminClient.createAcls(Stream.of(group,topic).collect(Collectors.toList()));
        createAclsResult.all().get();
    }
}
