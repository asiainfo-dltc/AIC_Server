package com.asiainfo.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    private Logger log = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;

    /*@Value("${spring.redis.time-out}")
    private int timeout;*/

    @Value("${spring.redis.maxActive}")
    private int maxActive;

    @Value("${spring.redis.maxIdle}")
    private int maxIdle;

   /* @Value("${spring.redis.pool.min-idle}")
    private int minIdle;*/

    @Value("${spring.redis.maxWaitMillis}")
    private long maxWaitMillis;



    @Bean
    public JedisPool redisPoolFactory() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        System.out.println("1231231");
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, 0, password);

        log.debug("JedisPool注入成功！");
       log.debug("redis地址：" + host + ":"+port+"pwd"+password );
        return  jedisPool;
    }
}
