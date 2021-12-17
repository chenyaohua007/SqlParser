package com.cloud.server.sql.bean;

import lombok.Data;

@Data
public class WhereInfo {

    private WhereColumnsInfo columnBefore;
    private String operator;
    private WhereColumnsInfo columnAfter;
    private String paramValue;
}
