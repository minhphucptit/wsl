package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateManufacturingMaterialRequest {


    @Length(max = 32, message = "productId must be less than 32 digits")
    private String productId;

    @Length(max = 255, message = "productName must be less than 255 digits")
    private String productName;

    @Length(max = 32, message = "unit must be less than 32 digits")
    private String unit;

    @Length(max = 255, message = "note must be less than 32 digits")
    private String note;

    @Length(max = 32, message = "productType must be less than 32 digits")
    private String productType;

    private BigDecimal inQuantity;

    private BigDecimal outQuantity;

    private String type;

}
