package com.asiainfo.controller.hbase;

import com.asiainfo.model.KafkaLagHisEnity;
import com.asiainfo.model.sys.PageResult;
import com.asiainfo.service.conf.HBaseService;
import com.asiainfo.service.util.KafkaLagHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class HbaseUtilController {

    @Autowired
    private  HBaseService hbaseService;



    @GetMapping("/getRelationData")
    public List<Map<String,String>> getRelationData(String rowKey) {

        System.out.println("rowKey"+rowKey);
        System.out.println("tables"+hbaseService.getAllTableNames());
        List<Map<String,String>> result=hbaseService.getRowData("ORDER_RELATION_TABLE",rowKey);
       // System.out.println("tables"+hbaseService.getRowData("ORDER_RELATION_TABLE",rowKey));
        return result;
    }
    public static void main(String[] args){

    }
}
