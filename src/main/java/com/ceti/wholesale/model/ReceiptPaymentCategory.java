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
@Table(name = "receipt_payment_category")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptPaymentCategory implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "name", columnDefinition = "nvarchar", nullable = false)
    private String name;

    @Column(name = "type", columnDefinition = "bit", nullable = false)
    private Boolean type;

    @Column(name = "is_active")
    private Boolean isActive = true;

}
