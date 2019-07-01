
package com.asiainfo.config;
import com.asiainfo.service.conf.HBaseService;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author: create by hexin
 * @department: dltc
 * @version: v1.0
 * @description: com.asiainfo.config
 * @date:2019/4/29
 */

@Configuration
public class HBaseConfig {
    private Logger log = LoggerFactory.getLogger(HBaseConfig.class);
    @Value("${spring.hbase.ZOOKEEPER_QUORUM}")
    private String ZOOKEEPER_QUORUM;

    @Value("${spring.hbase.ZOOKEEPER_ZNODE_PARENT}")
    private String ZK_ZNODE_PARENT;

    @Bean
    public HBaseService getHbaseService(){
        System.setProperty("HADOOP_USER_NAME","ocdp");
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum",ZOOKEEPER_QUORUM );
        conf.set(HConstants.ZOOKEEPER_ZNODE_PARENT, ZK_ZNODE_PARENT);
        return new HBaseService(conf);
    }

    /*@Bean
    public Connection h_connection() {
        org.apache.hadoop.conf.Configuration config = HBaseConfiguration.create();
        config.set(HConstants.ZOOKEEPER_QUORUM, ZOOKEEPER_QUORUM);
        config.set(HConstants.ZOOKEEPER_ZNODE_PARENT, ZK_ZNODE_PARENT);
        Connection connection = null;
        try {
            connection = ConnectionFactory.createConnection(config);
            log.info("[创建Hbase连接] 连接成功");
        } catch (IOException e) {
            log.error("[创建Hbase连接] 出现异常了，{}",e);
        }
        return connection;
    }*/
}
