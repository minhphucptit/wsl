package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateGoodsInOutRequest {

    private String id;

    private String voucherNo;

    private String productId;

    private String type;

    private String note;

    private BigDecimal inQuantity;

    private BigDecimal outQuantity;

    private String productName;

    private BigDecimal price;

    private BigDecimal discount;

    private String unit;

    private String customerId;

    private String companyId;

    private Boolean inFactory=true;
}
