package com.cloud.server.sql;

import com.alibaba.fastjson.JSON;
import com.cloud.server.sql.bean.ColumnsInfo;
import com.cloud.server.sql.bean.TableInfo;
import com.cloud.server.sql.bean.WhereColumnsInfo;
import com.cloud.server.sql.bean.WhereInfo;
import com.cloud.server.sql.enums.ExpSqlRuleEnum;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: chenyaohua
 * @Date: 2021/12/17
 * @Description: sql解析工具
 */
public class SqlAstUtil {

    /**
     * @Author: chenyaohua
     * @Date: 2021/12/17
     * @Description: 取sql参数表达式
     */
    private static final String REG_SQL_VAL_A = "(?<=\\s(%s\\s(>|<|<>|=|!=|like|in|not in|not like)\\s'))[\\s\\S]*?(?=('|$))";
    private static final String REG_SQL_VAL_B = "(?<=(%s\\s(>|<|<>|=|!=|like|in|not in|not like)\\s'))[\\s\\S]*?(?=('|$))";
    private static final String REG_SQL_VAL_C = "(?<=\\s(%s\\s(>|<|<>|=|!=|like|in|not in|not like)'))[\\s\\S]*?(?=('|$))";
    private static final String REG_SQL_VAL_D = "(?<=(%s\\s(>|<|<>|=|!=|like|in|not in|not like)'))[\\s\\S]*?(?=('|$))";

    /**
     * @Author: chenyaohua
     * @Date: 2021/12/17
     * @Description: 获取表名
     */
    public static List<TableInfo> getTableName(String sql){
        String regexA = ExpSqlRuleEnum.TABLE_NAME_A.getDesc();
        List<TableInfo> resA = getTableNameCore(regexA,sql);
        String regexB = ExpSqlRuleEnum.TABLE_NAME_B.getDesc();
        List<TableInfo> resB = getTableNameCore(regexB,sql);
        resA.addAll(resB);
        return resA;
    }

    /**
     * @Author: chenyaohua
     * @Date: 2021/12/17
     * @Description: 获取字段名
     */
    public static List<ColumnsInfo> getColumn(String sql){
        String regex = ExpSqlRuleEnum.COLUMNS.getDesc();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sql);

        List<ColumnsInfo> res = new ArrayList<>(16);
        while (matcher.find()) {
            String[] columns = matcher.group().trim().split(",");

            // 获取表名+字段
            for (String column : columns) {
                String name = column.trim();
                String[] ci = name.split("[.]");
                if (ci.length > 1) {
                    List<String> cloumns = new ArrayList<>(16);
                    ColumnsInfo columnsInfo = new ColumnsInfo();
                    columnsInfo.setColumns(cloumns);
                    columnsInfo.setTableName(ci[0]);
                    if (res.stream().noneMatch(item -> item.getTableName().equals(ci[0]))) {
                        res.add(columnsInfo);
                    }
                }
            }

            // 去重
            Set<String> cloumns = new HashSet<String>(16);
            res.forEach(item -> {
                for (String column : columns) {
                    String name = column.trim();
                    String[] ci = name.split("[.]");

                    if (ci.length > 1) {
                        if (ci[0].equals(item.getTableName())) {
                            item.getColumns().add(ci[1]);
                        }
                    } else {
                        cloumns.add(ci[0]);
                    }
                }
            });
            if(!CollectionUtils.isEmpty(cloumns)){
                ColumnsInfo columnsInfo = new ColumnsInfo();
                List<String> finalList = new ArrayList<>(cloumns);
                columnsInfo.setColumns(finalList);
                res.add(columnsInfo);
            }

        }
        return res;
    };

    /**
     * @Author: chenyaohua
     * @Date: 2021/12/17
     * @Description: 获取where条件字段
     */
    public static List<WhereInfo> getWhereColumns(String sql){
        List<WhereInfo> res = new ArrayList<>(16);
        String[] regex = new String[]{ExpSqlRuleEnum.WHERE_COLUMNS_BEFORE.getDesc(),ExpSqlRuleEnum.WHERE_COLUMNS_AFTER.getDesc()};

        String regexOperator = ExpSqlRuleEnum.WHERE_COLUMNS_OPERATOR.getDesc();

        Pattern patternOperator = Pattern.compile(regexOperator);
        Matcher matcherOperator = patternOperator.matcher(sql);
        List<String> opList = new ArrayList<>(16);
        while (matcherOperator.find()) {
            String operator = matcherOperator.group().trim();
            opList.add(operator);
        }

        // 取表达式前后表别名+字段名
        List<WhereColumnsInfo> beforeList = new ArrayList<>(16);
        List<WhereColumnsInfo> afterList = new ArrayList<>(16);
        for (int i = 0; i < regex.length; i++) {
            Pattern pattern = Pattern.compile(regex[i]);
            Matcher matcher = pattern.matcher(sql);
            int idx = 0;
            while (matcher.find()) {
                String str = matcher.group().trim();
                String[] ti = str.split("[.]");
                if (ti.length > 1) {
                    WhereColumnsInfo whereColumnsInfo = new WhereColumnsInfo();
                    whereColumnsInfo.setTableName(ti[0]);
                    whereColumnsInfo.setColumns(ti[1]);
                    if(regex[i].equals(ExpSqlRuleEnum.WHERE_COLUMNS_BEFORE.getDesc())){
                        beforeList.add(idx,whereColumnsInfo);
                        afterList.add(idx,new WhereColumnsInfo());
                    } else {
                        afterList.add(idx,whereColumnsInfo);
                    }
                    idx++;
                }
            }
        }
        for (int i = 0; i < beforeList.size(); i++) {
            WhereInfo whereInfo = new WhereInfo();
            whereInfo.setColumnBefore(beforeList.get(i));
            whereInfo.setColumnAfter(afterList.get(i));
            whereInfo.setOperator(opList.get(i));
            res.add(whereInfo);
        }

        // 取sql 字段名+对应参数值
        String[] regSqlVal = new String[]{REG_SQL_VAL_A,REG_SQL_VAL_B,REG_SQL_VAL_C,REG_SQL_VAL_D};
        res.forEach(item -> {
            if(!StringUtils.isEmpty(item.getColumnBefore().getTableName()) &&
                    StringUtils.isEmpty(item.getColumnAfter().getTableName())){
                String suffix = item.getColumnBefore().getTableName()+"."+item.getColumnBefore().getColumns();
                for (String s : regSqlVal) {
                    String expReg = String.format(s, suffix);
                    Pattern sqlParamOperator = Pattern.compile(expReg);
                    Matcher qlParamOperator = sqlParamOperator.matcher(sql);
                    while (qlParamOperator.find()) {
                        String sqlParam = qlParamOperator.group();
                        item.setParamValue(sqlParam);
                    }
                }


            }
        });
        return res;
    }

    /**
     * @Author: chenyaohua
     * @Date: 2021/12/17
     * @Description: 获取表名
     */
    private static List<TableInfo> getTableNameCore(String regex,String sql){
        List<TableInfo> res = new ArrayList<>(16);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            String[] tableNames = matcher.group().split(",");
            for (String tableName : tableNames) {
                TableInfo tableInfo = new TableInfo();
                String name = tableName.trim();
                String[] ti = name.split(" ");
                if (ti.length > 1) {
                    tableInfo.setName(ti[0]);
                    tableInfo.setAlias(ti[1]);
                } else {
                    tableInfo.setName(ti[0]);
                }
                res.add(tableInfo);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String sql = "select t1.name,t1.code,t2.age,t2.address,school " +
                "from person t1 left join person_info t2 on t1.id = t2.id and t1.code > t2.age and t1.name = '12321'";
        List<TableInfo> tableNameList = SqlAstUtil.getTableName(sql);
        System.out.println(JSON.toJSON(tableNameList));

        List<ColumnsInfo> columnsInfos = SqlAstUtil.getColumn(sql);
        System.out.println(JSON.toJSON(columnsInfos));

        List<WhereInfo> whereInfo = SqlAstUtil.getWhereColumns(sql);
        System.out.println(JSON.toJSON(whereInfo));


    }
}
