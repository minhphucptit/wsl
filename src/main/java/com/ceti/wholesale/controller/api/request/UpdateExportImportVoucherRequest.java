package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateExportImportVoucherRequest {
    @Valid
    private UpdateImportVoucherRequest updateImportVoucherRequest;
    @Valid
    private UpdateExportVoucherRequest updateExportVoucherRequest;

    private Boolean updateImportVoucher;

    private Boolean updateExportVoucher;
    @Valid
    private CreateImportVoucherRequest createImportVoucherRequest;
    @Valid
    private CreateExportVoucherRequest createExportVoucherRequest;

}
