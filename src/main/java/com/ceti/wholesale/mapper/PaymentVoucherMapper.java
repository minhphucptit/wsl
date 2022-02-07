package com.ceti.wholesale.mapper;

import java.time.Instant;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ceti.wholesale.dto.PaymentVoucherDto;

@Mapper
public interface PaymentVoucherMapper {

	List<PaymentVoucherDto> getList(@Param("product_id") String productId,@Param("sold_delivery_voucher_no") String soldDeliveryVoucherNo,@Param("sold_voucher_no") String soldVoucherNo, @Param("voucher_code") String voucherCode,
									@Param("customer_id") String customerId,@Param("factory_id") String factoryId,
									@Param("voucher_at_from") Instant voucherAtFrom, @Param("voucher_at_to") Instant voucherAtTo,
									@Param("pagingStr") String pagingStr);

	long countList(@Param("product_id") String productId,@Param("sold_delivery_voucher_no") String soldDeliveryVoucherNo,@Param("sold_voucher_no") String soldVoucherNo, @Param("voucher_code") String voucherCode,
				   @Param("customer_id") String customerId,@Param("factory_id") String factoryId,
				   @Param("voucher_at_from") Instant voucherAtFrom, @Param("voucher_at_to") Instant voucherAtTo);
	
	List<List> getListReturnProducts(@Param("delivery_voucher_no") String deliveryVoucherNo,@Param("factory_id") String factoryId);

}
