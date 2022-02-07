package com.ceti.wholesale.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ceti.wholesale.dto.CustomerReceiptPaymentVoucherDto;

@Mapper
public interface CustomerReceiptPaymentVoucherMapper {

	List<CustomerReceiptPaymentVoucherDto> getList(@Param("customerId") String customerId,
			@Param("customerFullName") String customerFullName, @Param("payer") String payer,
			@Param("category") String category, @Param("payerMethod") Boolean payerMethod,
			@Param("factoryId") String factoryId, @Param("voucher_code") String voucherCode,
			@Param("offset") long offset, @Param("size") int size);
	
	long countList(@Param("customerId") String customerId,
			@Param("customerFullName") String customerFullName, @Param("payer") String payer,
			@Param("category") String category, @Param("payerMethod") Boolean payerMethod,
			@Param("factoryId") String factoryId, @Param("voucher_code") String voucherCode);
}
