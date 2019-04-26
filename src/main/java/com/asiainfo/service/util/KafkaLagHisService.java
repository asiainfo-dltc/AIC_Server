package com.asiainfo.service.util;


import com.asiainfo.model.KafkaLagHisEnity;

/**
 * @author: create by hexin
 * @department: dltc
 * @version: v1.0
 * @description: com.asiainfo.service.util
 * @date:2019/4/1
 */
public interface KafkaLagHisService {
    void insertLagHis(KafkaLagHisEnity kafkaLagHisEnity);
}
