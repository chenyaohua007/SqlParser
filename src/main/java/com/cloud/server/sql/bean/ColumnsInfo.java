package com.cloud.server.sql.bean;

import lombok.Data;

import java.util.List;

/**
 * @Author: chenyaohua
 * @Date: 2021/12/17
 * @Description: 字段
 */
@Data
public class ColumnsInfo {

    private String tableName;
    private List<String> columns;
}
