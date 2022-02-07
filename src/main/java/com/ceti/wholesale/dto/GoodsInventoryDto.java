package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsInventoryDto {

    private String id;

    private String voucherId;

    private String voucherNo;

    private String productId;

    private String productName;

    private String productType;

    private String voucherCode;

    private String unit;

    private BigDecimal weight;

    private BigDecimal inventory;

    private String factoryId;
    
    private ProductStatusDto productStatus;

    private String companyId;
    
    private int stt;
}
