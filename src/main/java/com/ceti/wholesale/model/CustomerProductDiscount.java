package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Table(name = "customer_product_discount")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProductDiscount {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 128)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "customer_id", nullable = false, length = 32)
    private String customerId;

    @Column(name = "product_id", nullable = false, length = 32)
    private String productId;

    @Column(name = "factory_id", nullable = false, length = 32)
    private String factoryId;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "update_by",columnDefinition = "nvarchar")
    private String updateBy;
}
