package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateImportVoucherRequest {
    private String id;

    private Instant voucherAt;

    private String companyId;

    private String customerId;

    private String truckDriverId;

    private String truckLicensePlateNumber;

    private BigDecimal totalGoods;

    private BigDecimal totalReceivable;

    @Length(max = 255, message = "note must be less than 255 digits")
    private String note;

    @Length(max = 255, message = "updateByFullName must be less than 255 digits")
    private String updateByFullName;

    private String truckDriverFullName;

    @Length(max = 255, message = "salesMan must be less than 255 digits")
    private String salesmanId;

    private String salesmanFullName;

    @Valid
    List<CreateGoodsInOutRequest> goodsInOut;
}
