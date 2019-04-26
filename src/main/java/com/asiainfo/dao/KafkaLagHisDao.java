package com.asiainfo.dao;

import com.asiainfo.model.KafkaLagHisEnity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: create by hexin
 * @department: dltc
 * @version: v1.0
 * @description: com.asiainfo.dao
 * @date:2019/4/1
 */
@Mapper
public interface KafkaLagHisDao {
    public void insertKafkaLagHis(@Param("kafkaLagHisEnity") KafkaLagHisEnity kafkaLagHisEnity);
}
