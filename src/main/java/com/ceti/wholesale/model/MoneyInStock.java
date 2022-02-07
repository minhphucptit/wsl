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
@Table(name = "money_in_stock")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MoneyInStock implements Serializable {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 128)
    private String id;

    @Column(name = "year", unique = true, nullable = false)
    private Integer year;

    @Column(name = "inventory", columnDefinition = "decimal", nullable = false)
    private BigDecimal inventory = BigDecimal.ZERO;

    @Column(name = "note", columnDefinition = "nvarchar")
    private String note;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "create_by", columnDefinition = "nvarchar", length = 255)
    private String createBy;

    @Column(name = "update_by", columnDefinition = "nvarchar", length = 255)
    private String updateBy;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "update_at")
    private Instant updateAt;

}
