package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ManufacturingMaterialDto {

    private String id;

    private String manufacturingIoVoucherId;

    private String voucherNo;

    private String productId;

    private String productName;

    private String voucherCode;

    private String unit;

    private String note;

    private String productType;

    private String factoryId;

    private BigDecimal inQuantity= BigDecimal.ZERO;

    private BigDecimal outQuantity= BigDecimal.ZERO;

    private String type;

}
