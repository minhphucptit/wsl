package com.ceti.wholesale.controller.api.request;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateTruckWeighingVoucherRequest {

    private Instant voucherAt;

    private Instant weighingTime2;

    private BigDecimal weighingResult1;

    private Instant weighingTime1;

    private BigDecimal weighingResult2;

    private BigDecimal pressure;

    private BigDecimal payment;

    private String productName;

    private String unit;

    private BigDecimal price;

    private String type;

    private String companyId;

    private String customerId;

    private String truckDriverId;

    private String truckLicensePlateNumber;

    private String productId;

    private String voucherCode;

    private String note;

    private String createdByFullName;

    private String truckDriverFullName;

}
