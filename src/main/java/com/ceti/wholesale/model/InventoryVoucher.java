package com.ceti.wholesale.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Table(name = "inventory_voucher")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class InventoryVoucher implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "no", columnDefinition = "varchar", nullable = false)
    private String no;

    @Column(name = "voucher_at", nullable = false)
    private Instant voucherAt;

    @Column(name = "create_at", nullable = false)
    private Instant createAt;

    @Column(name = "create_by_full_name", columnDefinition = "nvarchar(255)")
    private String createByFullName;

    @Column(name = "note", columnDefinition = "nvarchar(255)")
    private String note;

    @Column(name = "voucher_code")
    private String voucherCode;

    @Column(name = "factory_id")
    private String factoryId;

    @Column(name = "counter", columnDefinition = "nvarchar(255)")
    private String counter;
    
    @Column(name = "update_by_full_name", columnDefinition = "nvarchar(255)")
    private String updateByFullName;

    @Column(name = "update_at")
    private Instant updateAt;
    
    @Column(name = "factory_import_voucher_id")
    private String factoryImportVoucherId;

    @Column(name = "total_lpg")
    private BigDecimal totalLpg = BigDecimal.ZERO;


}
