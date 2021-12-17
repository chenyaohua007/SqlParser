package com.cloud.server.sql.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: chenyaohua
 * @Date: 2021/12/16
 * @Description: 函数方法名枚举
 */
public enum SqlWordEnum {

    SELECT("SELECT","SELECT"),
    FROM("FROM","FROM"),
    WHERE("WHERE","WHERE"),
    LEFT("LEFT JOIN","LEFT JOIN"),
    RIGTH("RIGTH JOIN","RIGTH JOIN"),
    INNER("INNER JOIN","INNER JOIN"),
    UNION("UNION","UNION"),
    UNIONALL("UNION ALL","UNION ALL"),
    INSERT("INSERT","INSERT"),
    INTO("INTO","INTO"),
    VALUES("VALUES","VALUES"),
    UPDATE("UPDATE","UPDATE"),
    SET("SET","SET"),
    DELETE("DELETE","DELETE"),
    AND("AND","AND"),
    OR("OR","OR");

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String desc;

    SqlWordEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
