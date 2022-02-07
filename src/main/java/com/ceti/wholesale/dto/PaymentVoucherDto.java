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
public class PaymentVoucherDto {

	private String accNo;
	
    private String soldVoucherAccNo;
    
    private String soldDeliveryVoucherAccNo;
	
    private String no;

    private String id;

    private String soldVoucherNo;

    private String voucherId;

    private String customerId;

    private String soldDeliveryVoucherNo;

    private String voucherCode;

    private Instant voucherAt;

    private BigDecimal totalPaymentReceived;

    private BigDecimal totalGoodsReturn;

    private String note;

    private String createdByFullName;

    private Instant createdAt;

    private String updateByFullName;

    private Instant updateAt;

    private String payer;

    private String factoryId;

    private String truckLicensePlateNumber;

    private String truckDriverId;

    private String truckDriverFullName;

    private String companyId;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<GoodsInOutDto> goodsInOut;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private CustomerDto customer;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private CompanyDto company;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private TruckDriverDto truckDriver;
    
    private String deliveryVoucherNo;

    private String deliveryVoucherId;


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
