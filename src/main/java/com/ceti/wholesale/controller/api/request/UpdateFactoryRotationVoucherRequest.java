package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
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
public class UpdateFactoryRotationVoucherRequest {

    private Instant voucherAt;

    @Length(max = 32, message = "companyExportId must be less than 32 digits")
    private String companyExportId;

    @Length(max = 255, message = "companyExportName must be less than 255 digits")
    private String companyExportName;

    @Length(max = 32, message = "companyImportId must be less than 32 digits")
    private String companyImportId;

    @Length(max = 255, message = "companyImportName must be less than 255 digits")
    private String companyImportName;

    @Length(max = 32, message = "truckDriverId must be less than 32 digits")
    private String truckDriverId;

    @Length(max = 32, message = "truckLicensePlateNumber must be less than 32 digits")
    private String truckLicensePlateNumber;

    @Length(max = 255, message = "note must be less than 255 digits")
    private String note;

    private BigDecimal totalGoods = BigDecimal.ZERO;

    @Length(max = 255, message = "updateByFullName must be less than 255 digits")
    private String updateByFullName;

    private String truckDriverFullName;

    @Valid
    private List<CreateGoodsInOutRequest> goodsInOut;
}
