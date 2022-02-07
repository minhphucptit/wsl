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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer_receipt_payment_voucher")
@Entity
@Accessors(chain = true)
public class CustomerReceiptPaymentVoucher implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false,  length = 64)
    private String id;

    @Column(name = "no", nullable = false)
    private String no;

    @Column(name = "customer_id", columnDefinition = "varchar", nullable = false)
    private String customerId;

    @Column(name = "customer_full_name", columnDefinition = "nvarchar", nullable = false)
    private String customerFullName;

    @Column(name = "voucher_at", nullable = false)
    private Instant voucherAt;

    @Column(name = "payer", columnDefinition = "nvarchar")
    private String payer;

    @Column(name = "payer_method", columnDefinition = "bit", nullable = false)
    private Boolean payerMethod;

    @Column(name = "address", columnDefinition = "nvarchar")
    private String address;

    @Column(name = "reason", columnDefinition = "nvarchar")
    private String reason;

    @Column(name = "note", columnDefinition = "nvarchar")
    private String note;

    @Column(name = "category", columnDefinition = "varchar", nullable = false)
    private String category;

    @Column(name = "voucher_code")
    private String voucherCode;

    @Column(name = "create_at", nullable = false)
    private Instant createAt;

    @Column(name = "create_by", columnDefinition = "nvarchar", nullable = false)
    private String createBy;

    @Column(name = "update_at", nullable = false)
    private Instant updateAt;

    @Column(name = "update_by", columnDefinition = "nvarchar")
    private String updateBy;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "collect_money", columnDefinition = "varchar")
    private BigDecimal collectMoney = BigDecimal.valueOf(0);

    @Column(name = "spend_money", columnDefinition = "varchar")
    private BigDecimal spendMoney = BigDecimal.valueOf(0);
}
