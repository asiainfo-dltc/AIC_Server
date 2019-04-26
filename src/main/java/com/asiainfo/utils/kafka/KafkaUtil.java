/*
package com.asiainfo.utils.kafka;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import kafka.common.OffsetAndMetadata;
import kafka.coordinator.group.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

*/
/**
 * @author: create by hexin
 * @department: dltc
 * @version: v1.0
 * @description: com.asiainfo.utils.kafka
 * @date:2019/3/31
 *//*

public class KafkaUtil {

    private static final int ZOOKEEPER_TIMEOUT = 30000;
    private final CountDownLatch latch = new CountDownLatch(1);

    public ZooKeeper getZookeeper(String connectionString) {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(connectionString, ZOOKEEPER_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (Event.KeeperState.SyncConnected.equals(event.getState())) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zk;
    }

    public static Properties getConsumerProperties(String groupId, String bootstrap_servers) {
        Properties props = new Properties();
        props.put("group.id", groupId);
        props.put("bootstrap.servers",bootstrap_servers);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return props;
    }

    */
/**
     * 获取logSize, offset, lag等信息
     * @param zk
     * @param bootstrap_servers
     * @param groupId
     * @param topics null查询groupId消费过的所有topic
     * @param sorted
     * @return
     * @throws Exception
     *//*

    public List<Map<String, Object>> getLagByGroupAndTopic(ZooKeeper zk, String bootstrap_servers, String groupId,
                                                           String[] topics, boolean sorted) throws Exception {

        List<Map<String, Object>> topicPatitionMapList = new ArrayList<>();

        // 获取group消费过的所有topic
        List<String> topicList = null;
        topicList=Arrays.asList("Test.SX19BSS.IOM");
    */
/*    if (topics == null || topics.length == 0) {
            try {
                topicList = zk.getChildren("/consumers/" + groupId , false);
            } catch (KeeperException | InterruptedException e) {
                logger.error("从zookeeper获取topics失败：zkState: {}, groupId:{}", zk.getState(), groupId);
                throw new Exception("从zookeeper中获取topics失败");
            }
        } else {
            topicList = Arrays.asList(topics);
        }*//*


        Properties consumeProps = getConsumerProperties(groupId, bootstrap_servers);
        //logger.info("consumer properties:{}", consumeProps);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumeProps);

        // 查询topic partitions
        for (String topic : topicList) {
            List<PartitionInfo> partitionsFor = consumer.partitionsFor(topic);
            //由于有时延， 尽量逐个topic查询， 减少lag为负数的情况
            List<TopicPartition> topicPartitions = new ArrayList<>();

            // 获取topic对应的 TopicPartition
            for (PartitionInfo partitionInfo : partitionsFor) {
                TopicPartition topicPartition = new TopicPartition(partitionInfo.topic(), partitionInfo.partition());
                topicPartitions.add(topicPartition);
            }
            // 查询logSize
            Map<TopicPartition, Long> endOffsets = consumer.endOffsets(topicPartitions);
            for (Entry<TopicPartition, Long> entry : endOffsets.entrySet()) {
                TopicPartition partitionInfo = entry.getKey();
                // 获取offset
                String offsetPath = MessageFormat.format("/consumers/{0}/offsets/{1}/{2}", groupId, partitionInfo.topic(),
                        partitionInfo.partition());
                byte[] data = zk.getData(offsetPath, false, null);
                long offset = Long.valueOf(new String(data));

                Map<String, Object> topicPatitionMap = new HashMap<>();
                topicPatitionMap.put("group", groupId);
                topicPatitionMap.put("topic", partitionInfo.topic());
                topicPatitionMap.put("partition", partitionInfo.partition());
                topicPatitionMap.put("logSize", endOffsets.get(partitionInfo));
                topicPatitionMap.put("offset", offset);
                topicPatitionMap.put("lag", endOffsets.get(partitionInfo) - offset);
                topicPatitionMapList.add(topicPatitionMap);
            }
        }
        consumer.close();

        if(sorted) {
            Collections.sort(topicPatitionMapList, new Comparator<Map<String,Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    if(o1.get("topic").equals(o2.get("topic"))) {
                        return ((Integer)o1.get("partition")).compareTo((Integer)o2.get("partition"));
                    }
                    return ((String)o1.get("topic")).compareTo((String)o2.get("topic"));
                }
            });
        }

        return topicPatitionMapList;
    }

    */
/*public static void main(String[] args) throws Exception {
        String bootstrap_servers = "10.124.202.92:6667,10.124.202.93:6667,10.124.202.94:6667";
        String groupId = "T0000_SHANXI.IOM.ROUTER.ACCESS_20190325";
        String[] topics = {"Test.SX19BSS.IOM"};//{"test1", "test2", test3};

        KafkaUtil kafkaUtil = new KafkaUtil();
        String connectionString = "10.124.202.79:2181,10.124.202.73:2181,10.124.202.76:2181";
        ZooKeeper zk = kafkaUtil.getZookeeper(connectionString);
        if (zk == null) {
            throw new RuntimeException("获取zookeeper连接失败");
        }
        List<Map<String, Object>> topicPatitionMapList = kafkaUtil.getLagByGroupAndTopic(zk, bootstrap_servers,
                groupId, topics, true);

        for (Map<String, Object> map : topicPatitionMapList) {
            System.out.println(map);
        }
        zk.close();
    }*//*


    public static void main(String[] args) {
        Properties props = new Properties();
//        props.put("bootstrap.servers", "CBHKZY-4T-30:6667,CBHKZY-4T-31:6667,CBHKZY-4T-32:6667");
//        props.put("bootstrap.servers", "CBHKZY-4T-30:6667,CBHKZY-4T-32:6667,CBHKZY-4T-32:6667");
//        props.put("bootstrap.servers", "10.5.8.35:9092,10.5.8.36:9092,10.5.8.37:9092");

        //props.put("bootstrap.servers", "dn31.hadoop.unicom:6667,dn33.hadoop.unicom:6667,dn32.hadoop.unicom:6667");
        //自然人测试环境
        //   props.put("bootstrap.servers", "10.124.128.30:6667,10.124.128.31:6667,10.124.128.32:6667");
        //测试环境内部kafka
           props.put("bootstrap.servers", "10.124.202.92:6667,10.124.202.93:6667,10.124.202.94:6667");


        // props.put("bootstrap.servers", "dn49.hadoop.unicom:6667,dn50.hadoop.unicom:6667,dn51.hadoop.unicom:6667,dn54.hadoop.unicom:6667,dn55.hadoop.unicom:6667,dn56.hadoop.unicom:6667");
        //自然人生产环境
        //props.put("bootstrap.servers", "ZRR-PRODUCT-109:9062,ZRR-PRODUCT-110:9062,ZRR-PRODUCT-111:9062,ZRR-PRODUCT-112:9062,ZRR-PRODUCT-113:9062,ZRR-PRODUCT-114:9062,ZRR-PRODUCT-115:9062,ZRR-PRODUCT-116:9062,ZRR-PRODUCT-117:9062");

        //props.put("group.id", "T0000_SHANXI.MAIN.ROUTER.ACCESS_20190225");
        props.put("group.id", "T0000_SHANXI.IOM.ROUTER.ACCESS_20190325");
        //T0000_joinMergeQueueOrder_1
        props.put("enable.auto.commit", "false");
        props.put("max.partition.fetch.bytes", "51200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //  props.put("max.poll.records", "1");
       */
/* props.put("sasl.jaas.config",
                "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"new_sale\" password=\"ns@hrb\";");
        props.put("ssl.key.password", "zrr@kafkacenter");
        props.put("ssl.keystore.password", "zrr@kafkacenter");
        props.put("ssl.truststore.location", "C:\\Users\\hexin\\OC_CODE\\dacp-ordercollect-streams\\src\\main\\resources\\client-ssl\\kafkacenter_client.truststore.jks");
        props.put("ssl.keystore.location", "C:\\Users\\hexin\\OC_CODE\\dacp-ordercollect-streams\\src\\main\\resources\\client-ssl\\kafkacenter_client.keystore.jks");
        props.put("ssl.key.location", "C:\\Users\\hexin\\OC_CODE\\dacp-ordercollect-streams\\src\\main\\resources\\client-ssl\\kafkacenter_client.key");
        props.put("ssl.certificate.location", "C:\\Users\\hexin\\OC_CODE\\dacp-ordercollect-streams\\src\\main\\resources\\client-ssl\\kafkacenter_client.pem");
        props.put("ssl.ca.location", "C:\\Users\\hexin\\OC_CODE\\dacp-ordercollect-streams\\src\\main\\resources\\client-ssl\\ca-cert");
        props.put("sasl.mechanism", "PLAIN");
        props.put("security.protocol", "SASL_SSL");
        props.put("ssl.keystore.type", "JKS");
        System.setProperty("java.security.auth.login.config", "C:\\Users\\hexin\\OC_CODE\\dacp-ordercollect-streams\\src\\main\\resources\\client-ssl\\kafka_cilent_jaas.conf");*//*

        try {
            new KafkaUtil().getAllGroupsForTopic( props);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 public void getAllGroupsForTopic(Properties props){


      KafkaConsumer consumer = new KafkaConsumer(props);

     consumer.listTopics().toString();
     List<String> topicArray = new ArrayList();
     String topic="Test.SX19BSS.IOM";
     topicArray.add(topic);
     consumer.subscribe(topicArray);
    // System.out.println("topics"+consumer.listTopics().values());
    // consumer.subscribe();
     ConsumerRecords<String, byte[]> records = consumer.poll(1000);
     int partitions= consumer.partitionsFor(topic).size();
     System.out.println("partitions："+partitions);

     for(int i=0;i< consumer.partitionsFor(topic).size();i++)
     {

         TopicPartition partition0 = new TopicPartition(topic,i);
         //KafkaConsumer consumer1 = new KafkaConsumer(props);
         KafkaConsumer<byte[], byte[]> consumer1 = new KafkaConsumer<>(props);

         consumer1.assign(Arrays.asList(partition0));
         while (true) {

             // consumer.assign(partitions);
             ConsumerRecords<byte[],byte[] > records1 = consumer1.poll(1000);
             if(records.count()>0)
             {
                 Map<TopicPartition, Long> map11 = consumer1.endOffsets(Arrays.asList(partition0));

                 for (ConsumerRecord<byte[], byte[]> record1 : records1) {
                     System.out.println("#####"+record1.key());
                     byte[] bytes= record1.toString().getBytes();
                     BaseKey key = GroupMetadataManager.readMessageKey(ByteBuffer.wrap(bytes));
                     if (key instanceof OffsetKey) {
                         GroupTopicPartition partition = (GroupTopicPartition) key.key();

                         System.out.println("group : " + partition.group() + "  topic : " + partition.topicPartition().topic());
                         System.out.println(key.toString());
                     } else if (key instanceof GroupMetadataKey) {
                         System.out.println("groupMetadataKey:------------ " + key.key());
                     }
                     byte[] value = record1.value();
                     if (value == null) {
                         continue;
                     }
                     OffsetAndMetadata offset = GroupMetadataManager.readOffsetMessageValue(ByteBuffer.wrap(value));
                     if (key instanceof OffsetKey) {
                         OffsetKey newKey = (OffsetKey) key;
                         String group = newKey.key().group();
                         TopicPartition tp = newKey.key().topicPartition();
                         System.out.println(group + "," + tp.topic() + "," + tp.partition() + "," + offset.offsetMetadata().offset());
                     }
                 }
                 for (Entry<TopicPartition, Long> entry : map11.entrySet()) {
                     Long offset=entry.getValue();
                     if(entry.getKey().partition()==i)
                     {

                         System.out.println("currentOffSet:"+offset);
                         System.out.println("endOffSet:"+offset);
                         System.out.println("lag:"+offset);

                     }


                     consumer.commitAsync();
                 }
                 break;
             }

         }
     }

 }



}
*/
