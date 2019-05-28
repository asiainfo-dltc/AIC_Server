package com.asiainfo.utils;

import com.asiainfo.model.KafkaLagHisEnity;
import com.asiainfo.utils.kafka.ReadShellLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class TaskCallable implements Callable<List<KafkaLagHisEnity>> {


   /* @Autowired
    private JedisPool jedisPool;*/


    private String id;
    public TaskCallable(String id){
        this.id = id;
    }
    @Override
    public List<KafkaLagHisEnity> call() throws Exception {
        ReadShellLine readShellLine=new ReadShellLine();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

     /*   JedisPool jedisPool = new JedisPool(jedisPoolConfig, "10.245.32.69", 6379, 1000, "r-fk4979cce008efe4:PROsjgj345");
        List<KafkaLagHisEnity> list=readShellLine.excuteCommandQuery(id);
        Jedis jedis= jedisPool.getResource();
        jedis.set(id,list.toString());*/


        //return "result of taskWithResult "+id;
        System.out.println("id"+id);
        /*List<KafkaLagHisEnity> detail=new ArrayList<KafkaLagHisEnity>();
        KafkaLagHisEnity entity =new KafkaLagHisEnity();
        KafkaLagHisEnity entity1 =new KafkaLagHisEnity();
        entity.setCurrentOffset("111");
        entity.setPartition("0");
        entity1.setCurrentOffset("2222");
        entity1.setPartition("2");
        detail.add(entity);
        detail.add(entity1);*/
        //Thread.sleep(50000);
       // RedisConnection conn= connectionFactory.getConnection();

        //conn.openPipeline();

       // conn.stringCommands().set(serializationStrategy.serialize(id), serializationStrategy.serialize(readShellLine.excuteCommandQuery(id)));
       return readShellLine.excuteCommandQuery(id);
     //   String aa=serializationStrategy.deserialize(conn.get(serializationStrategy.serialize(id)));
       // return detail;
    }
}
