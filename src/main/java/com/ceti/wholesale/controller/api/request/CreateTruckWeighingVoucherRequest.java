package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateTruckWeighingVoucherRequest {

    @NotNull(message = "companyId can not be null")
    private String companyId;

    @NotNull(message = "customerId can not be null")
    private String customerId;

    @NotNull(message = "truckDriverId can not be null")
    private String truckDriverId;

    private String truckLicensePlateNumber;

    @NotNull(message = "productId can not be null")
    private String productId;

    @NotNull(message = "voucherCode can not be null")
    private String voucherCode;

    @NotNull(message = "voucherAt can not be null")
    private Instant voucherAt;

    private String note;

    @Length(max = 255, message = "createdByFullName must be less than 255 digits")
    @NotNull(message = "createdByFullName can not be null")
    private String createdByFullName;

    private BigDecimal weighingResult1;

    private BigDecimal pressure;

    private String productName;

    private String truckDriverFullName;

    private UpdateTruckWeighingVoucherRequest truckWeighingVoucher;
}
