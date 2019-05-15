
package com.asiainfo.utils.hbase;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class HbaseUtils {

    private static final Logger logger = Logger.getLogger(HbaseUtils.class);

    @Value("${ZK_ADDRESS}")
    private String ZK_ADDRESS;

    @Value("${ZK_ZNODE_PARENT}")
    private String ZK_ZNODE_PARENT;

    public Connection getOrderConnection(){
        org.apache.hadoop.conf.Configuration config = HBaseConfiguration.create();
        config.set(HConstants.ZOOKEEPER_QUORUM, ZK_ADDRESS);
        config.set(HConstants.ZOOKEEPER_ZNODE_PARENT, ZK_ZNODE_PARENT);
        Connection connection = null;
        try {
            connection = ConnectionFactory.createConnection(config);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return connection;
    }

    public Table getOrderRelationTable(Connection connection){
        Table table = null;
        try {
            TableName tableName = TableName.valueOf("ORDER_RELATION_TABLE");
            table = connection.getTable(tableName);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return table;
    }

    public String printDifference(Date startDate, Date endDate){
        long different = endDate.getTime() - startDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long differHours = different / hoursInMilli;
        return String.valueOf(differHours);
    }

    public static void main(String[] args){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long handleDate = System.currentTimeMillis()-1000*60*60;
        System.out.println(handleDate);
        String HANDLE_DATE = sdf.format(new Date(Long.valueOf(handleDate)));
        System.out.println(HANDLE_DATE);

        try {
            Date date1 = sdf.parse("2019-04-14 16:35:17");
            Date date2 = sdf.parse("2019-04-16 17:36:18");
            new HbaseUtils().printDifference(date1,date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
