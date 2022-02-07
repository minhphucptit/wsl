package com.ceti.wholesale.controller.api.v1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateCustomerReceiptPaymentVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerReceiptPaymentVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.CustomerReceiptPaymentVoucherDto;
import com.ceti.wholesale.service.CustomerReceiptPaymentVoucherService;

@RestController
@RequestMapping("/v1")
@Validated
public class CustomerReceiptPaymentVoucherController {

    @Autowired
    CustomerReceiptPaymentVoucherService customerReceiptPaymentVoucherService;

    @GetMapping(value = "/customer-receipt-payment-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerReceiptPaymentVoucherDto>> getAllReceiptPayments(
            @RequestParam(value = "customer_id", required = false) String customerId,
            @RequestParam(value = "customer_full_name", required = false) String customerFullName,
            @RequestParam(value = "payer", required = false) String payer,
            @RequestParam(value = "payer_method", required = false) Boolean payerMethod,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "type", required = false) Boolean type,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        Page<CustomerReceiptPaymentVoucherDto> page = customerReceiptPaymentVoucherService.getAllByCondition(
                customerId, customerFullName, payer, payerMethod, category, type, factoryId, pageable);
        ResponseBodyDto<CustomerReceiptPaymentVoucherDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping(value = "/customer-receipt-payment-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerReceiptPaymentVoucherDto>> createCustomerReceiptPaymentVoucher(
            @RequestBody CreateCustomerReceiptPaymentVoucherRequest createCustomerReceiptPaymentVoucherRequest,
            @RequestHeader(name = "department_id") String factoryId) {
        CustomerReceiptPaymentVoucherDto customerReceiptPaymentVoucherDto = customerReceiptPaymentVoucherService
                .createCustomerReceiptPaymentVoucher(createCustomerReceiptPaymentVoucherRequest, factoryId);
        ResponseBodyDto<CustomerReceiptPaymentVoucherDto> res = new ResponseBodyDto<>(
                customerReceiptPaymentVoucherDto, ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PatchMapping(value = "/customer-receipt-payment-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerReceiptPaymentVoucherDto>> updateCustomerReceiptPaymentVoucher(
            @PathVariable(name = "id") String id,
            @RequestBody UpdateCustomerReceiptPaymentVoucherRequest updateCustomerReceiptPaymentVoucherRequest) {
        CustomerReceiptPaymentVoucherDto customerReceiptPaymentVoucherDto = customerReceiptPaymentVoucherService
                .updateCustomerReceiptPaymentVoucher(id,
                        updateCustomerReceiptPaymentVoucherRequest);
        ResponseBodyDto<CustomerReceiptPaymentVoucherDto> res = new ResponseBodyDto<>(customerReceiptPaymentVoucherDto,
                ResponseCodeEnum.R_200, "ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/customer-receipt-payment-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteCustomerReceiptPaymentVoucher(
            @PathVariable(name = "id") String id) {
        customerReceiptPaymentVoucherService.deleteCustomerReceiptPaymentVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
