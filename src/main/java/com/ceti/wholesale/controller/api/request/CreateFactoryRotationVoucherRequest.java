package com.ceti.wholesale.controller.api.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateFactoryRotationVoucherRequest {

    @NotNull(message = "voucherAt can not be null")
    private Instant voucherAt;

    @Length(max = 32, message = "companyExportId must be less than 32 digits")
    @NotNull(message = "companyExportId can not be null")
    private String companyExportId;

    @Length(max = 255, message = "companyExportName must be less than 255 digits")
    @NotNull(message = "companyExportName can not be null")
    private String companyExportName;

    @Length(max = 32, message = "companyImportId must be less than 32 digits")
    @NotNull(message = "companyImportId can not be null")
    private String companyImportId;

    @Length(max = 255, message = "companyImportName must be less than 255 digits")
    @NotNull(message = "companyImportName can not be null")
    private String companyImportName;


    @Length(max = 32, message = "truckDriverId must be less than 32 digits")
  //  @NotNull(message = "truckDriverId can not be null")
    private String truckDriverId;

    @Length(max = 32, message = "truckLicensePlateNumber must be less than 32 digits")
    private String truckLicensePlateNumber;

    @Length(max = 255, message = "createdByFullName must be less than 255 digits")
    @NotNull(message = "createdByFullName can not be null")
    private String createdByFullName;

    @Length(max = 255, message = "note must be less than 255 digits")
    private String note;

    private BigDecimal totalGoods = BigDecimal.ZERO;

    private String truckDriverFullName;

    @Valid
    private List<CreateGoodsInOutRequest> goodsInOut;

}
