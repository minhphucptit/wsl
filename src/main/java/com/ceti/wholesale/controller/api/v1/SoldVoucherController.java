package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateSoldVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateSoldVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.SoldVoucherDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.SoldVoucherService;
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
public class SoldVoucherController {

    @Autowired
    GoodsInOutService goodsInOutService;
    @Autowired
    private SoldVoucherService soldVoucherService;
    @Autowired
    private VoucherMapper voucherMapper;

    // get list sold voucher
    @GetMapping(value = "/sold-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<VoucherDto>> getAllSoldVoucher(
            @RequestParam HashMap<String, String> where,
//            @RequestParam(value = "embed_goods_in_out", required = false) boolean embedGoodsInOut,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        where.put("factory_id", factoryId);
        String tableName = "sold_voucher";
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, tableName);
        List<VoucherDto> list = voucherMapper.getList(where, pagingStr,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        long totalItems = voucherMapper.countList(where,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        ResponseBodyDto<VoucherDto> res = new ResponseBodyDto<>(list,
                ResponseCodeEnum.R_200, "OK",totalItems);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create sold voucher
    @PostMapping(value = "/sold-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<SoldVoucherDto>> createSoldVoucher(
            @Valid @RequestBody CreateSoldVoucherRequest request,
            @RequestHeader(name = "department_id") String factoryId) {

        SoldVoucherDto soldVoucherDto = soldVoucherService.createSoldVoucher(request, factoryId);
        ResponseBodyDto<SoldVoucherDto> res = new ResponseBodyDto<>(soldVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update sold voucher
    @PatchMapping(value = "/sold-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<SoldVoucherDto>> updateSoldVoucher(
            @Valid @RequestBody UpdateSoldVoucherRequest request,
            @PathVariable(name = "id") String id) {

        SoldVoucherDto soldVoucherDto = soldVoucherService.updateSoldVoucher(id, request);
        ResponseBodyDto<SoldVoucherDto> res = new ResponseBodyDto<>(soldVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/sold-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteSoldVoucher(
            @PathVariable(name = "id") String id) {
        soldVoucherService.deleteSoldVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
