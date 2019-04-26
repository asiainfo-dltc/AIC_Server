package com.asiainfo.utils.kafka;

import com.asiainfo.model.KafkaLagHisEnity;
import com.asiainfo.service.impl.util.KafkaLagHisServiceImpl;
import com.asiainfo.service.util.KafkaLagHisService;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author: create by hexin
 * @department: dltc
 * @version: v1.0
 * @description: com.asiainfo.utils.kafka
 * @date:2019/4/1
 */
@Service("readShellLine")
public class ReadShellLine {
    @Autowired
    private KafkaLagHisService kafkaLagHisService;

    public  void readShellLine() {
        String[] groups=new String[]{
                "T0000_CB.DOMAIN1.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN2.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN3.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN4.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN5.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN6.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN7.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN8.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN1.MAIN.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN2.MAIN.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN3.MAIN.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN4.MAIN.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN5.MAIN.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN6.MAIN.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN7.MAIN.ROUTER.ACCESS_0630_221",
                "T0000_CB.DOMAIN8.MAIN.ROUTER.ACCESS_0630_221"
        };
        for(int i=0;i<groups.length;i++)
        {
            excuteCommand(groups[i]);
        }

    }

    public void excuteCommand(String groupid){
        Process process = null;
        List<String> processList = new ArrayList<String>();
        try {
            //String groupid = "T0000_CB.DOMAIN1.ROUTER.ACCESS_0630_221";
            String commandStr = "export KAFKA_OPTS=\" -Djava.security.auth.login.config=/home/dacp/monitor/zrr-kafka/client-ssl/kafka_cilent_jaas.conf\"\n" + "/home/dacp/monitor/zrr-kafka/kafka_2.12-1.1.0/bin/kafka-consumer-groups.sh --new-consumer --bootstrap-server 10.191.17.109:9062,10.191.17.110:9062 --command-config /home/dacp/monitor/zrr-kafka/client-ssl/client.properties --describe --group " + groupid + " | sort -n -k 2";
            System.out.println("消费组：" + groupid);
            String[] cmd = new String[]{"/bin/sh", "-c", commandStr};
           process = Runtime.getRuntime().exec(cmd);

            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";

            int i =1;
            while ((line = input.readLine()) != null) {
                if(i>2){//判断是第二行，进行文件行内容输出。
                    formatter(line,groupid);
                }
                i++;
                //processList.add(line);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void formatter(String line,String group){


        StringTokenizer pas = new StringTokenizer(line, " ");
        line = ""; //这里清空了str，但StringTokenizer对象中已经保留了原来字符串的内容。
        while (pas.hasMoreTokens()) {
            String s = pas.nextToken();
            line = line + s + " ";
        }
        String[] arr=line.trim().split(" ");
        KafkaLagHisEnity enity=new KafkaLagHisEnity();
        enity.setGroupId(group);
        enity.setTopic(arr[0]);
        enity.setPartition(arr[1]);
        enity.setCurrentOffset(arr[2]);
        enity.setLogEndOffset(arr[3]);
        enity.setLag(arr[4]);
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        Date d= new Date();
        String str = sdf.format(d);
        enity.setOperationTime(str);
        kafkaLagHisService.insertLagHis(enity);
    }
}
