package com.ceti.wholesale.mapper;

import com.ceti.wholesale.dto.SoldVoucherDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;
import java.util.List;

@Mapper
public interface SoldVoucherMapper {

	List<SoldVoucherDto> getList(@Param("product_id") String productId,@Param("no") String no, @Param("company_id") String companyId,
								 @Param("customer_id") String customerId, @Param("truck_driver_id") String truckDriverId,
								 @Param("truck_license_plate_number") String truckLicensePlateNumber,
								 @Param("factory_id") String factoryId, @Param("goods_in_out_type") String goodsInOutType,
								 @Param("voucher_at_from") Instant voucherAtFrom, @Param("voucher_at_to") Instant voucherAtTo,
								 @Param("pagingStr") String pagingStr);

	long countList(@Param("product_id") String productId,@Param("no") String no, @Param("company_id") String companyId,
				   @Param("customer_id") String customerId, @Param("truck_driver_id") String truckDriverId,
				   @Param("truck_license_plate_number") String truckLicensePlateNumber,
				   @Param("factory_id") String factoryId, @Param("goods_in_out_type") String goodsInOutType,
				   @Param("voucher_at_from") Instant voucherAtFrom, @Param("voucher_at_to") Instant voucherAtTo);
}
