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
public class CreateFactoryExportVoucherRequest {
    @NotNull(message = "companyId can not be null")
    private String companyId;

    @NotNull(message = "customerId can not be null")
    private String customerId;

   // @NotNull(message = "truckDriverId can not be null")
    private String truckDriverId;

   // @NotNull(message = "truckLicensePlateNumber can not be null")
    private String truckLicensePlateNumber;

    @NotNull(message = "totalGood can not be null")
    private BigDecimal totalGoods;

    @NotNull(message = "totalPaymentReceived can not be null")
    private BigDecimal totalPaymentReceived;

    @NotNull(message = "voucherAt can not be null")
    private Instant voucherAt;

    private String note;

    @Length(max = 255, message = "createdByFullName must be less than 255 digits")
    @NotNull(message = "createdByFullName can not be null")
    private String createdByFullName;

    @Length(max = 255, message = "salesMan must be less than 255 digits")
    private String salesmanId;

    private String truckDriverFullName;

    private String salesmanFullName;
    
    private Instant quantityAt;

    private Boolean inFactory;

    @Valid
    private List<CreateGoodsInOutRequest> goodsInOut;
}
