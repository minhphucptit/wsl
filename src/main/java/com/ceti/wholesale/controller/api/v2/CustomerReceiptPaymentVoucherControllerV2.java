package com.ceti.wholesale.controller.api.v2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.CustomerReceiptPaymentVoucherDto;
import com.ceti.wholesale.mapper.CustomerReceiptPaymentVoucherMapper;

@RestController
@RequestMapping("/v2")
@Validated
public class CustomerReceiptPaymentVoucherControllerV2 {

	@Autowired
	private CustomerReceiptPaymentVoucherMapper customerReceiptPaymentVoucherMapper;

	@GetMapping(value = "/customer-receipt-payment-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseBodyDto<CustomerReceiptPaymentVoucherDto>> getAllReceiptPayments(
			@RequestParam(value = "customer_id", required = false) String customerId,
			@RequestParam(value = "customer_full_name", required = false) String customerFullName,
			@RequestParam(value = "payer", required = false) String payer,
			@RequestParam(value = "payer_method", required = false) Boolean payerMethod,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "voucher_code", required = false) String voucherCode,
			@RequestHeader(name = "department_id") String factoryId, Pageable pageable) {
		List<CustomerReceiptPaymentVoucherDto> data = customerReceiptPaymentVoucherMapper.getList(customerId,
				customerFullName, payer, category, payerMethod, factoryId, voucherCode, pageable.getOffset(),
				pageable.getPageSize());
		long totalItem = customerReceiptPaymentVoucherMapper.countList(customerId, customerFullName, payer, category,
				payerMethod, factoryId, voucherCode);

		ResponseBodyDto<CustomerReceiptPaymentVoucherDto> res = new ResponseBodyDto<>(data, ResponseCodeEnum.R_200,
				"OK", totalItem);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}
}
