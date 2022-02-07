package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Table(name = "customer_price_by_month")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPriceByMonth {
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

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "customer_category", length = 32)
    private String customerCategory;

    @Column(name = "month1", length = 10)
    private BigDecimal month01;

    @Column(name = "month2", length = 10)
    private BigDecimal month02;

    @Column(name = "month3", length = 10)
    private BigDecimal month03;

    @Column(name = "month4", length = 10)
    private BigDecimal month04;

    @Column(name = "month5", length = 10)
    private BigDecimal month05;

    @Column(name = "month6", length = 10)
    private BigDecimal month06;

    @Column(name = "month7", length = 10)
    private BigDecimal month07;

    @Column(name = "month8", length = 10)
    private BigDecimal month08;

    @Column(name = "month9", length = 10)
    private BigDecimal month09;

    @Column(name = "month10", length = 10)
    private BigDecimal month10;

    @Column(name = "month11", length = 10)
    private BigDecimal month11;

    @Column(name = "month12", length = 10)
    private BigDecimal month12;



}
