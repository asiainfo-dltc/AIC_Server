package com.asiainfo.controller.kafka;

import com.asiainfo.controller.sys.MenuController;
import com.asiainfo.model.KafkaLagHisEnity;
import com.asiainfo.model.sys.MenuEntity;
import com.asiainfo.model.sys.PageResult;
import com.asiainfo.service.sys.MenuService;
import com.asiainfo.service.util.KafkaLagHisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: create by hexin
 * @department: dltc
 * @version: v1.0
 * @description: com.asiainfo.controller.kafka
 * @date:2019/4/17
 */
@RestController
public class KafkaUtilController {
    private Logger log = LoggerFactory.getLogger(KafkaUtilController.class);

    @Resource(name = "kafkaLagHisServiceImpl")
    private KafkaLagHisService kafkaLagHisService;


    @GetMapping("/getLags")
    public PageResult menusList(int pageSize, int page, String menuId) {
        PageResult pageResult = new PageResult();

        List<KafkaLagHisEnity> lags=new ArrayList<>();
        KafkaLagHisEnity kafkaLagHisEnity =new KafkaLagHisEnity();
     /*   kafkaLagHisEnity.setOperationTime("20190417");
        kafkaLagHisEnity.setLag("10000");
        kafkaLagHisEnity.setTopic("topic");
        kafkaLagHisEnity.setGroupId("group");
        lags.add(kafkaLagHisEnity);*/
        pageResult.setData(kafkaLagHisService.getLagHis(10,0));
        pageResult.setTotalCount(10);
        log.debug("The method is ending");
        return pageResult;
    }

}
