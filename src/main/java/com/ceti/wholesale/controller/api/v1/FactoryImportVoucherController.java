package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateFactoryImportExportVoucherRequestFromWarehouse;
import com.ceti.wholesale.controller.api.request.CreateFactoryImportVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateFactoryImportVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.FactoryImportVoucherDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.service.FactoryImportVoucherService;
import com.ceti.wholesale.service.GoodsInOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/v1")
@Validated
public class FactoryImportVoucherController {

    @Autowired
    private FactoryImportVoucherService factoryImportVoucherService;

    @Autowired
    private GoodsInOutService goodsInOutService;

    @Autowired
    private VoucherMapper voucherMapper;

    // get list factory import voucher
    @GetMapping(value = "/factory-import-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<VoucherDto>> getAllFactoryImportVoucher(
            @RequestParam HashMap<String, String> where,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        where.put("factory_id", factoryId);
        String tableName = "factory_import_voucher";
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, tableName);
        List<VoucherDto> list = voucherMapper.getList(where, pagingStr,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        long totalItems = voucherMapper.countList(where,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        ResponseBodyDto<VoucherDto> res = new ResponseBodyDto<>(list,
                ResponseCodeEnum.R_200, "OK",totalItems);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create factory import voucher
    @PostMapping(value = "/factory-import-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<FactoryImportVoucherDto>> createFactoryImportVoucher(
            @Valid @RequestBody CreateFactoryImportVoucherRequest request,
            @RequestHeader(name = "department_id") String factoryId) {

        FactoryImportVoucherDto factoryImportVoucher = factoryImportVoucherService.createFactoryImportVoucher(request, factoryId);
        ResponseBodyDto<FactoryImportVoucherDto> res = new ResponseBodyDto<>(factoryImportVoucher,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update factory import voucher
    @PatchMapping(value = "/factory-import-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<FactoryImportVoucherDto>> updateFactoryImportVoucher(
            @Valid @RequestBody UpdateFactoryImportVoucherRequest request,
            @PathVariable(name = "id") String id) {

        FactoryImportVoucherDto factoryImportVoucherDto = factoryImportVoucherService.updateFactoryImportVoucher(id, request);
        ResponseBodyDto<FactoryImportVoucherDto> res = new ResponseBodyDto<>(factoryImportVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/factory-import-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteFactoryImportVoucher(
            @PathVariable(name = "id") String id) {
        factoryImportVoucherService.deleteFactoryImportVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create factory import voucher from ware house
    @PostMapping(value = "/factory-import-vouchers/warehouse", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<FactoryImportVoucherDto>> createFactoryImportVoucherFromWarehouse(
            @Valid @RequestBody CreateFactoryImportExportVoucherRequestFromWarehouse request,
            @RequestHeader("department_id") String factoryId,
            @RequestHeader("user_id") String userId) {

        FactoryImportVoucherDto factoryImportVoucher =
                factoryImportVoucherService.createFactoryImportVoucherFromWarehouse(request, factoryId, userId);
        ResponseBodyDto<FactoryImportVoucherDto> res = new ResponseBodyDto<>(factoryImportVoucher,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
