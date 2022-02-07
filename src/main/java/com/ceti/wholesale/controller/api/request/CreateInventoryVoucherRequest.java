package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateInventoryVoucherRequest {

    private String note;

    @NotNull(message = "voucherAt can not be null")
    private Instant voucherAt;

    @NotNull(message = "counter can not be null")
    private String counter;

    @NotNull(message = "create_by_full_name can not be null")
    private String createByFullName;

    @Valid
    List<CreateGoodsInventoryRequest> goodsInventories;

}
