package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateManufacturingIoVoucherRequest {

    private String voucherCode;

    private String note;

    private String updateByFullName;

    private Instant voucherAt;

    private List<CreateManufacturingMaterialRequest> manufacturingMaterialRequests;

}
