
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
    /*
    * 联调
    * */
    @Value("${spring.hbase-int.ZOOKEEPER_QUORUM}")
    private String ZOOKEEPER_QUORUM_INT;
    @Value("${spring.hbase-int.ZOOKEEPER_ZNODE_PARENT}")
    private String ZK_ZNODE_PARENT_INT;

    /*灰度
    * */
    @Value("${spring.hbase-AB.ZOOKEEPER_QUORUM}")
    private String ZOOKEEPER_QUORUM_AB;
    @Value("${spring.hbase-AB.ZOOKEEPER_ZNODE_PARENT}")
    private String ZK_ZNODE_PARENT_AB;

    /*生产*/
    @Value("${spring.hbase-prd.ZOOKEEPER_QUORUM}")
    private String ZOOKEEPER_QUORUM_PRD;
    @Value("${spring.hbase-prd.ZOOKEEPER_ZNODE_PARENT}")
    private String ZK_ZNODE_PARENT_PRD;
    @Bean
    public HBaseService getHbaseService(){
        System.setProperty("HADOOP_USER_NAME","ocdp");
        /*联调*/
        org.apache.hadoop.conf.Configuration conf_int = HBaseConfiguration.create();
        conf_int.set("hbase.zookeeper.quorum",ZOOKEEPER_QUORUM_INT );
        conf_int.set(HConstants.ZOOKEEPER_ZNODE_PARENT, ZK_ZNODE_PARENT_INT);
        /*灰度
         * */
        org.apache.hadoop.conf.Configuration conf_AB = HBaseConfiguration.create();
        conf_AB.set("hbase.zookeeper.quorum",ZOOKEEPER_QUORUM_AB );
        conf_AB.set(HConstants.ZOOKEEPER_ZNODE_PARENT, ZK_ZNODE_PARENT_AB);
        /*生产*/
        org.apache.hadoop.conf.Configuration conf_prd = HBaseConfiguration.create();
        conf_prd.set("hbase.zookeeper.quorum",ZOOKEEPER_QUORUM_PRD );
        conf_prd.set(HConstants.ZOOKEEPER_ZNODE_PARENT, ZK_ZNODE_PARENT_PRD);



        return new HBaseService(conf_int,conf_AB,conf_prd);
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
