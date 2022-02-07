package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "cylinder_debt")
@Accessors(chain = true)
@NoArgsConstructor
public class CylinderDebt {

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "voucher_id", nullable = false, length = 32)
    private String voucherId;

    @Column(name = "product_id", nullable = false, length = 32)
    private String productId;

    @Column(name = "voucher_code", nullable = false, length = 32)
    private String voucherCode;

    @Column(name = "voucher_at", nullable = false, length = 32)
    private Instant voucherAt;

    @Column(name = "voucher_no", nullable = false, length = 32)
    private String voucherNo;

    @Column(name = "in_quantity", nullable = false, length = 32)
    private BigDecimal inQuantity;

    @Column(name = "out_quantity", nullable = false, length = 32)
    private BigDecimal outQuantity;

    @Column(name = "customer_id", nullable = false, length = 32)
    private String customerId;

    @Column(name = "company_id", nullable = false, length = 32)
    private String companyId;

    @Column(name = "note", columnDefinition = "nvarchar(255)")
    private String note;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "created_by", columnDefinition = "nvarchar(255)")
    private String createdBy;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "update_by", columnDefinition = "nvarchar(255)")
    private String updateBy;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "goods_in_out_type")
    private String goodsInOutType;

    public CylinderDebt(String voucherId, String productId, String voucherCode, Instant voucherAt,
                            String voucherNo, BigDecimal inQuantity, BigDecimal outQuantity, String customerId,
                            String companyId, String note, Instant createdAt, String createdBy, Instant updateAt,
                            String updateBy, String factoryId){
        this.voucherId=voucherId;
        this.productId=productId;
        this.voucherCode=voucherCode;
        this.voucherAt=voucherAt;
        this.voucherNo=voucherNo;
        this.inQuantity=inQuantity;
        this.outQuantity=outQuantity;
        this.customerId=customerId;
        this.companyId=companyId;
        this.note=note;
        this.createdAt=createdAt;
        this.createdBy=createdBy;
        this.updateAt=updateAt;
        this.updateBy=updateBy;
        this.factoryId=factoryId;
    }
}
