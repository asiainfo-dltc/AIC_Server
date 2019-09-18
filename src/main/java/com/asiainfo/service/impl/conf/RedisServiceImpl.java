package com.asiainfo.service.impl.conf;


import com.asiainfo.service.conf.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private JedisPool jedisPool;

    public redis.clients.jedis.Jedis getResource() {
        //获取Jedis连接池资源
    //    System.out.println("aaa"+jedisPool.getResource().getDB().toString());
        return jedisPool.getResource();
    }

    @SuppressWarnings("deprecation")
    public void returnResource(redis.clients.jedis.Jedis jedis) {
        if(jedis!=null) {
            //释放jedisPool资源
            jedisPool.returnResource(jedis);
        }
    }
    /**
     *实现数据写入的setInfo方法
     */
    public void setInfo(String key, Map<String, String> value) {
        redis.clients.jedis.Jedis jedis=null;
        try {
            //获取jedis实例连接
            jedis=getResource();
            //选择使用的数据库(0-15,如不指定默认为0）
            jedis.select(15);
            //使用hmset方法（key，value(hashmap<key,value>)）
            jedis.hmset(key, value);
        }catch (Exception e) {
            e.printStackTrace();// TODO: handle exception
        }finally {
            returnResource(jedis);
        }
    }
    /**
     *实现数据查询的get方法
     */
    public Map<String, String> get(String key){
        redis.clients.jedis.Jedis jedis=null;
        Map<String, String> result=null;
        try {
            jedis=getResource();
            jedis.select(15);
            //通过key返回其所有的subkey及value
            jedis.hgetAll(key);
            result=jedis.hgetAll(key);
        }catch (Exception e) {
            e.printStackTrace();// TODO: handle exception
        }finally {
            returnResource(jedis);
        }
        return result;
    }

}
