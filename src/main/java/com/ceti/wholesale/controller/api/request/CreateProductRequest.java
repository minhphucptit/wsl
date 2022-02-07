package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateProductRequest {
    @NotNull(message = "id can not be null")
    @Length(max = 64, message = "id must be less than 64 digits")
    private String id;

    @Length(max = 255, message = "name must be less than 255 digits")
    private String name;

    @NotNull(message = "category_id can not be null")
    @Length(max = 32, message = "categoryId must be less than 32 digits")
    private String categoryId;

    @NotNull(message = "unit can not be null")
    @Length(max = 32, message = "unit must be less than 32 digits")
    private String unit;

    @Length(max = 10, message = "weight must be less than 10 digits")
    private BigDecimal weight;

    @Length(max = 10, message = "cylinderCategory must be less than 10 digits")
    private BigDecimal cylinderCategory;

//    @NotNull(message = "purpose can not be null")
//    @Length(max = 32, message = "purpose must be less than 32 digits")
    private String purpose;

    @Length(max = 64, message = "reference_product_id must be less than 64 digits")
    private String referenceProductId;

    @Length(max = 10, message = "buy_price ,must be less than 10 digits")
    private BigDecimal buyPrice;

    @Length(max = 10, message = "sale_price must be less than 10 digits")
    private BigDecimal salePrice;

    private String type;

    private String updateByFullName;

    private String brandId;

    private String colorId;
}
