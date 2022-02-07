package com.ceti.wholesale.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
public class ExportVoucher {
    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "no", unique = true, nullable = false, length = 32)
    private String no;

    @Column(name = "company_id", nullable = false, length = 32)
    private String companyId;

    @Column(name = "voucher_at", nullable = false, length = 32)
    private Instant voucherAt;

    @Column(name = "customer_id", nullable = false, length = 32)
    private String customerId;

    @Column(name = "truck_driver_id", nullable = false, length = 32)
    private String truckDriverId;

    @Column(name = "truck_license_plate_number", length = 32)
    private String truckLicensePlateNumber;

    @Column(name = "total_receivable", nullable = false)
    private BigDecimal totalReceivable = BigDecimal.valueOf(0);

    @Column(name = "total_goods", nullable = false)
    private BigDecimal totalGoods = BigDecimal.valueOf(0);

    @Column(name = "note", columnDefinition = "nvarchar(255)")
    private String note;

    @Column(name = "created_by_full_name", columnDefinition = "nvarchar(255)")
    private String createdByFullName;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "update_by_full_name", columnDefinition = "nvarchar(255)")
    private String updateByFullName;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "truck_driver_full_name", columnDefinition = "nvarchar(255)")
    private String truckDriverFullName;

    @Column(name = "voucher_code", columnDefinition = "varchar")
    private String voucherCode;

    @Column(name = "salesman_id", length = 32)
    private String salesmanId;

    @Column(name = "salesman_full_name", columnDefinition = "nvarchar(255)")
    private String salesmanFullName;
}
