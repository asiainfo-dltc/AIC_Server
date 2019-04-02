package com.asiainfo.service.impl.util;

import com.asiainfo.dao.KafkaLagHisDao;
import com.asiainfo.model.KafkaLagHisEnity;
import com.asiainfo.service.util.KafkaLagHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: create by hexin
 * @department: dltc
 * @version: v1.0
 * @description: com.asiainfo.service.impl.util
 * @date:2019/4/1
 */
@Service("kafkaLagHisServiceImpl")
public class KafkaLagHisServiceImpl implements KafkaLagHisService{
    @Autowired
    private KafkaLagHisDao kafkaLagHisDao;
    @Override
    public void insertLagHis(KafkaLagHisEnity kafkaLagHisEnity) {
        kafkaLagHisDao.insertKafkaLagHis(kafkaLagHisEnity);
    }
}
