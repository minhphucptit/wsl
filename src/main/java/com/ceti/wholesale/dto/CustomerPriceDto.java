package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import javax.persistence.Column;
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
public class CustomerPriceDto {

    private String id;

    private String customerId;

    private String customerName;

    private String groupId;

    private String groupName;

    private Instant createdAt;

    private String updateDate;

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

    private String factoryId;

    private String customerCategory;

    @JsonGetter("created_at")
    public Object getCreateAt() {
        try {
            return createdAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

}
