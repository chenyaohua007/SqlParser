package com.cloud.server.sql.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: chenyaohua
 * @Date: 2021/12/16
 * @Description: 函数方法名枚举
 */
public enum OperatorEnum {

    A(">",">"),
    B("<","<"),
    C("=","="),
    D("<>","<>"),
    E("!=","!="),
    F("IN","IN"),
    G("NOT IN","NOT IN"),
    H("LIKE","LIKE"),
    I("NOT LIKE","NOT LIKE");

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String desc;

    OperatorEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
