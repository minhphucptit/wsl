package com.ceti.wholesale.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VoucherDto {
	
    private String accNo;
    
    private String soldVoucherAccNo;
    
    private String soldDeliveryVoucherAccNo;
    
    private String deliveryVoucherAccNo;

    private String no;
   
    private String id;

    private String voucherCode;

    private Instant voucherAt;

    private String companyId;

    private String customerId;

    private String salesmanId;

    private String salesmanId2;

    private String truckDriverId;

    private String truckLicensePlateNumber;

    private BigDecimal totalPaymentReceived;

    private String payer;

    private String soldDeliveryVoucherNo;
    
    private String soldVoucherNo;

    private BigDecimal totalGoodsReturn;

    private BigDecimal totalGoods;

    private BigDecimal totalReceivable;

    private String note;

    private String createdByFullName;

    private Instant createdAt;

    private String updateByFullName;

    private Instant updateAt;

    private String factoryId;
    
    private String type;

    private String deliveryVoucherNo;

    private String deliveryVoucherId;

    private BigDecimal totalGas;

    private Boolean inFactory;

    private String paymentVoucherId;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private CompanyDto company;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private CustomerDto customer;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private TruckDriverDto truckDriver;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private TruckDto truck;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private SalesmanDto salesman;

    private String truckDriverFullName;

    private String salesmanFullName;

    private String salesmanFullName2;

    @JsonGetter("voucher_at")
    public Object getVoucherAt() {
        try {
            return voucherAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("created_at")
    public Object getCreateAt() {
        try {
            return createdAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("update_at")
    public Object getUpdateAt() {
        try {
            return updateAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }
}
