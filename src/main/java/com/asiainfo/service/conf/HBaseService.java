package com.asiainfo.service.conf;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * HBase数据库基本操作
 */

public class HBaseService {
    private Logger log = LoggerFactory.getLogger(HBaseService.class);

    // The administrative API for HBase
    // Admin can be used to create, drop, list, enable and disable and
    // otherwise modify tables,
    // as well as perform other administrative operations.
    private Admin admin_int = null;
    private Connection connection_int = null;

    private Admin admin_AB = null;
    private Connection connection_AB = null;

    private Admin admin_prd = null;
    private Connection connection_prd = null;

   /* private Admin admin = null;
    private Connection connection = null;*/

    public HBaseService(Configuration conf_int,Configuration conf_AB,Configuration conf_prd) {
        try {

            connection_int = ConnectionFactory.createConnection(conf_int);
            admin_int = connection_int.getAdmin();

            connection_AB = ConnectionFactory.createConnection(conf_AB);
            admin_AB = connection_AB.getAdmin();

            connection_prd = ConnectionFactory.createConnection(conf_prd);
            admin_prd = connection_prd.getAdmin();

        } catch (IOException e) {
            log.error("获取HBase连接失败");
        }
    }

    /**
     * 创建表
     * create <table>, {NAME => <column family>, VERSIONS => <VERSIONS>}
     * shell command: create ‘user’, ‘cf1’
     */
    /*public boolean creatTable(String tableName, List<String> columnFamily) {
        try {
            //列族 column family
            List<ColumnFamilyDescriptor> cfDesc = new ArrayList<>(columnFamily.size());
            columnFamily.forEach(cf -> {
                cfDesc.add(ColumnFamilyDescriptorBuilder.newBuilder(
                        Bytes.toBytes(cf)).build());
            });
            //表 table
            TableDescriptor tableDesc = TableDescriptorBuilder
                    .newBuilder(TableName.valueOf(tableName))
                    .setColumnFamilies(cfDesc).build();

            if (admin.tableExists(TableName.valueOf(tableName))) {
                log.debug("table Exists!");
            } else {
                admin.createTable(tableDesc);
                log.debug("create table Success!");
            }
        } catch (IOException e) {
            log.error(MessageFormat.format("创建表{0}失败", tableName), e);
            return false;
        } finally {
            close(admin, null, null);
        }
        return true;
    }*/

    /**
     * 查询库中所有表的表名
     * shell command: list
     */
  /*  public void  init(String env){

        if(env=="int")
        {
            admin=admin_int;
            connection=connection_int;
        }else if(env=="AB"){
            admin=null;
            admin=admin_AB;
            connection=connection_AB;
        }else
        {
            admin=admin_prd;
            connection=connection_prd;
        }

    }*/
    public List<String> getAllTableNames(String env ) {
        List<String> result = new ArrayList<>();
        Admin admin = null;

        TableName[] tableNames =null;
        try {
            if(env=="int")
            {

                tableNames = admin_int.listTableNames();

            }else if(env=="AB"){
                tableNames = admin_AB.listTableNames();

            }else
            {
                tableNames = admin_prd.listTableNames();
            }


            for (TableName tableName : tableNames) {
                result.add(tableName.getNameAsString());
            }
        } catch (IOException e) {
            log.error("获取所有表的表名失败", e);
        } finally {
            close(admin, null, null);
        }
        return result;
    }

    /**
     * 遍历查询指定表中的所有数据
     * shell command: scan 'user'
     */
    public Map<String, Map<String, String>> getResultScanner(String tableName,String env) {
        Scan scan = new Scan();
        return this.queryData(tableName, scan,env);
    }

    /**
     * 通过表名以及过滤条件查询数据
     */
    private Map<String, Map<String, String>> queryData(String tableName,
                                                       Scan scan,String env) {
        // <rowKey,对应的行数据>
        Map<String, Map<String, String>> result = new HashMap<>();

        ResultScanner rs = null;
        // 获取表
        Table table = null;
        try {
            table = getTable(tableName,env);
            rs = table.getScanner(scan);
            for (Result r : rs) {
                // 每一行数据
                Map<String, String> columnMap = new HashMap<>();
                String rowKey = null;
                // 行键，列族和列限定符一起确定一个单元（Cell）
                for (Cell cell : r.listCells()) {
                    if (rowKey == null) {
                        rowKey = Bytes.toString(cell.getRowArray(),
                                cell.getRowOffset(), cell.getRowLength());
                    }
                    columnMap.put(
                            // 列限定符
                            Bytes.toString(cell.getQualifierArray(),
                                    cell.getQualifierOffset(),
                                    cell.getQualifierLength()),
                            // 列族
                            Bytes.toString(cell.getValueArray(),
                                    cell.getValueOffset(),
                                    cell.getValueLength()));
                }

                if (rowKey != null) {
                    result.put(rowKey, columnMap);
                }
            }
        } catch (IOException e) {
            log.error(MessageFormat.format("遍历查询指定表中的所有数据失败,tableName:{0}",
                    tableName), e);
        } finally {
            close(null, rs, table);
        }

        return result;
    }

    /**
     * 根据tableName和rowKey精确查询行数据
     */
    public List<Map<String,String>> getRowData(String tableName, String rowKey,String env) {
        // 返回的键值对
        Map<String, String> map = new HashMap<>();
        List<Map<String,String>> result=new ArrayList<>();
        Get get = new Get(Bytes.toBytes(rowKey));
        // 获取表
        Table table = null;
        try {
            table = getTable(tableName,env);
            Result hTableResult = table.get(get);
            if (hTableResult != null && !hTableResult.isEmpty()) {
                for (Cell cell : hTableResult.listCells()) {

                    map.put(
                            Bytes.toString(cell.getQualifierArray(),
                                    cell.getQualifierOffset(),
                                    cell.getQualifierLength()),
                            Bytes.toString(cell.getValueArray(),
                                    cell.getValueOffset(),
                                    cell.getValueLength()));
                    String timestap;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(cell.getTimestamp());
                    timestap = simpleDateFormat.format(date);
                    map.put("timestap",timestap);

                }
                result.add(map);
            }
        } catch (IOException e) {
            log.error(MessageFormat.format(
                    "查询一行的数据失败,tableName:{0},rowKey:{1}", tableName, rowKey), e);
        } finally {
            close(null, null, table);
        }

        return result;
    }

    /**
     * 为表添加 or 更新数据
     */
    public void putData(String tableName, String rowKey, String familyName,
                        String[] columns, String[] values,String env) {
        // 获取表
        Table table = null;
        try {
            table = getTable(tableName,env);

            putData(table, rowKey, tableName, familyName, columns, values);
        } catch (Exception e) {
            log.error(MessageFormat.format(
                    "为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}",
                    tableName, rowKey, familyName), e);
        } finally {
            close(null, null, table);
        }
    }

    private void putData(Table table, String rowKey, String tableName,
                         String familyName, String[] columns, String[] values) {
        try {
            // 设置rowkey
            Put put = new Put(Bytes.toBytes(rowKey));

            if (columns != null && values != null
                    && columns.length == values.length) {
                for (int i = 0; i < columns.length; i++) {
                    if (columns[i] != null && values[i] != null) {
                        put.addColumn(Bytes.toBytes(familyName),
                                Bytes.toBytes(columns[i]),
                                Bytes.toBytes(values[i]));
                    } else {
                        throw new NullPointerException(MessageFormat.format(
                                "列名和列数据都不能为空,column:{0},value:{1}", columns[i],
                                values[i]));
                    }
                }
            }

            table.put(put);
            log.debug("putData add or update data Success,rowKey:" + rowKey);
            table.close();
        } catch (Exception e) {
            log.error(MessageFormat.format(
                    "为表添加 or 更新数据失败,tableName:{0},rowKey:{1},familyName:{2}",
                    tableName, rowKey, familyName), e);
        }
    }

    /**
     * 根据表名 获取table
     * Used to communicate with a single HBase table.
     * Table can be used to get, put, delete or scan data from a table.
     */
    private Table getTable(String tableName,String env) throws IOException {


        if(env=="int")
        {
            return connection_int.getTable(TableName.valueOf(tableName));

        }else if(env=="AB"){

            return connection_AB.getTable(TableName.valueOf(tableName));
        }else
        {
            return connection_prd.getTable(TableName.valueOf(tableName));
        }
       // return connection.getTable(TableName.valueOf(tableName));
    }

    /**
     * 关闭流
     */
    private void close(Admin admin, ResultScanner rs, Table table) {
        if (admin != null) {
            try {
                admin.close();
            } catch (IOException e) {
                log.error("关闭Admin失败", e);
            }
        }

        if (rs != null) {
            rs.close();
        }

        if (table != null) {
            try {
                table.close();
            } catch (IOException e) {
                log.error("关闭Table失败", e);
            }
        }
    }
}

