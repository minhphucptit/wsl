package com.ceti.wholesale.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Table(name = "customer")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    
    @Column(name = "code", columnDefinition = "nvarchar")
    private String code;

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(name = "name", columnDefinition = "nvarchar", nullable = false)
    private String name;

    @Column(name = "address", columnDefinition = "nvarchar")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "category")
    private String category;

    @Column(name = "group_id", nullable = false)
    private String groupId;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "vat")
    private BigDecimal vat;

    @Column(name = "region_id", columnDefinition = "varchar")
    private String regionId;

    @Column(name = "OWNER_company")
    private String ownerCompany;

    @Column(name = "is_wholesale_customer")
    private Boolean isWholesaleCustomer = false;

}

