package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateRecallVoucherRequest {

    private String truckDriverId;

    private String truckLicensePlateNumber;

    private BigDecimal totalGoodsReturn;

    private String note;

    private String updateByFullName;

    private Instant updateAt;

    private Instant voucherAt;

    private String truckDriverFullName;

    private List<CreateGoodsInOutRequest> goodsInOut;

}
