package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreatePaymentVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdatePaymentVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.PaymentVoucherDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.PaymentVoucherService;
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
public class PaymentVoucherController {

    @Autowired
    GoodsInOutService goodsInOutService;
    @Autowired
    private PaymentVoucherService paymentVoucherService;

    @Autowired
    private VoucherMapper voucherMapper;

    // get list payment voucher
    @GetMapping(value = "/payment-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<VoucherDto>> getAllPaymentVoucher(
            @RequestParam HashMap<String, String> where,
//            @RequestParam(value = "embed_goods_in_out", required = false) boolean embedGoodsInOut,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        where.put("factory_id", factoryId);
        String tableName = "payment_voucher";
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, tableName);
        List<VoucherDto> list = voucherMapper.getList(where, pagingStr,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        long totalItems = voucherMapper.countList(where,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
//        if (embedGoodsInOut) {
//            for (PaymentVoucherDto pv : page) {
//                pv.setGoodsInOut(goodsInOutService.getGoodsInOutByVoucherId(pv.getId(), true));
//            }
//        }
        ResponseBodyDto<VoucherDto> res = new ResponseBodyDto<>(list,
                ResponseCodeEnum.R_200, "OK",totalItems);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create payment voucher
    @PostMapping(value = "/payment-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<PaymentVoucherDto>> createPaymentVoucher(
            @Valid @RequestBody CreatePaymentVoucherRequest request,
            @RequestHeader(name = "department_id") String factoryId) {

        PaymentVoucherDto paymentVoucherDto = paymentVoucherService.createPaymentVoucher(request, factoryId);
        ResponseBodyDto<PaymentVoucherDto> res = new ResponseBodyDto<>(paymentVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update payment voucher
    @PatchMapping(value = "/payment-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<PaymentVoucherDto>> updatePaymentVoucher(
            @Valid @RequestBody UpdatePaymentVoucherRequest request,
            @PathVariable(name = "id") String id) {

        PaymentVoucherDto paymentVoucherDto = paymentVoucherService.updatePaymentVoucher(id, request);
        ResponseBodyDto<PaymentVoucherDto> res = new ResponseBodyDto<>(paymentVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/payment-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deletePaymentVoucher(
            @PathVariable(name = "id") String id) {
        paymentVoucherService.deletePaymentVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping(value = "/payment-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<PaymentVoucherDto>> getDetailPaymentVoucher(
            @PathVariable(name = "id") String id) {

        PaymentVoucherDto paymentVoucherDto = paymentVoucherService.getDetailPaymentVoucher(id);
        ResponseBodyDto<PaymentVoucherDto> res = new ResponseBodyDto<>(paymentVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
