package com.asiainfo.service.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.model.KafkaLagHisEnity;
import com.asiainfo.model.kafka.KafkaConfigEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author: create by hexin
 * @department: dltc
 * @version: v1.0
 * @description: com.asiainfo.service.util
 * @date:2019/4/1
 */
public interface KafkaLagHisService {
    public void insertLagHis(KafkaLagHisEnity kafkaLagHisEnity);
    public List<KafkaLagHisEnity> getLagHis(String start, String end);
    public List<String> getTopicsHis();
    public JSONArray getLag(JSONArray groupIds) throws ExecutionException, InterruptedException;
    public  void seekOffset(KafkaLagHisEnity kafkaLagHisEnity);
    public  List<KafkaConfigEntity> getTopicAndGroup(KafkaConfigEntity kafkaConfigEntity);

}
