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
public class CreateRecallVoucherRequest {

    private String truckDriverId;

    private String truckLicensePlateNumber;

    @NotNull(message = "deliveryVoucherNo can not be null")
    private String deliveryVoucherNo;

    @NotNull(message = "deliveryVoucherId can not be null")
    private String deliveryVoucherId;

    @NotNull(message = "totalGoodsReturn can not be null")
    private BigDecimal totalGoodsReturn;

    @NotNull(message = "company_id can not be null")
    private String companyId;

    @NotNull(message = "voucherAt can not be null")
    private Instant voucherAt;

    private String note;

    @Length(max = 255, message = "createById must be less than 255 digits")
    @NotNull(message = "createdByFullName can not be null")
    private String createdByFullName;

    private String truckDriverFullName;

    @Valid
    List<CreateGoodsInOutRequest> goodsInOut;

}
