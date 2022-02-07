package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateSoldVoucherRequest {

    @NotNull(message = "companyId can not be null")
    private String companyId;

    @NotNull(message = "customerId can not be null")
    private String customerId;

    private String truckDriverId;

    private String truckLicensePlateNumber;

    @NotNull(message = "totalGood can not be null")
    private BigDecimal totalGoods;

    @NotNull(message = "totalReceivable can not be null")
    private BigDecimal totalReceivable;

    @NotNull(message = "totalPaymentReceived can not be null")
    private BigDecimal totalPaymentReceived;

    @NotNull(message = "totalGoodsReturn can not be null")
    private BigDecimal totalGoodsReturn;

    @NotNull(message = "voucherAt can not be null")
    private Instant voucherAt;

    private String note;

    private String truckDriverFullName;

    private String salesmanFullName;

    @Length(max = 255, message = "createdByFullName must be less than 255 digits")
    @NotNull(message = "createdByFullName can not be null")
    private String createdByFullName;

    @Length(max = 255, message = "salesMan must be less than 255 digits")
    //@NotNull(message = "salesMan can not be null")
    private String salesmanId;

    private String salesmanId2;

    private String salesmanFullName2;

    @Valid
    private List<CreateGoodsInOutRequest> goodsInOut;

}

