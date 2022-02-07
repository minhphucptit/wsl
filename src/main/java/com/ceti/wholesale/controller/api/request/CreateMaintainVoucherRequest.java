package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
public class CreateMaintainVoucherRequest {
    private String companyId;

    private String customerId;

    private String truckDriverId;

    private String truckLicensePlateNumber;

    private BigDecimal totalGoods;

    @NotNull(message = "type can not be null")
    private String type;

    private Instant voucherAt;

    private String note;

    private String createdByFullName;

    private String salesmanId;

    private String truckDriverFullName;

    private String salesmanFullName;

    private Boolean inFactory;

    @Valid
    private List<CreateGoodsInOutRequest> goodsInOut;
}
