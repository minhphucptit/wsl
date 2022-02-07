package com.ceti.wholesale.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "product_price")
@NoArgsConstructor
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ProductPrice implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "buy_price")
    private BigDecimal buyPrice = BigDecimal.valueOf(0);

    @Column(name = "sale_price")
    private BigDecimal salePrice = BigDecimal.valueOf(0);

    @Column(name = "update_at", nullable = false)
    private Instant updateAt;

    @Column(name = "update_by_full_name", columnDefinition = "nvarchar", nullable = false)
    private String updateByFullName;

    @Column(name = "factory_id")
    private String factoryId;

    @Column(name = "product_id")
    private String productId;
}
