package com.asiainfo.dao;

import com.asiainfo.model.KafkaLagHisEnity;
import com.asiainfo.model.sys.MenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    public List<KafkaLagHisEnity> getKafkaLagHis(@Param("pageSize") int pageSize, @Param("start") int start);
}
