package com.ceti.wholesale.controller.api.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import javax.validation.Valid;

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
public class UpdateSoldDeliveryVoucherRequest {

    private String companyId;

    private String customerId;

    private String truckDriverId;

    private String truckLicensePlateNumber;

    private BigDecimal totalGoods;

    private BigDecimal totalReceivable;

    private BigDecimal totalPaymentReceived;

    private BigDecimal totalGoodsReturn;

    private String salesmanId;

    private Instant VoucherAt;

    @Length(max = 255, message = "note must be less than 255 digits")
    private String note;

    @Length(max = 255, message = "updateByFullName must be less than 255 digits")
    private String updateByFullName;

    private String truckDriverFullName;

    @Valid
    private List<CreateGoodsInOutRequest> goodsInOut;
}
