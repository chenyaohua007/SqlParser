package com.cloud.server.sql.enums;

import lombok.Getter;
import lombok.Setter;

public enum ExpSqlRuleEnum {

    TABLE_NAME_A("TABLE_NAME_A",
            "(?<=\\bfrom\\b)[\\s\\S]*?(?=\\b(where|left|join|inner)\\b)"),
    TABLE_NAME_B("TABLE_NAME_B",
            "(?<=\\bjoin\\b)[\\s\\S]*?(?=\\bon\\b)"),
    COLUMNS("COLUMNS",
            "(?<=\\bselect\\b)[\\s\\S]*?(?=\\bfrom\\b)"),
    WHERE_COLUMNS_BEFORE("WHERE_COLUMNS_BEFORE",
            "(?<=\\b(where|and|or|on)\\b)[\\s\\S]*?(?=\\s(>|<|<>|=|!=|like|in|not in|not like)\\s)"),
    WHERE_COLUMNS_OPERATOR("WHERE_COLUMNS_OPERATOR",
            "(\\s(>|<|<>|=|!=|like|in|not in|not like)\\s)"),
    WHERE_COLUMNS_AFTER("WHERE_COLUMNS_AFTER",
            "(?<=\\s(>|<|<>|=|!=|like|in|not in|not like)\\s)[\\s\\S]*?(?=(and|or|;|$))");

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String desc;

    ExpSqlRuleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
