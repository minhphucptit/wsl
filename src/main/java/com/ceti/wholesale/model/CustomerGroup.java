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
@Table(name = "customer_group")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerGroup implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "name", columnDefinition = "nvarchar")
    private String name;

    @Column(name = "note", columnDefinition = "nvarchar")
    private String note;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "is_active")
    private Boolean isActive = true;

}
