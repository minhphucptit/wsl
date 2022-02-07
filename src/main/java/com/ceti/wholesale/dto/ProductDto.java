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
public class ProductDto {

    private String id;

    private String name;

    private String categoryId;

    private String unit;

    private BigDecimal weight;

    private BigDecimal cylinderCategory;

    private String purpose;

    private String referenceProductId;

    private ProductCategoryDto productCategory;

    private String type;

    private Boolean isActive;

    private String factoryId;

    private BigDecimal buyPrice;

    private BigDecimal salePrice;

    private BigDecimal wholesalePrice;

    private CustomerProductPriceDto customerProductPrice;

    private CustomerProductDiscountDto customerProductDiscount;

    private BrandDto brand;

    private ProductTypeDto productType;

    private ColorDto color;

    private ProductUnitDto productUnit;

    private FactoryDto factory;
}
