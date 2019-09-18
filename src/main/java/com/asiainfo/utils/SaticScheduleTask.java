package com.asiainfo.utils;


import com.alibaba.fastjson.JSONObject;
import com.asiainfo.model.KafkaLagHisEnity;
import com.asiainfo.service.util.KafkaLagHisService;
import com.asiainfo.utils.hbase.HbaseUtils;
import com.asiainfo.utils.kafka.ReadShellLine;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

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


    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    protected HbaseUtils hbaseUtils;

    private Logger log = LoggerFactory.getLogger(SaticScheduleTask.class);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //3.添加定时任务
    //每五分钟执行一次
    //@Scheduled(cron = "0 0 * * * ? ")
   // @Scheduled(cron = "0 0/10 * * * ?")
  //  @Scheduled(cron = "*/30 * * * * ? ")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    //每日24点执行
   @Scheduled(cron = "0 0 0 * * ?")
    private void configureTasks() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());

       // ReadShellLine readShellLine=new ReadShellLine();
       readShellLine.readShellLine();
    }
    /*@Scheduled(fixedRate=1000*60)
    public void doTask(){
        try {
            long handleDate = System.currentTimeMillis()-1000*60*60;
            String handleDateScope = sdf.format(new Date(Long.valueOf(handleDate)));
            boolean doallflg = true;

            Connection connection = hbaseUtils.getOrderConnection();
            Table table = hbaseUtils.getOrderRelationTable(connection);

            while(doallflg) {
                String selSql = "SELECT ORDER_LINE_ID,SYS_CODE,CREATE_DATE from INCOMPLETE_ORDER_RELATIONSHIP_TABLE WHERE HANDLE_DATE < '" + handleDateScope + "' LIMIT 10";
                List<Map<String, Object>> list = jdbcTemplate.queryForList(selSql);
                int flgSize = list.size();
                if(flgSize==0)  doallflg=false;
                for(Map<String, Object> map :list) {

                    String ORDER_LINE_ID = String.valueOf(map.get("ORDER_LINE_ID"));
                    String SYS_CODE = String.valueOf(map.get("SYS_CODE"));
                    String CREATE_DATE = String.valueOf(map.get("CREATE_DATE"));
                    String isOuterOrder = null;
                    String topSysCode = SYS_CODE;
                    String outOrderLineId = ORDER_LINE_ID;
                    String sysRoute = SYS_CODE;
                    String orderIdRoute = ORDER_LINE_ID;
                    JSONObject remarkJson = new JSONObject();
                    do {
                        String shaOutOrderLineId = DigestUtils.shaHex(outOrderLineId);
                        String rowKey = shaOutOrderLineId.substring(shaOutOrderLineId.length() - 3) + topSysCode + shaOutOrderLineId;
                        Get get = new Get(rowKey.getBytes());
                        Result relaRs = table.get(get);
                        if (relaRs.size() > 0) {
                            isOuterOrder = Bytes.toString(relaRs.getValue(Bytes.toBytes("F1"), Bytes.toBytes("IS_OUTER_ORDER")));
                            topSysCode = Bytes.toString(relaRs.getValue(Bytes.toBytes("F1"), Bytes.toBytes("SYS_CODE")));
                            outOrderLineId = Bytes.toString(relaRs.getValue(Bytes.toBytes("F1"), Bytes.toBytes("OUTER_ORDER_ID")));

                            if("1".equals(isOuterOrder)){
                                sysRoute = topSysCode +"|"+sysRoute;
                                orderIdRoute = outOrderLineId + "|" + orderIdRoute;
                            }
                        } else {
                            break;
                        }
                    } while ("1".equals(isOuterOrder) && StringUtils.isNotBlank(outOrderLineId) && StringUtils.isNotBlank(topSysCode));


                    if ("0".equals(isOuterOrder)) {
                        String deleSql = "DELETE FROM INCOMPLETE_ORDER_RELATIONSHIP_TABLE WHERE ID = '"+ORDER_LINE_ID+"|"+SYS_CODE+"'";
                        jdbcTemplate.update(deleSql);
                    }else{
                        remarkJson.put("orderIdRoute",orderIdRoute);
                        remarkJson.put("sysRoute",sysRoute);
                        long currectTime = System.currentTimeMillis();
                        String currectDate = sdf.format(new Date(Long.valueOf(currectTime)));
                        String DIFFER_HOURS = "0";
                        try {
                            Date date1 = sdf.parse(CREATE_DATE);
                            Date date2 = sdf.parse(currectDate);
                            DIFFER_HOURS = hbaseUtils.printDifference(date1,date2);
                        } catch (ParseException e) {
                            log.error(e.getMessage());
                        }

                        String updateSql = "UPDATE INCOMPLETE_ORDER_RELATIONSHIP_TABLE SET HANDLE_DATE = '"+currectDate+"',REMARK = '"+remarkJson.toJSONString()+"',DIFFER_HOURS = '"+DIFFER_HOURS+"' WHERE ID = '"+ORDER_LINE_ID+"|"+SYS_CODE+"'";
                        jdbcTemplate.update(updateSql);
                    }
                }
            }

            table.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
