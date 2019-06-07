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
import java.util.ArrayList;
import java.util.List;
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
    @GetMapping("/getLags")
    public JSONArray getLags(String groupIds) throws ExecutionException, InterruptedException {
        JSONArray arr=JSONObject.parseArray(groupIds);
        //List<KafkaLagHisEnity> lags=new ArrayList<>();
       // KafkaLagHisEnity kafkaLagHisEnity =new KafkaLagHisEnity();
        JSONArray result= kafkaLagHisService.getLag(arr);
        return result;
    }
    @PostMapping("/seekOffset")
    public void seekOffset(@RequestBody KafkaLagHisEnity kafkaLagHisEnity) throws ExecutionException, InterruptedException {

        kafkaLagHisService.seekOffset(kafkaLagHisEnity);

      /*  JSONArray arr=JSONObject.parseArray(groupIds);
        List<KafkaLagHisEnity> lags=new ArrayList<>();
        KafkaLagHisEnity kafkaLagHisEnity =new KafkaLagHisEnity();
        JSONArray result= kafkaLagHisService.getLag(arr);
        return result;*/

    }




    public void main(){

        JSONArray arr=new JSONArray();
        arr.add(new String[]{"1"});
        arr.add(new String[]{"2"});
        try {
            kafkaLagHisService.getLag(arr);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
