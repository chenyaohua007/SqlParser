package com.cloud.server.sql.bean;

import lombok.Data;

/**
 * @Author: chenyaohua
 * @Date: 2021/12/17
 * @Description: 表关联关系
 */
@Data
public class TabRelaInfo {

    public String tableLeft;
    public String tableRelaOpretor;
    public String tableRight;
}
