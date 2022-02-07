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
public class UpdatePaymentVoucherRequest {

    private String soldVoucherNo;

    private String voucherId;

    private String customerId;

    private Instant VoucherAt;

    private BigDecimal totalGoodsReturn;

    @Length(max = 255, message = "note must be less than 255 digits")
    private String note;

    @Length(max = 255, message = "updateByFullName must be less than 255 digits")
    private String updateByFullName;

    @Length(max = 255, message = "payer must be less than 255 digits")
    private String payer;

    private BigDecimal totalPaymentReceived;

    private String truckDriverId;

    private String truckDriverFullName;

    private String truckLicensePlateNumber;

    private String companyId;

    @Valid
    List<CreateGoodsInOutRequest> goodsInOut;
}
