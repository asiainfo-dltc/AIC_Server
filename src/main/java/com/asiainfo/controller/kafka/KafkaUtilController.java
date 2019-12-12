package com.asiainfo.controller.kafka;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.model.KafkaLagHisEnity;
import com.asiainfo.model.kafka.KafkaConfigEntity;
import com.asiainfo.model.sys.PageResult;
import com.asiainfo.service.util.CustomMultiThreadingService;
import com.asiainfo.service.util.KafkaLagHisService;
import com.asiainfo.utils.kafka.KafkaOffsetTools;
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
import java.util.concurrent.Future;

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
    @Resource(name = "customMultiThreadingServiceImpl")
    private CustomMultiThreadingService customMultiThreadingService;


    @GetMapping("/getLagsHis")
    public JSONObject getLagsHis(String start, String end) {
        PageResult pageResult = new PageResult();
        JSONObject jsonObject = new JSONObject();
        List<KafkaLagHisEnity> lags = new ArrayList<>();
        KafkaLagHisEnity kafkaLagHisEnity = new KafkaLagHisEnity();
     /*   kafkaLagHisEnity.setOperationTime("20190417");
        kafkaLagHisEnity.setLag("10000");
        kafkaLagHisEnity.setTopic("topic");
        kafkaLagHisEnity.setGroupId("group");
        lags.add(kafkaLagHisEnity);*/
        List<KafkaLagHisEnity> result = kafkaLagHisService.getLagHis(start, end);
        List<String> topics = kafkaLagHisService.getTopicsHis();

        String[] xAxis = {"2019-07-06", "2019-07-07", "2019-07-08"};
        jsonObject.put("legend", topics);
        jsonObject.put("xAxis", xAxis);
        ArrayList<Map> series = new ArrayList<Map>();
        for (int i = 0; i < topics.size(); i++) {
            HashMap map = new HashMap();
            map.put("name", topics.get(i));
            map.put("type", "line");
            map.put("stack", "总量");
            List<String> logEndOffset = new ArrayList<>();
            for (int k = 0; k < result.size(); k++) {

                if (topics.get(i).equals(result.get(k).getTopic())) {
                    logEndOffset.add(result.get(k).getLogEndOffset());
                }
            }
            map.put("data", logEndOffset);
            series.add(map);
        }
        jsonObject.put("series", series);
        return jsonObject;
    }

    /*
     * 获取自然人积压量
     * */
    @GetMapping("/getLags")
    public JSONArray getLags(String groupIds) throws ExecutionException, InterruptedException {
        JSONArray arr = JSONObject.parseArray(groupIds);
        //List<KafkaLagHisEnity> lags=new ArrayList<>();
        // KafkaLagHisEnity kafkaLagHisEnity =new KafkaLagHisEnity();
        JSONArray result = kafkaLagHisService.getLag(arr);
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
    /*
     * 调整自然人偏移量
     * */
    @PostMapping("/seekOffset")
    public void seekOffset(@RequestBody KafkaLagHisEnity kafkaLagHisEnity) throws ExecutionException, InterruptedException {

        kafkaLagHisService.seekOffset(kafkaLagHisEnity);

      /*  JSONArray arr=JSONObject.parseArray(groupIds);
        List<KafkaLagHisEnity> lags=new ArrayList<>();
        KafkaLagHisEnity kafkaLagHisEnity =new KafkaLagHisEnity();
        JSONArray result= kafkaLagHisService.getLag(arr);
        return result;*/
    }

    /*
     * 查询dacp各个流程kafka积压量
     * */
    @GetMapping("/getLagsFromDACP")
    public List<KafkaConfigEntity> getLagsFromDACP(String level, String sysCode) throws ExecutionException, InterruptedException {
        // 从配置库取出相关参数
        KafkaConfigEntity kafkaConfigEntity = new KafkaConfigEntity();
        kafkaConfigEntity.setLevel(level);
        kafkaConfigEntity.setSysCode(sysCode);
        List<KafkaConfigEntity> result = kafkaLagHisService.getTopicAndGroup(kafkaConfigEntity);
        //查询积压量
        KafkaOffsetTools tool = new KafkaOffsetTools();
        customMultiThreadingService.getLag(kafkaConfigEntity);
        // getLags(result);
    /*    Iterator iterator = result.iterator();
        ArrayList<Future<KafkaConfigEntity>> loadResults = new ArrayList<Future<KafkaConfigEntity>>();
        while (iterator.hasNext()) {
            Future<KafkaConfigEntity> future = customMultiThreadingService.getLag((KafkaConfigEntity) iterator.next());
            loadResults.add(future);
        }
        List<KafkaConfigEntity> resultFinal=new ArrayList<KafkaConfigEntity>();
        if(getLoadPercent(loadResults))
        {
            Iterator<Future<KafkaConfigEntity>> it = loadResults.iterator();
            while (it.hasNext()) {
                Future<KafkaConfigEntity> next = it.next();
                resultFinal.add(next.get());
            }
        }
        return resultFinal;*/
    return null;

    }

    public boolean  getLoadPercent(ArrayList<Future<KafkaConfigEntity>>  loadResults ) {
        Iterator<Future<KafkaConfigEntity>> it = loadResults.iterator();
        int size =loadResults.size();
        while (it.hasNext()&&size>0) {
            Future<KafkaConfigEntity> next = it.next();
            if (next.isDone()) {
             //   System.out.println("isDone"+next.isDone());
                size--;
            }
            else{
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }if(size>0)
        {
            getLoadPercent(loadResults);
        }
        return true;
    }
}
