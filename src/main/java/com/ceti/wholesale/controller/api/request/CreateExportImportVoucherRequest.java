package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateExportImportVoucherRequest {
    @Valid
    private CreateImportVoucherRequest createImportVoucherRequest;
    @Valid
    private CreateExportVoucherRequest createExportVoucherRequest;

    private Boolean createImportVoucher;

    private Boolean createExportVoucher;


}
