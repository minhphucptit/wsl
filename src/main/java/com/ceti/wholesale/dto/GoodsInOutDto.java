package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.persistence.Column;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsInOutDto {

    private String id;

    private String voucherId;

    private String voucherNo;

    private String productId;

    private String productName;

    private String productType;

    private String voucherCode;

    private String type;

    private String stt;

    private String note;

    private BigDecimal inQuantity;

    private BigDecimal outQuantity;

    private BigDecimal price;

    private BigDecimal discount;

    private String unit;

    private BigDecimal xbxOutQuantity;

    private BigDecimal nxeInQuantity;

    private BigDecimal xxeOutQuantity;

    private Boolean isMainProduct;

    private Boolean inFactory;

    private Instant voucherAt;

    private String customerId;

    private String companyId;

    private BigDecimal weight;

    private String factoryId;

    private String productUnitName;

    private GoodsInOutMaintainDetailDto goodsInOutMaintainDetail;

    private String truckDriverId;

    private String truckLicensePlateNumber;
    
    private Instant quantityAt;
    
    @JsonGetter("voucher_at")
    public Object getVoucherAt() {
        try {
            return voucherAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }
    
    @JsonGetter("quantity_at")
    public Object getQuantityAt() {
        try {
            return quantityAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

}
