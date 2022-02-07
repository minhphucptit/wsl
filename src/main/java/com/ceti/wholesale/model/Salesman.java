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
@Table(name = "salesman")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Salesman implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "abbreviated_name", nullable = false)
    private String abbreviatedName;

    @Column(name = "full_name", columnDefinition = "nvarchar", nullable = false)
    private String fullName;

    @Column(name = "address", columnDefinition = "nvarchar")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "is_active")
    private Boolean isActive = true;
}