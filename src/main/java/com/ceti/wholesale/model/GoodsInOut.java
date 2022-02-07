package com.ceti.wholesale.model;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
public class GoodsInOut {
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "voucher_id", nullable = false, length = 32)
    private String voucherId;

    @Column(name = "voucher_no", nullable = false, length = 32)
    private String voucherNo;

    @Column(name = "product_id", nullable = false, length = 64)
    private String productId;

    @Column(name = "product_name", columnDefinition = "nvarchar(255)")
    private String productName;

    @Column(name = "note", columnDefinition = "nvarchar(255)")
    private String note;

    @Column(name = "product_type", length = 32)
    private String productType;

    @Column(name = "voucher_code", nullable = false, length = 32)
    private String voucherCode;

    @Column(name = "type", nullable = false, length = 32)
    private String type;

    @Column(name = "voucher_at")
    private Instant voucherAt;

    @Column(name = "in_quantity")
    private BigDecimal inQuantity = BigDecimal.valueOf(0);

    @Column(name = "out_quantity")
    private BigDecimal outQuantity = BigDecimal.valueOf(0);

    // cột số hàng bán được, giành riêng cho phiếu xuất xe bán, không dùng để tính toán
    @Column(name = "xbx_out_quantity")
    private BigDecimal xbxOutQuantity = BigDecimal.valueOf(0);

    // cột số hàng không bán được, giành riêng cho phiếu nhập thu hồi, không dùng để tính toán
    @Column(name = "nxe_in_quantity")
    private BigDecimal nxeInQuantity = BigDecimal.valueOf(0);

    // cột số hàng xuất kho theo xe(tổng bán được và không bán được), giành riêng cho phiếu xuất xe
    @Column(name = "xxe_out_quantity")
    private BigDecimal xxeOutQuantity = BigDecimal.valueOf(0);


    @Column(name = "price")
    private BigDecimal price = BigDecimal.valueOf(0);

    @Column(name = "discount")
    private BigDecimal discount = BigDecimal.valueOf(0);

    @Column(name = "unit", columnDefinition = "nvarchar(255)")
    private String unit;

    @Column(name = "is_main_product")
    private Boolean isMainProduct = true;

    @Column(name = "in_factory")
    private Boolean inFactory = true ;

    @Column(name = "customer_id", columnDefinition = "varchar")
    private String customerId;

    @Column(name = "company_id", columnDefinition = "varchar")
    private String companyId;

    @Column(name = "weight")
    private BigDecimal weight = BigDecimal.valueOf(0);

    @Column(name = "stt")
    private Integer stt =0;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;
    
    @Column(name = "truck_driver_id", columnDefinition = "varchar")
    private String truckDriverId;
    
    @Column(name = "truck_license_plate_number", columnDefinition = "varchar")
    private String truckLicensePlateNumber;

    @Column(name = "salesman_id", columnDefinition = "varchar")
    private String salesmanId;

    @Column(name = "salesman_id_2", columnDefinition = "varchar")
    private String salesmanId2;
    
    @Column(name = "quantity_at")
    private Instant quantityAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @Column(name = "update_by_full_name",columnDefinition = "nvarchar(255)")
    private String updateByFullName;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "create_by_full_name",columnDefinition = "nvarchar(255)")
    private String createByFullName;
}
