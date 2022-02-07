package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Table(name = "beginning_cylinder_debt")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BeginningCylinderDebt {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "year", columnDefinition = "int", nullable = false)
    private int year;

    @Column(name = "customer_id", nullable = false, length = 32)
    private String customerId;

    @Column(name = "product_id", nullable = false, length = 32)
    private String productId;

    @Column(name = "inventory", nullable = false, length = 32)
    private BigDecimal inventory;

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
