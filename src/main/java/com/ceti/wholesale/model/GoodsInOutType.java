package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "goods_in_out_type")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInOutType {
    @Id
    @Column(name = "id", columnDefinition = "varchar")
    private String id;

    @Column(name = "code", columnDefinition = "varchar")
    private String code;

    @Column(name = "name", columnDefinition = "nvarchar")
    private String name;

    @Column(name = "DESCRIPTION", columnDefinition = "nvarchar")
    private String description;
    
    @Column(name = "product_type", columnDefinition = "varchar")
    private String productType;

}
