package com.cloud.server.sql.bean;

import lombok.Data;

import java.util.List;

/**
 * @Author: chenyaohua
 * @Date: 2021/12/17
 * @Description: 字段
 */
@Data
public class WhereColumnsInfo {

    private String tableName;
    private String columns;
}
