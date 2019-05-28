package com.asiainfo.service.impl.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.dao.KafkaLagHisDao;
import com.asiainfo.model.KafkaLagHisEnity;
import com.asiainfo.service.util.KafkaLagHisService;
import com.asiainfo.utils.TaskCallable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    @Override
    public List<KafkaLagHisEnity> getLagHis(int pageSize, int start) {
        return kafkaLagHisDao.getKafkaLagHis(pageSize,start);
    }

    @Override
    public JSONArray getLag(JSONArray groupIds) throws ExecutionException, InterruptedException {

        JSONArray result =new JSONArray();

        ExecutorService exec = Executors.newCachedThreadPool();//工头
        ArrayList<Future<List<KafkaLagHisEnity>>> loadResults = new ArrayList<Future<List<KafkaLagHisEnity>>>();//

        for(int i=0;i<groupIds.size();i++){

            String group = groupIds.get(i).toString();  // 遍历 jsonarray 数组，把每一个对象转成 json 对象

            loadResults.add(exec.submit(new TaskCallable(group)));




            //results.add();//submit返回一个Future，代表了即将要返回的结果
           // Future<List<KafkaLagHisEnity>> list= exec.submit(new TaskCallable(group));
        //    List<KafkaLagHisEnity> detail=excuteCommand(group);

            /*    obj.put("groupId",group);
                obj.put("topic",list.get().get(0).getTopic() );
                obj.put("detail",list.get());
                result.add(obj);*/

            /*List<KafkaLagHisEnity> detail=new ArrayList<KafkaLagHisEnity>();
            KafkaLagHisEnity entity =new KafkaLagHisEnity();
            KafkaLagHisEnity entity1 =new KafkaLagHisEnity();
            entity.setCurrentOffset("111");
            entity.setPartition("0");
            entity1.setCurrentOffset("2222");
            entity1.setPartition("2");
            detail.add(entity);
            detail.add(entity1);
            obj.put("groupId",group);
            obj.put("topic",detail.get(0).getTopic());
            obj.put("detail",detail);
            result.add(obj);*/
        }
      //  ArrayList<Future<List<KafkaLagHisEnity>>> loadResultsTemp = loadResults;

        if(getLoadPercent(loadResults))
        {
         //   System.out.println("aaaaa");

            Iterator<Future<List<KafkaLagHisEnity>>> it = loadResults.iterator();
            while (it.hasNext()) {
                Future<List<KafkaLagHisEnity>> next = it.next();
                JSONObject obj =new JSONObject();

                obj.put("groupId",next.get().get(0).getGroupId());
                obj.put("topic",next.get().get(0).getTopic() );
                obj.put("detail",next.get());
                result.add(obj);
            }


        }

        return  result;
    }

    public boolean  getLoadPercent(ArrayList<Future<List<KafkaLagHisEnity>>>  loadResults ) {
        Iterator<Future<List<KafkaLagHisEnity>>> it = loadResults.iterator();
        int size =loadResults.size();
        while (it.hasNext()&&size>0) {
            Future<List<KafkaLagHisEnity>> next = it.next();
           // System.out.println("isDone"+next.isDone());
            if (next.isDone()) {
                System.out.println("isDone"+next.isDone());
                size--;
               // it.remove();
                }
                else{
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
       // System.out.println("size"+loadResults.size());
        if(size>0)
        {
            getLoadPercent(loadResults);
        }
        return true;
    }
    /*public List<KafkaLagHisEnity> excuteCommand(String groupid){
        Process process = null;
        List<KafkaLagHisEnity> processList = new ArrayList<KafkaLagHisEnity>();
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
                    System.out.println("line"+line);
                    KafkaLagHisEnity enity=formatter(line,groupid);
                    System.out.println("getCurrentOffset"+enity.getCurrentOffset());
                    processList.add(enity);
                }
                i++;

            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return processList;
    }

    public KafkaLagHisEnity formatter(String line,String group){


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
        return enity;
    }*/

}
