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
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
    public List<KafkaLagHisEnity> getLagHis(String start, String end) {
        start="2019-07-06";
        end ="2019-07-30";
        List<KafkaLagHisEnity> result=kafkaLagHisDao.getKafkaLagHis(start,end);
        return result ;
    }

    @Override
    public List<String> getTopicsHis() {
        return  kafkaLagHisDao.getKafkaTopicsHis();
    }

    @Override
    public JSONArray getLag(JSONArray groupIds) throws ExecutionException, InterruptedException {

        JSONArray result =new JSONArray();

        ExecutorService exec = Executors.newCachedThreadPool();//工头
        ArrayList<Future<List<KafkaLagHisEnity>>> loadResults = new ArrayList<Future<List<KafkaLagHisEnity>>>();//

       for(int i=0;i<groupIds.size();i++){
            String group = groupIds.get(i).toString();  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            System.out.println("消费组"+group);
            loadResults.add(exec.submit(new TaskCallable(group)));
        }

       if(getLoadPercent(loadResults))
        {
            Iterator<Future<List<KafkaLagHisEnity>>> it = loadResults.iterator();
            while (it.hasNext()) {
                Future<List<KafkaLagHisEnity>> next = it.next();
                JSONObject obj =new JSONObject();
                if(next.get().size()>0) {
                    obj.put("groupId", next.get().get(0).getGroupId());
                    obj.put("topic", next.get().get(0).getTopic());
                    obj.put("detail", next.get());
                    result.add(obj);
                }

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


        String path = null;
        System.out.println(kafkaLagHisEnity.getCurrentOffset());
       // path = KafkaLagHisServiceImpl.class.getClassLoader().getResource("client-ssl").getPath();
        path="client-ssl";
        Long offset =Long.parseLong(kafkaLagHisEnity.getCurrentOffset());
        ResourceLoader resourceLoader = new DefaultResourceLoader();
  //      unJar();

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
/*        props.put("ssl.truststore.location",truststore);
        props.put("ssl.keystore.location", keystore);
        props.put("ssl.key.location",key);
        props.put("ssl.certificate.location",certificate);
        props.put("ssl.ca.location", ca);*/

        props.put("ssl.truststore.location", path+"/kafkacenter_client.truststore.jks");
        props.put("ssl.keystore.location", path+"/kafkacenter_client.keystore.jks");
        props.put("ssl.key.location", path+"/kafkacenter_client.key");
        props.put("ssl.certificate.location", path+"/kafkacenter_client.pem");
        props.put("ssl.ca.location", path+"/ca-cert");
        props.put("sasl.mechanism", "PLAIN");
        props.put("security.protocol", "SASL_SSL");
        props.put("ssl.keystore.type", "JKS");
        props.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"new_sale\" password=\"ns@hrb\";");
        System.setProperty("java.security.auth.login.config", path+"/kafka_cilent_jaas.conf");

        KafkaConsumer consumer = new KafkaConsumer(props);
        //  TopicPartition partition0 = new TopicPartition(topic, partition);
        // consumer.assign(Arrays.asList(partition0));

        TopicPartition partition0 = new TopicPartition(kafkaLagHisEnity.getTopic(), Integer.parseInt(kafkaLagHisEnity.getPartition()));

        consumer.assign(Arrays.asList(partition0));
        consumer.seek(partition0,offset);
        consumer.commitSync();
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
    private static void unJar() {
        if (!new File("client-ssl").exists()) {
            try {
                String path = KafkaLagHisServiceImpl.class.getProtectionDomain().getCodeSource().getLocation().getFile();
                ZipInputStream Zin = new ZipInputStream(new FileInputStream(path));
                BufferedInputStream Bin = new BufferedInputStream(Zin);

                File fout = null;
                ZipEntry entry;
                try {
                    while ((entry = Zin.getNextEntry()) != null) {
                        if (!entry.getName().startsWith("client-ssl")) {
                            continue;
                        }
                        fout = new File(entry.getName());

                        if (entry.isDirectory()) {
                            fout.mkdirs();
                            continue;
                        }
                        FileOutputStream out = new FileOutputStream(fout);
                        BufferedOutputStream Bout = new BufferedOutputStream(out);
                        int b;
                        while ((b = Bin.read()) != -1) {
                            Bout.write(b);
                        }
                        Bout.close();
                        out.close();
                        System.out.println(fout + "SSL配置文件释放成功");
                    }
                    Bin.close();
                    Zin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
