package com.asiainfo.controller.elastic;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ElasticSearchUtilController {
    private Logger log = LoggerFactory.getLogger(ElasticSearchUtilController.class);
    @Autowired
    private TransportClient client;
    private static ClusterAdminClient clusterAdminClient;

    @GetMapping("/testRedis")
    public String saveIndex(String indexname, String type, String json) {
        try {
            clusterAdminClient = client.admin().cluster();
            ClusterHealthResponse healths = clusterAdminClient.prepareHealth().get();

            log.debug("clusterName" + healths.getClusterName());
            log.debug("nodes" + healths.getNumberOfNodes());
            Map map = new HashMap();
            map.put("name", "hexin");
            IndexResponse response = client.prepareIndex("test", "test").setSource(map).get();
            //  IndexResponse response = client.prepareIndex("test", "test").setSource("测试").get();
            //创建成功 反会的状态码是201
            if (response.status().getStatus() == 201) {
                System.out.println("创建成功");
                return response.getId();
            }
        } catch (Exception e) {
            System.out.println("不符合索引的数据存储，数据源为：" + json);
        }
        return null;
    }

    /****
    * 查询返回Map
     */
    public Map<String, Object> getInfoByIndex(String indexname, String type, String id) {
        GetResponse response = client.prepareGet(indexname, type, id).get();
        return response.getSource();
    }

    /***
     * 删除文档
     */
    public boolean deleteIndexById(String indexname, String type, String id) {
        DeleteResponse response = client.prepareDelete(indexname, type, id).get();
        if (response.status().getStatus() == 200) {
            return true;
        }
        return false;
    }

    /***
     * 跟新文档
     */
    public boolean UpdateIndexById(String indexname, String type, String id, String doc) throws Exception {
        UpdateRequest updateRequest = new UpdateRequest(indexname, type, id).doc(doc);
        UpdateResponse response = client.update(updateRequest).get();
        if (response.status().getStatus() == 200) {
            return true;
        }
        return false;
    }


}
