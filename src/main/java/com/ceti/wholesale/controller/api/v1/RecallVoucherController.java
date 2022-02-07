package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateRecallVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateRecallVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.RecallVoucherDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.RecallVoucherService;
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
public class RecallVoucherController {

    @Autowired
    GoodsInOutService goodsInOutService;
    @Autowired
    private RecallVoucherService recallVoucherService;
    @Autowired
    private VoucherMapper voucherMapper;

    // get list recall voucher
    @GetMapping(value = "/recall-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<VoucherDto>> getAllRecallVoucher(
            @RequestParam HashMap<String, String> where,
//            @RequestParam(value = "embed_goods_in_out", required = false) boolean embedGoodsInOut,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        where.put("factory_id", factoryId);
        String tableName = "recall_voucher";
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, tableName);
        List<VoucherDto> list = voucherMapper.getList(where, pagingStr,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        long totalItems = voucherMapper.countList(where,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        ResponseBodyDto<VoucherDto> res = new ResponseBodyDto<>(list,
                ResponseCodeEnum.R_200, "OK",totalItems);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create recall voucher
    @PostMapping(value = "/recall-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<RecallVoucherDto>> createRecallVoucher(
            @Valid @RequestBody CreateRecallVoucherRequest request,
            @RequestHeader(name = "department_id") String factoryId) {

        RecallVoucherDto recallVoucherDto = recallVoucherService.createRecallVoucher(request, factoryId);
        ResponseBodyDto<RecallVoucherDto> res = new ResponseBodyDto<>(recallVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }


    // update recall voucher
    @PatchMapping(value = "/recall-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<RecallVoucherDto>> updateRecallVoucher(
            @Valid @RequestBody UpdateRecallVoucherRequest request,
            @PathVariable(name = "id") String id) {

        RecallVoucherDto recallVoucherDto = recallVoucherService.updateRecallVoucher(id, request);
        ResponseBodyDto<RecallVoucherDto> res = new ResponseBodyDto<>(recallVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/recall-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteRecallVoucher(
            @PathVariable(name = "id") String id) {
        recallVoucherService.deleteRecallVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
