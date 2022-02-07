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
public class UpdateCustomerPriceRequest {

    @NotNull
    private String id;
    
    private String customerId;

    private String customerName;

    private String customerCategory;

    private BigDecimal day01;

    private BigDecimal day02;

    private BigDecimal day03;

    private BigDecimal day04;

    private BigDecimal day05;

    private BigDecimal day06;

    private BigDecimal day07;

    private BigDecimal day08;

    private BigDecimal day09;

    private BigDecimal day10;

    private BigDecimal day11;

    private BigDecimal day12;

    private BigDecimal day13;

    private BigDecimal day14;

    private BigDecimal day15;

    private BigDecimal day16;

    private BigDecimal day17;

    private BigDecimal day18;

    private BigDecimal day19;

    private BigDecimal day20;

    private BigDecimal day21;

    private BigDecimal day22;

    private BigDecimal day23;

    private BigDecimal day24;

    private BigDecimal day25;

    private BigDecimal day26;

    private BigDecimal day27;

    private BigDecimal day28;

    private BigDecimal day29;

    private BigDecimal day30;

    private BigDecimal day31;
}
