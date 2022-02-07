package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateSoldDeliveryVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateSoldDeliveryVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.SoldDeliveryVoucherDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.SoldDeliveryVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class SoldDeliveryVoucherController {

    @Autowired
    private SoldDeliveryVoucherService soldDeliveryVoucherService;

    @Autowired
    private GoodsInOutService goodsInOutService;

    @Autowired
    private VoucherMapper voucherMapper;

    // get list sold delivery voucher
    @GetMapping(value = "/sold-delivery-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<VoucherDto>> getAllSoldDeliveryVoucher(
            @RequestParam HashMap<String, String> where,
//            @RequestParam(value = "embed_goods_in_out", required = false) boolean embedGoodsInOut,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        where.put("factory_id", factoryId);
        String tableName = "sold_delivery_voucher";
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, tableName);
        List<VoucherDto> list = voucherMapper.getList(where, pagingStr,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        long totalItems = voucherMapper.countList(where,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        ResponseBodyDto<VoucherDto> res = new ResponseBodyDto<>(list,
                ResponseCodeEnum.R_200, "OK",totalItems);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create sold delivery voucher
    @PostMapping(value = "/sold-delivery-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<SoldDeliveryVoucherDto>> createSoldDeliveryVoucher(
            @Valid @RequestBody CreateSoldDeliveryVoucherRequest request,
            @RequestHeader(name = "department_id") String factoryId) {

        SoldDeliveryVoucherDto soldDeliveryVoucherDto = soldDeliveryVoucherService
                .createSoldDeliveryVoucher(request, factoryId);
        ResponseBodyDto<SoldDeliveryVoucherDto> res = new ResponseBodyDto<>(soldDeliveryVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update sold delivery voucher
    @PatchMapping(value = "/sold-delivery-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<SoldDeliveryVoucherDto>> updateSoldDeliveryVoucher(
            @Valid @RequestBody UpdateSoldDeliveryVoucherRequest request,
            @PathVariable(name = "id") String id) {

        SoldDeliveryVoucherDto soldDeliveryVoucherDto = soldDeliveryVoucherService
                .updateSoldDeliveryVoucher(id, request);
        ResponseBodyDto<SoldDeliveryVoucherDto> res = new ResponseBodyDto<>(soldDeliveryVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/sold-delivery-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteSoldDeliveryVoucher(
            @PathVariable(name = "id") String id) {
        soldDeliveryVoucherService.deleteSoldDeliveryVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
