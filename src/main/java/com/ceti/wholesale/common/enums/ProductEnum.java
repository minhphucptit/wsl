package com.ceti.wholesale.common.enums;

public enum ProductEnum {
    GBONAPCAO("GAS BỒN ÁP CAO"),
    GASDU("Gas dư"),
    GASTHUHOI("GAS THU HỒI");

    private String name;

    private ProductEnum(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
