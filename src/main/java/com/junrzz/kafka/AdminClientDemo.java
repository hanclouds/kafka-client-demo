package com.junrzz.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.server.quota.ClientQuotaCallback;

import javax.security.auth.login.Configuration;
import java.util.*;
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
   //     Configuration.setConfiguration(new SaslConfig("admin","admin"));
        adminClient = AdminClient.create(properties);
    }
    /**
     * Principal P is [Allowed/Denied] Operation O From Host H on any Resource R matching ResourcePattern RP
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建topic
//        NewTopic newTopic = new NewTopic("adminClientTest", 3, (short) 1);
//        CreateTopicsResult ret = adminClient.createTopics(Collections.singletonList(newTopic));
//        ret.all().get();
       //授予只读权限
//        AclBinding group = new AclBinding(new ResourcePattern(ResourceType.GROUP, "test-consumer-group", PatternType.LITERAL),
//              new AccessControlEntry("User:vh6luFPT", "*", AclOperation.READ, AclPermissionType.ALLOW ) );
//        AclBinding topic = new AclBinding(new ResourcePattern(ResourceType.TOPIC, "test", PatternType.LITERAL),
//                new AccessControlEntry("User:vh6luFPT", "*", AclOperation.READ, AclPermissionType.ALLOW ) );
//        CreateAclsResult createAclsResult = adminClient.createAcls(Stream.of(group,topic).collect(Collectors.toList()));
//        createAclsResult.all().get();
//        DescribeClusterResult describeClusterResult = adminClient.describeCluster();
//        List<Node> nodes = new ArrayList<>(describeClusterResult.nodes().get());
//        ConfigResource configResource = new ConfigResource(ConfigResource.Type.BROKER, String.valueOf(nodes.get(0).id()));
//        DescribeConfigsResult result = adminClient.describeConfigs(Collections.singletonList(configResource) );
//        System.out.println(result.all().get());
//        Map<ConfigResource,Config> config = new HashMap<>();
//        String addConfig = "producer_byte_rate=1024,consumer_byte_rate=1024,request_percentage=200";
//        ConfigEntry entry1 = new ConfigEntry("--add-config", addConfig);
//        ConfigEntry entry2 = new ConfigEntry("--entity-type", "users");
//        ConfigEntry entry3 = new ConfigEntry("--entity-name", "liujunjie");
//        ConfigEntry entry4 = new ConfigEntry("--zookeeper", "localhost:2182");
//        Config config1 = new Config( Arrays.asList(entry1,entry2,entry3,entry4));
//        config.put(configResource, config1);
//       AlterConfigsResult result1 = adminClient.alterConfigs(config);
//       System.out.println(result1.all().get());


    }
}


