package com.ceti.wholesale.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Table(name = "product_category")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    // @GeneratedValue(generator = "uuid")
    //@GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "name", columnDefinition = "nvarchar", nullable = false)
    private String name;

    @Column(name = "product_type_id", nullable = false)
    private String productTypeId;

    @Column(name = "is_active")
    private Boolean isActive = true;

}
