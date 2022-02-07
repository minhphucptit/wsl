package com.ceti.wholesale.common.enums;

public enum GoodsInOutTypeEnum {
    NDVO("Nhập đổi vỏ"),
    NGDU("Nhập gas dư"),
    NHTL("Nhập hàng trả lại"),
    NKNM("Nhập kho"),
    NKTH("Nhập thu hồi"),
    XBKM("Xuất bán khuyến mãi"),
    XBSP("Xuất bán sản phẩm"),
    XBTC("Xuất bán thế chân"),
    XDVO("Xuất đổi vỏ"),
    XGDU("Xuất gas dư"),
    XKNM("Xuất kho"),
    XTTH("Xuất trả thu hồi"),
    XTVO("Xuất trả vỏ"),
    TTKH("Thanh toán khách hàng"),
    TCV("Trả cược vỏ");

    private String name;

    private GoodsInOutTypeEnum(String name) {
        this.name = name;
    }

    public static GoodsInOutTypeEnum getEnumFromValue(String label) {
        for (GoodsInOutTypeEnum e : values()) {
            if (e.name.equals(label)) {
                return e;
            }
        }
        return null;
    }

    static public boolean isMember(String value) {
        for (GoodsInOutTypeEnum e : values())
            if (e.name().equals(value))
                return true;
        return false;
    }

    public String getName() {
        return name;
    }

}
