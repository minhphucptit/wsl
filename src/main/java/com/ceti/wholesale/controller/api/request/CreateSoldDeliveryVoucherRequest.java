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
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateSoldDeliveryVoucherRequest {

    @NotNull(message = "deliveryVoucherNo can not be null")
    private String deliveryVoucherNo;

    @NotNull(message = "deliveryVoucherId can not be null")
    private String deliveryVoucherId;

    @NotNull(message = "companyId can not be null")
    private String companyId;

    @NotNull(message = "customerId can not be null")
    private String customerId;

    private String truckDriverId;

    private String truckLicensePlateNumber;

    @NotNull(message = "totalGood can not be null")
    private String totalGoods;

    @NotNull(message = "totalReceivable can not be null")
    private String totalReceivable;

    @NotNull(message = "totalPaymentReceived can not be null")
    private String totalPaymentReceived;

    @NotNull(message = "totalGoodsReturn can not be null")
    private String totalGoodsReturn;

    @NotNull(message = "voucherAt can not be null")
    private Instant voucherAt;

    private String note;

    @Length(max = 255, message = "createdByFullName must be less than 255 digits")
    @NotNull(message = "createdByFullName can not be null")
    private String createdByFullName;

    private String truckDriverFullName;

    @Valid
    private List<CreateGoodsInOutRequest> goodsInOut;

    private String salesmanId;
    
    private String salesmanFullName;
}
