package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateProductRequest {

    @Length(max = 255, message = "name must be less than 255 digits")
    private String name;

    @Length(max = 32, message = "categoryId must be less than 32 digits")
    private String categoryId;

    @Length(max = 32, message = "unit must be less than 32 digits")
    private String unit;

    @Length(max = 10, message = "weight must be less than 10 digits")
    private BigDecimal weight;

    @Length(max = 10, message = "cylinderCategory must be less than 10 digits")
    private BigDecimal cylinderCategory;

    @Length(max = 32, message = "purpose must be less than 32 digits")
    private String purpose;

    @Length(max = 64, message = "reference_product_id must be less than 64 digits")
    private String referenceProductId;

    @Length(max = 128, message = "update by full name must be less than 128 digits")
    private String updateByFullName;

    private String type;

    private Boolean isActive;

    private BigDecimal buyPrice;

    private BigDecimal salePrice;

    private String brandId;

    private String colorId;

}
