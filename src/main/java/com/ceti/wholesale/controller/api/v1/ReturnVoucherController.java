package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateReturnVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateReturnVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ReturnVoucherDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ReturnVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/v1")
@Validated
public class ReturnVoucherController {

    @Autowired
    GoodsInOutService goodsInOutService;

    @Autowired
    private ReturnVoucherService returnVoucherService;

    @Autowired
    private VoucherMapper voucherMapper;

    // get list return voucher
    @GetMapping(value = "/return-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<VoucherDto>> getAllReturnVoucher(
            @RequestParam HashMap<String, String> where,
            @RequestHeader(name = "department_id") String factory_id,
            Pageable pageable) {
        where.put("factory_id", factory_id);
        String tableName = "return_voucher";
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, tableName);
        List<VoucherDto> list = voucherMapper.getList(where, pagingStr,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        long totalItems = voucherMapper.countList(where,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        ResponseBodyDto<VoucherDto> res = new ResponseBodyDto<>(list,
                ResponseCodeEnum.R_200, "OK",totalItems);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create return voucher
    @PostMapping(value = "/return-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ReturnVoucherDto>> createReturnVoucher(
            @Valid @RequestBody CreateReturnVoucherRequest request,
            @RequestHeader(name = "department_id") String factory_id) {

        ReturnVoucherDto returnVoucherDto = returnVoucherService.createReturnVoucher(request, factory_id);
        ResponseBodyDto<ReturnVoucherDto> res = new ResponseBodyDto<>(returnVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update return voucher
    @PatchMapping(value = "/return-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ReturnVoucherDto>> updateReturnVoucher(
            @Valid @RequestBody UpdateReturnVoucherRequest request,
            @PathVariable(name = "id") String id) {

        ReturnVoucherDto returnVoucherDto = returnVoucherService.updateReturnVoucher(id, request);
        ResponseBodyDto<ReturnVoucherDto> res = new ResponseBodyDto<>(returnVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/return-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteReturnVoucher(
            @PathVariable(name = "id") String id) {
        returnVoucherService.deleteReturnVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
