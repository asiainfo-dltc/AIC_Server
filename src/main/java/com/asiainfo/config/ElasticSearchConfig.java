package com.asiainfo.config;


import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Configuration
public class ElasticSearchConfig {
    private Logger log = LoggerFactory.getLogger(ElasticSearchConfig.class);
    @Value("${spring.es.hostName}")
    private String hostName;

    @Value("${spring.es.transport}")
    private int transport;

    @Value("${spring.es.clusterName}")
    private String clusterName;
    private static Settings settings;
    private static TransportClient transportClient;
    private static AdminClient adminClient;
    private static ClusterAdminClient clusterAdminClient;

    @Bean
    public TransportClient transportClient() {
        //logger.info("ElasticSearch初始化开始");

        TransportClient transportClient = null;


            Settings settings = Settings.builder().put("cluster.name", clusterName).build();
            TransportClient client = new PreBuiltTransportClient(settings);
            String[] hosts = hostName.split(",");
            for (String host : hosts) {
                // 如果我们配置多个host，自动识别配置多个节点
                try {
                    client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), transport));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            return client;


    }
}