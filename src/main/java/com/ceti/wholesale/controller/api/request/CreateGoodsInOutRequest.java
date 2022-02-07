package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateGoodsInOutRequest {

    @NotNull(message = "product_id can not be null")
    private String productId;

    private String note;

    private Integer stt;

    @NotNull(message = "type can not be null")
    private String type;

    private BigDecimal inQuantity = BigDecimal.valueOf(0);

    private BigDecimal outQuantity = BigDecimal.valueOf(0);

    private BigDecimal xbxOutQuantity = BigDecimal.valueOf(0);

    private BigDecimal nxeInQuantity = BigDecimal.valueOf(0);

    private BigDecimal xxeOutQuantity = BigDecimal.valueOf(0);

    @NotNull(message = "product_name can not be null")
    private String productName;

    private String productType;

    private BigDecimal price = BigDecimal.valueOf(0);

    private BigDecimal discount = BigDecimal.valueOf(0);

    private String unit;

    private BigDecimal weight = BigDecimal.valueOf(0);

    private Instant voucherAt;

    private String customerId;

    private String companyId;

    private String factoryId;

    private Boolean inFactory =true;

    @Valid
    private CreateGoodsInOutMaintainDetailRequest goodsInOutMaintainDetail;

    private String truckDriverId;

    private String truckLicensePlateNumber;

    private String salesmanId;

    private String salesmanId2;
    
    private BigDecimal cylinderPrice = BigDecimal.valueOf(0);

}