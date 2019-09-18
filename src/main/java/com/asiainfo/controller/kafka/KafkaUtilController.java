package com.asiainfo.controller.kafka;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.model.KafkaLagHisEnity;
import com.asiainfo.model.sys.PageResult;
import com.asiainfo.service.util.KafkaLagHisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

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


    @GetMapping("/getLagsHis")
    public JSONObject getLagsHis(String start,String end) {
        PageResult pageResult = new PageResult();
        JSONObject jsonObject=new JSONObject();
        List<KafkaLagHisEnity> lags=new ArrayList<>();
        KafkaLagHisEnity kafkaLagHisEnity =new KafkaLagHisEnity();
     /*   kafkaLagHisEnity.setOperationTime("20190417");
        kafkaLagHisEnity.setLag("10000");
        kafkaLagHisEnity.setTopic("topic");
        kafkaLagHisEnity.setGroupId("group");
        lags.add(kafkaLagHisEnity);*/
        List<KafkaLagHisEnity> result= kafkaLagHisService.getLagHis(start,end);
        List<String> topics= kafkaLagHisService.getTopicsHis();

        String[] xAxis={"2019-07-06","2019-07-07","2019-07-08"};
        jsonObject.put("legend",topics);
        jsonObject.put("xAxis",xAxis);
        ArrayList<Map> series = new ArrayList<Map>();
        for(int i=0;i<topics.size();i++){
            HashMap map = new HashMap();
            map.put("name",topics.get(i) );
            map.put("type","line" );
            map.put("stack","总量" );
            List<String> logEndOffset= new ArrayList<>();
            for(int k=0;k<result.size();k++) {

                if(topics.get(i).equals(result.get(k).getTopic())) {
                logEndOffset.add(result.get(k).getLogEndOffset());
                }
             }
            map.put("data",logEndOffset);
            series.add(map);
        }
        jsonObject.put("series",series);
        return jsonObject;
    }
    @GetMapping("/getLags")
    public JSONArray getLags(String groupIds) throws ExecutionException, InterruptedException {
        JSONArray arr=JSONObject.parseArray(groupIds);
        //List<KafkaLagHisEnity> lags=new ArrayList<>();
       // KafkaLagHisEnity kafkaLagHisEnity =new KafkaLagHisEnity();
        JSONArray result= kafkaLagHisService.getLag(arr);
        return result;
    }

 /*   @GetMapping("/getLogEndOffset")
    public JSONArray getLogEndOffset() throws ExecutionException, InterruptedException {
      //  JSONArray arr=JSONObject.parseArray(groupIds);
        //List<KafkaLagHisEnity> lags=new ArrayList<>();
        // KafkaLagHisEnity kafkaLagHisEnity =new KafkaLagHisEnity();
        JSONArray result= kafkaLagHisService.getLogEndOffset();
        return result;
    }*/
    @PostMapping("/seekOffset")
    public void seekOffset(@RequestBody KafkaLagHisEnity kafkaLagHisEnity) throws ExecutionException, InterruptedException {

        kafkaLagHisService.seekOffset(kafkaLagHisEnity);

      /*  JSONArray arr=JSONObject.parseArray(groupIds);
        List<KafkaLagHisEnity> lags=new ArrayList<>();
        KafkaLagHisEnity kafkaLagHisEnity =new KafkaLagHisEnity();
        JSONArray result= kafkaLagHisService.getLag(arr);
        return result;*/

    }
}
