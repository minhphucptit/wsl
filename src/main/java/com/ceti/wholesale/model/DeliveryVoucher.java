package com.ceti.wholesale.model;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class DeliveryVoucher {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "no", unique = true, nullable = false, length = 32)
    private String no;

    @Column(name = "voucher_code", nullable = false, length = 32)
    private String voucherCode;

    @Column(name = "voucher_at")
    private Instant voucherAt;

    @Column(name = "truck_driver_id", nullable = false, length = 32)
    private String truckDriverId;

    @Column(name = "truck_license_plate_number", length = 32)
    private String truckLicensePlateNumber;

    @Column(name = "total_goods", nullable = false)
    private BigDecimal totalGoods = BigDecimal.valueOf(0);

    @Column(name = "note", columnDefinition = "nvarchar(255)")
    private String note;

    @Column(name = "created_by_full_name", nullable = false, columnDefinition = "nvarchar(255)")

    private String createdByFullName;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "company_id", nullable = false, length = 32)
    private String companyId;

    @Column(name = "update_by_full_name", nullable = false, columnDefinition = "nvarchar(255)")
    private String updateByFullName;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "truck_driver_full_name", columnDefinition = "nvarchar(255)")
    private String truckDriverFullName;
    
    @Column(name = "salesman_id")
    private String salesmanId;
    
    @Column(name = "salesman_full_name", columnDefinition = "nvarchar(255)")
    private String salesmanFullName;
}
