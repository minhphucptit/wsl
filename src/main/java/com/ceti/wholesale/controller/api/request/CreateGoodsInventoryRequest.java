package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateGoodsInventoryRequest {

    @NotNull(message = "product_id can not be null")
    private String productId;

    @NotNull(message = "product_name can not be null")
    private String productName;

    @NotNull(message = "product_type can not be null")
    private String productType;

    @NotNull(message = "unit can not be null")
    private String unit;

    private BigDecimal weight = BigDecimal.ZERO;

    private BigDecimal inventory = BigDecimal.ZERO;
    
    private String productStatusId;

    private String companyId;
    
    private int stt;
}