package com.ceti.wholesale.common.enums;

public enum VoucherEnum {
    VOUCHER_CODE_XUAT_XE_BAN("XBX","X"),
    VOUCHER_CODE_XUAT_BAN("XBH","X"),
    VOUCHER_CODE_XUAT_XE("XXE","XXE"),
    VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY("XNM","X"),
    VOUCHER_CODE_PHIEU_CAN_XUAT("PCX","X"),
    VOUCHER_CODE_EXPORT_MAINTAIN_VOUCHER("XBD","X"),
	
    VOUCHER_CODE_NHAP_XE("NL","NL"),
    VOUCHER_CODE_NHAP_THU_HOI("NXE","NXE"),
    VOUCHER_CODE_PHIEU_CAN_NHAP("PCN","N"),
    VOUCHER_CODE_PHIEU_NHAP_HANG_NHA_MAY("NNM","N"),
    VOUCHER_CODE_PHIEU_THANH_TOAN_THEO_XUAT_XE_BAN("PTX","N"),
    VOUCHER_CODE_PHIEU_THANH_TOAN_THEO_XUAT_BAN("PTH","N"),
    VOUCHER_CODE_PHIEU_THANH_TOAN("PTT","N"),
    VOUCHER_CODE_IMPORT_MAINTAIN_VOUCHER("NBD","N");
	
	private String code;
	private String groupCode;
	
    private VoucherEnum(String code, String groupCode) {
        this.code = code;
        this.groupCode = groupCode;
    }
    
    public static VoucherEnum getEnumFromCode(String label) {
        for (VoucherEnum e : values()) {
            if (e.code.equals(label)) {
                return e;
            }
        }
        return null;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getGroupCode() {
        return groupCode;
    }


}
