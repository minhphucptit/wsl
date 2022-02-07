package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

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
public class CreateReturnVoucherRequest {

    private String truckDriverId;

    private String truckLicensePlateNumber;

    @NotNull(message = "company_id can not be null")
    private String companyId;

    @NotNull(message = "totalGood can not be null")
    private BigDecimal totalGoods;

    private String note;

    @NotNull(message = "voucherAt can not be null")
    private Instant voucherAt;

    @Length(max = 255, message = "created_by_full_name must be less than 255 digits")
    @NotNull(message = "created_by_full_name can not be null")
    private String createdByFullName;

    private String truckDriverFullName;
    
    private String deliveryVoucherNo;

    @Valid
    List<CreateGoodsInOutRequest> goodsInOut;

}
