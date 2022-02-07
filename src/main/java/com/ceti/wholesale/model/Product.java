package com.ceti.wholesale.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.ceti.wholesale.model.compositeid.ProductId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Table(name = "product")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ProductId.class)
public class Product implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Id
    @Column(name = "factory_id", unique = true, nullable = false)
    private String factoryId;

    @Column(name = "name", columnDefinition = "nvarchar", nullable = false)
    private String name;

    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "weight")
    private BigDecimal weight = BigDecimal.valueOf(0);

    @Column(name = "buy_price")
    private BigDecimal buyPrice = BigDecimal.valueOf(0);

    @Column(name = "sale_price")
    private BigDecimal salePrice = BigDecimal.valueOf(0);

    @Column(name = "cylinder_category")
    private BigDecimal cylinderCategory = BigDecimal.valueOf(0);

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "reference_product_id", nullable = false)
    private String referenceProductId;

    @Column(name = "type")
    private String type;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "brand_id")
    private String brandId;

    @Column(name = "color_id")
    private String colorId;
}
