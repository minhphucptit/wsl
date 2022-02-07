package com.ceti.wholesale.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "truck_weighing_voucher")
public class TruckWeighingVoucher {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "no", unique = true, nullable = false, length = 32)
    private String no;

    @Column(name = "voucher_code", nullable = false, length = 32)
    private String voucherCode;

    @Column(name = "voucher_at")
    private Instant voucherAt;

    @Column(name = "company_id", nullable = false, length = 32)
    private String companyId;

    @Column(name = "product_id", nullable = false, length = 32)
    private String productId;

    @Column(name = "customer_id", nullable = false, length = 32)
    private String customerId;

    @Column(name = "truck_driver_id", nullable = false, length = 32)
    private String truckDriverId;

    @Column(name = "truck_license_plate_number", length = 32)
    private String truckLicensePlateNumber;

    @Column(name = "weighing_result_1")
    private BigDecimal weighingResult1 = BigDecimal.valueOf(0);

    @Column(name = "weighing_result_2")
    private BigDecimal weighingResult2 = BigDecimal.valueOf(0);

    @Column(name = "weighing_time_1")
    private Instant weighingTime1;

    @Column(name = "weighing_time_2")
    private Instant weighingTime2;

    @Column(name = "weighing_result_final")
    private BigDecimal weighingResultFinal = BigDecimal.valueOf(0);

    @Column(name = "pressure")
    private BigDecimal pressure = BigDecimal.valueOf(0);

    @Column(name = "note", columnDefinition = "nvarchar(255)")
    private String note;

    @Column(name = "created_by_full_name", nullable = false, columnDefinition = "nvarchar(255)")
    private String createdByFullName;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "update_by_full_name", nullable = false, columnDefinition = "nvarchar(255)")
    private String updateByFullName;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "product_name", columnDefinition = "nvarchar(255)")
    private String productName;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "command_reference_id", columnDefinition = "varchar")
    private String commandReferenceId;

    @Column(name = "truck_driver_full_name", columnDefinition = "nvarchar(255)")
    private String truckDriverFullName;
}
