package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "customer_category")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCategory {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "name", nullable = false,columnDefinition = "nvarchar")
    private String name;

}
