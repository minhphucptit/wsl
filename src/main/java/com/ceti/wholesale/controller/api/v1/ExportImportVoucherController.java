package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateExportImportVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateExportImportVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateExportVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ExportVoucherDto;
import com.ceti.wholesale.dto.ExportImportVoucherDto;
import com.ceti.wholesale.dto.FactoryExportVoucherDto;
import com.ceti.wholesale.dto.ImportVoucherDto;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ExportImportVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@Validated
public class ExportImportVoucherController {

    @Autowired
    private ExportImportVoucherService exportImportVoucherService;

    @Autowired
    private GoodsInOutService goodsInOutService;

    // get list import/export voucher
    @GetMapping(value = "/export-import-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ExportImportVoucherDto>> getAllImportVoucher(
            @RequestParam MultiValueMap<String, String> where,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        Page<ExportImportVoucherDto> page = exportImportVoucherService.getAllByCondition(where, pageable, factoryId);
        ResponseBodyDto<ExportImportVoucherDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // get detail export import vouchers
    @GetMapping(value = "/export-import-vouchers/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ExportImportVoucherDto>> getAllImportVoucherDetail(
            @RequestParam(value = "import_voucher_id", required = false) String importVoucherId,
            @RequestParam(value = "export_voucher_id", required = false) String exportVoucherId,
            @RequestHeader(name = "department_id") String factory_id) {
    	ExportImportVoucherDto exportImportVoucherDto = exportImportVoucherService.getDetail(importVoucherId, exportVoucherId);
        ResponseBodyDto<ExportImportVoucherDto> res = new ResponseBodyDto<>(exportImportVoucherDto,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create import-export voucher
    @PostMapping(value = "/export-import-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ExportImportVoucherDto>> createExportImportVoucher(
            @Valid @RequestBody CreateExportImportVoucherRequest request,
            @RequestHeader(name = "department_id") String factoryId) {

        ExportImportVoucherDto exportImportVoucherDto = exportImportVoucherService.createImportExportVoucher(request, factoryId);
        ResponseBodyDto<ExportImportVoucherDto> res = new ResponseBodyDto<>(exportImportVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update import-export voucher
    @PatchMapping(value = "/export-import-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ExportImportVoucherDto>> updateExportImportVoucher(
            @Valid @RequestBody UpdateExportImportVoucherRequest request,
            @RequestHeader(name = "department_id") String factory_id
            ){

        ExportImportVoucherDto exportImportVoucherDto = exportImportVoucherService.
                updateImportExportVoucher(factory_id, request);

        ResponseBodyDto<ExportImportVoucherDto> res = new ResponseBodyDto<>(exportImportVoucherDto,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/export-import-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteExportImportVoucher(
            @RequestParam(value = "import_voucher_id", required = false) String importVoucherId,
            @RequestParam(value = "export_voucher_id", required = false) String exportVoucherId
    ) {
        exportImportVoucherService.deleteExportImportVoucher(importVoucherId, exportVoucherId);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


}
