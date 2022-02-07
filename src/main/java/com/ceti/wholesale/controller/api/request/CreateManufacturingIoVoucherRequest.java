package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateManufacturingIoVoucherRequest {

    @Length(max = 32, message = "voucherCode must be less than 32 digits")
    private String voucherCode;

    @NotNull(message = "voucherAt can not be null")
    private Instant voucherAt;

    @Length(max = 255, message = "createdByFullName must be less than 255 digits")
    private String createdByFullName;

    @Length(max = 255, message = "note must be less than 255 digits")
    private String note;

    private List<CreateManufacturingMaterialRequest> manufacturingMaterialRequests;

}
