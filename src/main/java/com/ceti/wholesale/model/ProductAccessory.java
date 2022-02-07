package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Table(name = "product_accessory")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class    ProductAccessory {

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "main_product_id", nullable = false, length = 32)
    private String mainProductId;

    @Column(name = "sub_product_id", nullable = false, length = 32)
    private String subProductId;

    @Column(name = "sub_product_quantity", nullable = false, length = 32)
    private BigDecimal subProductQuantity = BigDecimal.valueOf(0);

    @Column(name = "sub_product_name", columnDefinition = "nvarchar", length = 32)
    private String subProductName;

    @Column(name = "sub_product_type")
    private String subProductType;

    @Column(name = "factory_id", length = 32)
    private String factoryId;

}
