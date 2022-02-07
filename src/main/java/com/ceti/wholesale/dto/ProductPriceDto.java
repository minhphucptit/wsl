package com.ceti.wholesale.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProductPriceDto {

    private String id;

    private BigDecimal buyPrice;

    private BigDecimal salePrice;

    private String updateByFullName;

    private Instant updateAt;

    private String productId;

    private String factoryId;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private ProductDto product;

    @JsonGetter("update_at")
    public Object getActiveAt() {
        try {
            return updateAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }


}
