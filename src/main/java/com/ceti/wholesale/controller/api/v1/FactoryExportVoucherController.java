package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateFactoryExportVoucherRequest;
import com.ceti.wholesale.controller.api.request.CreateFactoryImportExportVoucherRequestFromWarehouse;
import com.ceti.wholesale.controller.api.request.UpdateFactoryExportVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.FactoryExportVoucherDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.service.FactoryExportVoucherService;
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
public class FactoryExportVoucherController {

    @Autowired
    private FactoryExportVoucherService factoryExportVoucherService;

    @Autowired
    private GoodsInOutService goodsInOutService;

    @Autowired
    private VoucherMapper voucherMapper;

    // get list factory export voucher
    @GetMapping(value = "/factory-export-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<VoucherDto>> getAllFactoryExportVoucher(
            @RequestParam HashMap<String, String> where,
//            @RequestParam(value = "embed_goods_in_out", required = false) boolean embedGoodsInOut,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        where.put("factory_id", factoryId);
        String tableName = "factory_export_voucher";
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, tableName);
        List<VoucherDto> list = voucherMapper.getList(where, pagingStr,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        long totalItems = voucherMapper.countList(where,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        ResponseBodyDto<VoucherDto> res = new ResponseBodyDto<>(list,
                ResponseCodeEnum.R_200, "OK",totalItems);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create factory export voucher
    @PostMapping(value = "/factory-export-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<FactoryExportVoucherDto>> createFactoryExportVoucher(
            @Valid @RequestBody CreateFactoryExportVoucherRequest request,
            @RequestHeader(name = "department_id") String factory_id) {

        FactoryExportVoucherDto factoryExportVoucherDto = factoryExportVoucherService
                .createFactoryExportVoucher(request, factory_id);
        ResponseBodyDto<FactoryExportVoucherDto> res = new ResponseBodyDto<>(factoryExportVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update factory export voucher
    @PatchMapping(value = "/factory-export-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<FactoryExportVoucherDto>> updateFactoryExportVoucher(
            @Valid @RequestBody UpdateFactoryExportVoucherRequest request,
            @PathVariable(name = "id") String id) {

        FactoryExportVoucherDto factoryExportVoucherDto = factoryExportVoucherService.updateFactoryExportVoucher(id, request);
        ResponseBodyDto<FactoryExportVoucherDto> res = new ResponseBodyDto<>(factoryExportVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/factory-export-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteFactoryExportVoucher(
            @PathVariable(name = "id") String id) {
        factoryExportVoucherService.deleteFactoryExportVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create factory export voucher from ware house
    @PostMapping(value = "/factory-export-vouchers/warehouse", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<FactoryExportVoucherDto>> createFactoryExportVoucherFromWarehouse(
            @Valid @RequestBody CreateFactoryImportExportVoucherRequestFromWarehouse request,
            @RequestHeader("department_id") String factoryId,
            @RequestHeader("user_id") String userId) {

        FactoryExportVoucherDto factoryExportVoucherDto =
                factoryExportVoucherService.createFactoryExportVoucherFromWarehouse(request, factoryId, userId);
        ResponseBodyDto<FactoryExportVoucherDto> res = new ResponseBodyDto<>(factoryExportVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
