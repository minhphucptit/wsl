package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "product_unit")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductUnit implements Serializable {

    @Id
    @Column(name = "id", columnDefinition = "varchar")
    private String id;

    @Column(name = "name", columnDefinition = "nvarchar")
    private String name;

    @Column(name = "sort", columnDefinition = "int")
    private int sort;

}
