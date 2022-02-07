package com.ceti.wholesale.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

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
public class SoldDeliveryVoucherDto {

    private String accNo;

    private String no;

    private String id;

    private String deliveryVoucherNo;

    private String deliveryVoucherId;

    private String voucherCode;

    private Instant voucherAt;

    private String truckDriverId;

    private String truckLicensePlateNumber;

    private BigDecimal totalGoods;

    private String note;

    private String createdByFullName;

    private Instant createdAt;

    private String updateByFullName;

    private Instant updateAt;

    private String companyId;

    private String customerId;

    private BigDecimal totalReceivable;

    private BigDecimal totalPaymentReceived;

    private BigDecimal totalGoodsReturn;

    private String factoryId;

    private String salesmanId;

    private String truckDriverFullName;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<GoodsInOutDto> goodsInOut;

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

    private String paymentVoucherId;
    
    private String salesmanFullName;

}
