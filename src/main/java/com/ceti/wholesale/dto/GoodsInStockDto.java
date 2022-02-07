package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GoodsInStockDto {
    private String id;

    private Integer year;

    private String companyId;

    private String productId;

    private BigDecimal inventory;

    private String createBy;

    private String updateBy;

    private Instant createAt;

    private Instant updateAt;

    private String note;

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

    @JsonGetter("create_at")
    public Object getCreateAt() {
        try {
            return createAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }
}

