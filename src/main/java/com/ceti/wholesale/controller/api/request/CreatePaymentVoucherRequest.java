package com.ceti.wholesale.controller.api.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreatePaymentVoucherRequest {

    private String soldVoucherNo;

    @NotNull(message = "voucherId can not be null")
    private String voucherId;

    private String companyId;

    @NotNull(message = "customerId can not be null")
    private String customerId;

    private String soldDeliveryVoucherNo;

    @NotNull(message = "totalGoodsReturn can not be null")
    private BigDecimal totalGoodsReturn;

    @NotNull(message = "voucherAt can not be null")
    private Instant voucherAt;

    private String note;

    @NotNull(message = "voucher_code can not be null")
    private String voucherCode;

    @Length(max = 255, message = "createdByFullName must be less than 255 digits")
    @NotNull(message = "createdByFullName can not be null")
    private String createdByFullName;

    @Length(max = 255, message = "payer must be less than 255 digits")
    private String payer;

    private BigDecimal totalPaymentReceived;

    private String truckDriverId;

    private String truckDriverFullName;

    private String truckLicensePlateNumber;
    
    private String deliveryVoucherNo;

    private String deliveryVoucherId;

    @Valid
    List<CreateGoodsInOutRequest> goodsInOut;
    
}
