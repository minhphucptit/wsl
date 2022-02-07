package com.ceti.wholesale.controller.api.request.v2;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateTruckWeighingVoucherRequest {

    private BigDecimal weighingResult1;

    private Instant weighingTime1;

    private Instant weighingTime2;

    private BigDecimal weighingResult2;

    private String createdByFullName;

    private String voucherCode;

}
