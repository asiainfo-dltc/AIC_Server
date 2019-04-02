package com.asiainfo.utils;


import com.asiainfo.model.KafkaLagHisEnity;
import com.asiainfo.service.util.KafkaLagHisService;
import com.asiainfo.utils.kafka.ReadShellLine;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: create by hexin
 * @department: dltc
 * @version: v1.0
 * @description: com.asiainfo.utils
 * @date:2019/3/31
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {

    @Resource(name = "kafkaLagHisServiceImpl")
    private KafkaLagHisService kafkaLagHisService;
    @Resource(name = "readShellLine")
    private ReadShellLine readShellLine;


    //3.添加定时任务

   // @Scheduled(cron = "*/2 * * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
   @Scheduled(cron = "0 0 6,18 * * ?")
    private void configureTasks() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());

        readShellLine.readShellLine();
    }
}
