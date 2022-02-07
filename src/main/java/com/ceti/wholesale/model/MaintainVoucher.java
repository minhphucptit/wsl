package com.ceti.wholesale.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
public class MaintainVoucher {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "no", unique = true)
    private String no;

    @Column(name = "voucher_code")
    private String voucherCode;

    @Column(name = "voucher_at")
    private Instant voucherAt;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "salesman_id")
    private String salesmanId;

    @Column(name = "truck_driver_id")
    private String truckDriverId;

    @Column(name = "truck_license_plate_number")
    private String truckLicensePlateNumber;

    @Column(name = "type")
    private String type;

    @Column(name = "in_factory")
    private Boolean inFactory;

    @Column(name = "total_goods")
    private BigDecimal totalGoods = BigDecimal.ZERO;

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

    @Column(name = "factory_id")
    private String factoryId;

    @Column(name = "truck_driver_full_name", columnDefinition = "nvarchar(255)")
    private String truckDriverFullName;

    @Column(name = "salesman_full_name", columnDefinition = "nvarchar(255)")
    private String salesmanFullName;
}
