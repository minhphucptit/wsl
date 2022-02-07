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
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateCustomerPriceByMonthRequest {
    @NotNull
    private String id;

    private String customerId;

    private String customerName;

    private String customerCategory;

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
}
