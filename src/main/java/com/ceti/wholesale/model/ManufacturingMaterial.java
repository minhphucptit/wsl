package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "manufacturing_material")
@Entity
@Accessors(chain = true)
public class ManufacturingMaterial {

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "manufacturing_io_voucher_id", nullable = false,columnDefinition = "varchar")
    private String manufacturingIoVoucherId;

    @Column(name = "voucher_no",columnDefinition = "varchar")
    private String voucherNo;

    @Column(name = "product_id",columnDefinition = "varchar")
    private String productId;

    @Column(name = "product_name",columnDefinition = "nvarchar")
    private String productName;

    @Column(name = "voucher_code",columnDefinition = "varchar")
    private String voucherCode;

    @Column(name = "unit",columnDefinition = "varchar")
    private String unit;

    @Column(name = "note",columnDefinition = "nvarchar")
    private String note;

    @Column(name = "product_type",columnDefinition = "varchar")
    private String productType;

    @Column(name = "factory_id",columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "in_quantity")
    private BigDecimal inQuantity= BigDecimal.ZERO;

    @Column(name = "out_quantity")
    private BigDecimal outQuantity= BigDecimal.ZERO;

    @Column(name = "type",columnDefinition = "varchar")
    private String type;
}
