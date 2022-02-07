package com.ceti.wholesale.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class OtherDiscount {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "month")
    private Integer month;

    @Column(name = "year")
    private Integer year;

    @Column(name = "customer_code")
    private String customerCode;

    @Column(name = "date_from")
    private Integer dateFrom;

    @Column(name = "date_to")
    private Integer dateTo;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "weight_discount")
    private BigDecimal weightDiscount;
}
