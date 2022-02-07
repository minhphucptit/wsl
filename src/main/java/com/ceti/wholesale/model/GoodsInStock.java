package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Table(name = "goods_in_stock")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInStock {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 128)
    private String id;
    @Column(name = "year", nullable = false)
    private Integer year;
    @Column(name = "company_id", nullable = false, length = 32)
    private String companyId;
    @Column(name = "product_id", nullable = false, length = 32)
    private String productId;
    @Column(name = "inventory", nullable = false)
    private BigDecimal inventory = BigDecimal.valueOf(0);
    @Column(name = "create_by", nullable = false, columnDefinition = "nvarchar", length = 255)
    private String createBy;
    @Column(name = "update_by", nullable = false, columnDefinition = "nvarchar", length = 255)
    private String updateBy;
    @Column(name = "create_at", nullable = false)
    private Instant createAt;
    @Column(name = "update_at", nullable = false)
    private Instant updateAt;
    @Column(name = "note", nullable = false, columnDefinition = "nvarchar", length = 255)
    private String note;
    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

}
