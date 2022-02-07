package com.ceti.wholesale.common.enums;

public enum ProductCategoryEnum {
    DAILY,
    TDAILY,
    CONGNGHIEP,
    CONGTY;
    static public boolean isMember(String value) {
        for (ProductCategoryEnum e : values())
            if (e.name().equals(value))
                return true;
        return false;
    }

}
