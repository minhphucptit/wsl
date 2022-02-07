package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class CustomerPriceByMonthDto {

    private String id;

    private String customerId;

    private String customerName;

    private String groupId;

    private String groupName;

    private Instant createdAt;
    
    private Integer year;

    private BigDecimal month01;

    private BigDecimal month02;

    private BigDecimal month03;

    private BigDecimal month04;

    private BigDecimal month05;

    private BigDecimal month06;

    private BigDecimal month07;

    private BigDecimal month08;

    private BigDecimal month09;

    private BigDecimal month10;

    private BigDecimal month11;

    private BigDecimal month12;

    @JsonGetter("created_at")
    public Object getCreateAt() {
        try {
            return createdAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }
}
