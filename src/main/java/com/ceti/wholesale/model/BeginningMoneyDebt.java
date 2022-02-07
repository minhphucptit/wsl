package com.ceti.wholesale.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "beginning_money_debt")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BeginningMoneyDebt implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "year", columnDefinition = "int", nullable = false)
    private int year;

    @Column(name = "customer_id", columnDefinition = "varchar", nullable = false)
    private String customerId;

    @Column(name = "inventory", columnDefinition = "decimal", nullable = false)
    private BigDecimal inventory = BigDecimal.ZERO;

    @Column(name = "create_by", columnDefinition = "nvarchar", nullable = false)
    private String createBy;

    @Column(name = "create_at", columnDefinition = "datetime", nullable = false)
    private Instant createAt;

    @Column(name = "update_at", columnDefinition = "datetime", nullable = false)
    private Instant updateAt;

    @Column(name = "update_by", columnDefinition = "nvarchar", nullable = false)
    private String updateBy;

    @Column(name = "note", columnDefinition = "nvarchar", nullable = true)
    private String note;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

}
