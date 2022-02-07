package com.ceti.wholesale.model;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class CustomerPrice {

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 128)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "customer_name", columnDefinition = "nvarchar", nullable = false)
    private String customerName;

    @Column(name = "customer_id", nullable = false, length = 32)
    private String customerId;

    @Column(name = "created_at", nullable = false, length = 32)
    private Instant createdAt;

    @Column(name = "year")
    private Integer year;
    
    @Column(name = "month")
    private Integer month;
    
    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "customer_category", length = 32)
    private String customerCategory;

    @Column(name = "day1", length = 10)
    private BigDecimal day01;

    @Column(name = "day2", length = 10)
    private BigDecimal day02;

    @Column(name = "day3", length = 10)
    private BigDecimal day03;

    @Column(name = "day4", length = 10)
    private BigDecimal day04;

    @Column(name = "day5", length = 10)
    private BigDecimal day05;

    @Column(name = "day6", length = 10)
    private BigDecimal day06;

    @Column(name = "day7", length = 10)
    private BigDecimal day07;

    @Column(name = "day8", length = 10)
    private BigDecimal day08;

    @Column(name = "day9", length = 10)
    private BigDecimal day09;

    @Column(name = "day10", length = 10)
    private BigDecimal day10;

    @Column(name = "day11", length = 10)
    private BigDecimal day11;

    @Column(name = "day12", length = 10)
    private BigDecimal day12;

    @Column(name = "day13", length = 10)
    private BigDecimal day13;

    @Column(name = "day14", length = 10)
    private BigDecimal day14;

    @Column(name = "day15", length = 10)
    private BigDecimal day15;

    @Column(name = "day16", length = 10)
    private BigDecimal day16;

    @Column(name = "day17", length = 10)
    private BigDecimal day17;

    @Column(name = "day18", length = 10)
    private BigDecimal day18;

    @Column(name = "day19", length = 10)
    private BigDecimal day19;

    @Column(name = "day20", length = 10)
    private BigDecimal day20;

    @Column(name = "day21", length = 10)
    private BigDecimal day21;

    @Column(name = "day22", length = 10)
    private BigDecimal day22;

    @Column(name = "day23", length = 10)
    private BigDecimal day23;

    @Column(name = "day24", length = 10)
    private BigDecimal day24;

    @Column(name = "day25", length = 10)
    private BigDecimal day25;

    @Column(name = "day26", length = 10)
    private BigDecimal day26;

    @Column(name = "day27", length = 10)
    private BigDecimal day27;

    @Column(name = "day28", length = 10)
    private BigDecimal day28;

    @Column(name = "day29", length = 10)
    private BigDecimal day29;

    @Column(name = "day30", length = 10)
    private BigDecimal day30;

    @Column(name = "day31", length = 10)
    private BigDecimal day31;

}
