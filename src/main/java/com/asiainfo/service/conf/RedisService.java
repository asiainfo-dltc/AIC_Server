package com.asiainfo.service.conf;

import redis.clients.jedis.Jedis;

import java.util.Map;

public interface RedisService {
    public redis.clients.jedis.Jedis getResource();
    public void returnResource(Jedis jedis);
    public void setInfo(String key, Map<String, String> value);
    public Map<String, String> get(String key);

}
