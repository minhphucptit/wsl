package com.ceti.wholesale.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

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
@Table(name = "goods_inventory")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInventory implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "voucher_id", columnDefinition = "varchar", nullable = false)
    private String voucherId;

    @Column(name = "voucher_no", columnDefinition = "varchar", nullable = false)
    private String voucherNo;

    @Column(name = "product_id", columnDefinition = "varchar", nullable = false)
    private String productId;

    @Column(name = "product_name", columnDefinition = "nvarchar", nullable = false)
    private String productName;

    @Column(name = "voucher_code", columnDefinition = "varchar", nullable = false)
    private String voucherCode;

    @Column(name = "unit", columnDefinition = "varchar", nullable = false)
    private String unit;

    @Column(name = "product_type", columnDefinition = "varchar", nullable = false)
    private String productType;

    @Column(name = "weight")
    private BigDecimal weight = BigDecimal.valueOf(0);

    @Column(name = "inventory")
    private BigDecimal inventory = BigDecimal.valueOf(0);

    @Column(name = "factory_id", columnDefinition = "nvarchar", nullable = false)
    private String factoryId;
    
    @Column(name = "voucher_at")
    private Instant voucherAt;
    
    @Column(name = "product_status_id", columnDefinition = "varchar")
    private String productStatusId;

    @Column(name = "company_id",columnDefinition = "varchar")
    private String companyId;
    
    @Column(name = "stt")
    private int stt;

}
