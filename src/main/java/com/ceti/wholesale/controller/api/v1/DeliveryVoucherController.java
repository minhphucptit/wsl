package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateDeliveryVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateDeliveryVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.DeliveryVoucherDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.service.DeliveryVoucherService;
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
public class DeliveryVoucherController {

    @Autowired
    GoodsInOutService goodsInOutService;

    @Autowired
    private DeliveryVoucherService deliveryVoucherService;

    @Autowired
    private VoucherMapper voucherMapper;

    // get list delivery voucher
    @GetMapping(value = "/delivery-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<VoucherDto>> getAllDeliveryVoucher(
            @RequestParam HashMap<String, String> where,
//            @RequestParam(value = "embed_goods_in_out", required = false) boolean embedGoodsInOut,
            @RequestHeader(name = "department_id") String factory_id,
            Pageable pageable) {
        where.put("factory_id", factory_id);
        String tableName = "delivery_voucher";
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, tableName);
        List<VoucherDto> list = voucherMapper.getList(where, pagingStr,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        long totalItems = voucherMapper.countList(where,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        ResponseBodyDto<VoucherDto> res = new ResponseBodyDto<>(list,
                ResponseCodeEnum.R_200, "OK",totalItems);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create delivery voucher
    @PostMapping(value = "/delivery-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<DeliveryVoucherDto>> createDeliveryVoucher(
            @Valid @RequestBody CreateDeliveryVoucherRequest request,
            @RequestHeader(name = "department_id") String factory_id) {

        DeliveryVoucherDto deliveryVoucherDto = deliveryVoucherService.createDeliveryVoucher(request, factory_id);
        ResponseBodyDto<DeliveryVoucherDto> res = new ResponseBodyDto<>(deliveryVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update delivery voucher
    @PatchMapping(value = "/delivery-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<DeliveryVoucherDto>> updateDeliveryVoucher(
            @Valid @RequestBody UpdateDeliveryVoucherRequest request,
            @PathVariable(name = "id") String id) {

        DeliveryVoucherDto deliveryVoucherDto = deliveryVoucherService.updateDeliveryVoucher(id, request);
        ResponseBodyDto<DeliveryVoucherDto> res = new ResponseBodyDto<>(deliveryVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/delivery-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteDeliveryVoucher(
            @PathVariable(name = "id") String id) {
        deliveryVoucherService.deleteDeliveryVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
