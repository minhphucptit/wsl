package com.ceti.wholesale.model;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class PaymentVoucher {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "no", unique = true, nullable = false, length = 32)
    private String no;

    @Column(name = "sold_voucher_no", nullable = false, length = 32)
    private String soldVoucherNo;

    @Column(name = "voucher_id", nullable = false, length = 32)
    private String voucherId;

    @Column(name = "sold_delivery_voucher_no", nullable = false, length = 32)
    private String soldDeliveryVoucherNo;

    @Column(name = "voucher_code", nullable = false, length = 32)
    private String voucherCode;

    @Column(name = "customer_id", nullable = false, length = 32)
    private String customerId;

    @Column(name = "voucher_at")
    private Instant voucherAt;

    @Column(name = "total_goods_return", nullable = false)
    private BigDecimal totalGoodsReturn = BigDecimal.valueOf(0);

    @Column(name = "total_payment_received", nullable = false)
    private BigDecimal totalPaymentReceived = BigDecimal.valueOf(0);

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

    @Column(name = "payer")
    private String payer;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "truck_license_plate_number", length = 32)
    private String truckLicensePlateNumber;

    @Column(name = "truck_driver_id", length = 32)
    private String truckDriverId;

    @Column(name = "truck_driver_full_name", columnDefinition = "nvarchar(255)")
    private String truckDriverFullName;

    @Column(name = "company_id")
    private String companyId;
    
    @Column(name = "delivery_voucher_no", columnDefinition = "varchar")
    private String deliveryVoucherNo;

    @Column(name = "delivery_voucher_id", columnDefinition = "varchar")
    private String deliveryVoucherId;

}
