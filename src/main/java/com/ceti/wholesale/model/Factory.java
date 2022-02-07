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
@Table(name = "factory")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Factory implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "name", columnDefinition = "nvarchar")
    private String name;

    @Column(name = "name_on_voucher", columnDefinition = "nvarchar")
    private String nameOnVoucher;

    @Column(name = "address_on_voucher", columnDefinition = "nvarchar")
    private String addressOnVoucher;

    @Column(name = "director_full_name", columnDefinition = "nvarchar")
    private String directorFullName;

    @Column(name = "accountant_full_name", columnDefinition = "nvarchar")
    private String accountantFullName;

    @Column(name = "code")
    private String code;

}
