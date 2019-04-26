package com.asiainfo.service.util;


import com.asiainfo.model.KafkaLagHisEnity;

import java.util.List;

/**
 * @author: create by hexin
 * @department: dltc
 * @version: v1.0
 * @description: com.asiainfo.service.util
 * @date:2019/4/1
 */
public interface KafkaLagHisService {
    void insertLagHis(KafkaLagHisEnity kafkaLagHisEnity);
    public List<KafkaLagHisEnity> getLagHis(int pageSize, int start);
}
