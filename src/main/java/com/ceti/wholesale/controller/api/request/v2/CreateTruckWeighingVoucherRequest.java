package com.ceti.wholesale.controller.api.request.v2;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateTruckWeighingVoucherRequest {

    private String truckLicensePlateNumber;

    @NotNull(message = "voucherCode can not be null")
    private String voucherCode;

    @NotNull(message = "voucherAt can not be null")
    private Instant voucherAt;

    @NotNull(message = "factoryId can not be null")
    private String factoryId;

    @NotNull(message = "commandReferenceId can not be null")
    private String commandReferenceId;

    private String createdByFullName;
}
