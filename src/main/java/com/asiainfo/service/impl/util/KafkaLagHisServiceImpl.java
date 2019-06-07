package com.asiainfo.service.impl.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dao.KafkaLagHisDao;
import com.asiainfo.model.KafkaLagHisEnity;
/*import com.asiainfo.service.conf.RedisService;*/
import com.asiainfo.service.conf.RedisService;
import com.asiainfo.service.util.KafkaLagHisService;
import com.asiainfo.utils.TaskCallable;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author: create by hexin
 * @department: dltc
 * @version: v1.0
 * @description: com.asiainfo.service.impl.util
 * @date:2019/4/1
 */
@Service("kafkaLagHisServiceImpl")
public class KafkaLagHisServiceImpl implements KafkaLagHisService{
    @Autowired
    private KafkaLagHisDao kafkaLagHisDao;

    @Override
    public void insertLagHis(KafkaLagHisEnity kafkaLagHisEnity) {
        kafkaLagHisDao.insertKafkaLagHis(kafkaLagHisEnity);
    }

    @Override
    public List<KafkaLagHisEnity> getLagHis(int pageSize, int start) {
        return kafkaLagHisDao.getKafkaLagHis(pageSize,start);
    }

    @Override
    public JSONArray getLag(JSONArray groupIds) throws ExecutionException, InterruptedException {

        JSONArray result =new JSONArray();

        ExecutorService exec = Executors.newCachedThreadPool();//工头
        ArrayList<Future<List<KafkaLagHisEnity>>> loadResults = new ArrayList<Future<List<KafkaLagHisEnity>>>();//

       for(int i=0;i<groupIds.size();i++){
            String group = groupIds.get(i).toString();  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            loadResults.add(exec.submit(new TaskCallable(group)));
        }

       if(getLoadPercent(loadResults))
        {
            Iterator<Future<List<KafkaLagHisEnity>>> it = loadResults.iterator();
            while (it.hasNext()) {
                Future<List<KafkaLagHisEnity>> next = it.next();
                JSONObject obj =new JSONObject();
                obj.put("groupId",next.get().get(0).getGroupId());
                obj.put("topic",next.get().get(0).getTopic() );
                obj.put("detail",next.get());
                result.add(obj);
            }
        }
        /*模拟数据*/
          /* List<KafkaLagHisEnity> detail=new ArrayList<KafkaLagHisEnity>();
            JSONObject obj =new JSONObject();
            KafkaLagHisEnity entity =new KafkaLagHisEnity();
            KafkaLagHisEnity entity1 =new KafkaLagHisEnity();
            entity.setCurrentOffset("111");
            entity.setPartition("0");
            entity.setTopic("topic001");
            entity.setGroupId("0411");
             entity1.setCurrentOffset("2222");
            entity1.setPartition("2");
            detail.add(entity);
            detail.add(entity1);
            obj.put("groupId","aaaaaa");
            obj.put("topic",detail.get(0).getTopic());
            obj.put("detail",detail);
            result.add(obj);*/

        return  result;
    }
    /*
    * 调整偏移量
    *
    * */
    @Override
    public void seekOffset(KafkaLagHisEnity kafkaLagHisEnity) {


        File path = null;
        String rootPath = "";
        System.out.println(kafkaLagHisEnity.getCurrentOffset());
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        rootPath = path.getAbsolutePath();
        //System.out.println("rootPath"+rootPath);

        Long offset =Long.parseLong(kafkaLagHisEnity.getCurrentOffset());

        String client_ssl_dir = rootPath+"\\client-ssl\\";
        Properties props = new Properties();
       // props.put("bootstrap.servers", "dn49.hadoop.unicom:6667,dn50.hadoop.unicom:6667,dn51.hadoop.unicom:6667,dn54.hadoop.unicom:6667,dn55.hadoop.unicom:6667,dn56.hadoop.unicom:6667");
        props.put("bootstrap.servers", "ZRR-PRODUCT-109:9062,ZRR-PRODUCT-110:9062,ZRR-PRODUCT-111:9062,ZRR-PRODUCT-112:9062,ZRR-PRODUCT-113:9062,ZRR-PRODUCT-114:9062,ZRR-PRODUCT-115:9062,ZRR-PRODUCT-116:9062,ZRR-PRODUCT-117:9062");
        //props.put("bootstrap.servers", "10.5.8.35:9092,10.5.8.36:9092,10.5.8.37:9092");
        props.put("group.id", kafkaLagHisEnity.getGroupId());
        props.put("enable.auto.commit", "true");
        props.put("max.partition.fetch.bytes", 51200);
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        //props.put("auto.offset.reset", "earliest");
        props.put("auto.offset.reset", "latest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        props.put("max.poll.records",1);
        props.put("ssl.key.password", "zrr@kafkacenter");
        props.put("ssl.keystore.password", "zrr@kafkacenter");
        props.put("ssl.truststore.location", client_ssl_dir+"kafkacenter_client.truststore.jks");
        props.put("ssl.keystore.location", client_ssl_dir+"kafkacenter_client.keystore.jks");
        props.put("ssl.key.location", client_ssl_dir+"kafkacenter_client.key");
        props.put("ssl.certificate.location", client_ssl_dir+"kafkacenter_client.pem");
        props.put("ssl.ca.location", client_ssl_dir+"ca-cert");
        props.put("sasl.mechanism", "PLAIN");
        props.put("security.protocol", "SASL_SSL");
        props.put("ssl.keystore.type", "JKS");
        System.setProperty("java.security.auth.login.config", client_ssl_dir+"kafka_cilent_jaas.conf");

        KafkaConsumer consumer = new KafkaConsumer(props);
      //  TopicPartition partition0 = new TopicPartition(topic, partition);
       // consumer.assign(Arrays.asList(partition0));

        TopicPartition partition0 = new TopicPartition(kafkaLagHisEnity.getTopic(), Integer.parseInt(kafkaLagHisEnity.getPartition()));

        consumer.assign(Arrays.asList(partition0));
        consumer.seek(partition0,offset);

       /* while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                for (ConsumerRecord<String, String> record : records) {

                    //System.out.printf("topic = %s,partition = %d, offset = %s, time = %s, value = %s\n",record.topic(),record.partition(),record.offset(),sdf.format(new Date(Long.valueOf(record.timestamp()))),record.value());
                    System.out.printf("topic = %s,partition = %d, offset = %s, timestep = %s, time = %s, value = %s\n", record.topic(), record.partition(), record.offset(), record.timestamp(), sdf.format(new Date(Long.valueOf(record.timestamp()))), record.value());
//                    if(Long.valueOf(record.timestamp())>1552579199000l)
                    //  consumer.commitSync();
                    System.exit(0);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/
        // 指定kafka topic的offset消费
       // consumer.seek(partition0,Integer.parseInt(kafkaLagHisEnity.getCurrentOffset()));
        consumer.commitSync();

    }

    public boolean  getLoadPercent(ArrayList<Future<List<KafkaLagHisEnity>>>  loadResults ) {
        Iterator<Future<List<KafkaLagHisEnity>>> it = loadResults.iterator();
        int size =loadResults.size();
        while (it.hasNext()&&size>0) {
            Future<List<KafkaLagHisEnity>> next = it.next();
           // System.out.println("isDone"+next.isDone());
            if (next.isDone()) {
                System.out.println("isDone"+next.isDone());
                size--;
               // it.remove();
                }
                else{
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
       // System.out.println("size"+loadResults.size());
        if(size>0)
        {
            getLoadPercent(loadResults);
        }
        return true;
    }


}
