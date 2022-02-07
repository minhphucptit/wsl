package com.ceti.wholesale.model;

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
@Table(name = "gas_refueling_voucher")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GasRefuelingVoucher {
	
    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "no", unique = true, nullable = false, length = 32)
    private String no;
    
    @Column(name = "created_at")
    private Instant createdAt;
    
    @Column(name = "created_by_full_name", nullable = false, columnDefinition = "nvarchar(255)")
    private String createdByFullName;
    
    @Column(name = "update_at")
    private Instant updateAt;
    
    @Column(name = "update_by_full_name", nullable = false, columnDefinition = "nvarchar(255)")
    private String updateByFullName;
    
    @Column(name = "note", columnDefinition = "nvarchar(255)")
    private String note;

    @Column(name = "total_gas", nullable = false)
    private BigDecimal totalGas = BigDecimal.ZERO;
    
    @Column(name = "voucher_at")
    private Instant voucherAt;
    
    @Column(name = "voucher_code", nullable = false, length = 32)
    private String voucherCode;
    
    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;
}
