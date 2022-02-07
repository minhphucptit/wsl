package com.ceti.wholesale.mapper;

import com.ceti.wholesale.dto.DeliveryVoucherDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;
import java.util.List;

@Mapper
public interface DeliveryVoucherMapper {

	List<DeliveryVoucherDto> getList(@Param("product_id") String productId,@Param("no") String no, @Param("truck_license_plate_number") String truckLicensePlateNumber,
									 @Param("truck_driver_id") String truckDriverId,
									 @Param("factory_id") String factoryId,
									 @Param("voucher_at_from") Instant voucherAtFrom, @Param("voucher_at_to") Instant voucherAtTo,
									 @Param("pagingStr") String pagingStr);

	long countList(@Param("product_id") String productId,@Param("no") String no, @Param("truck_license_plate_number") String truckLicensePlateNumber,
				   @Param("truck_driver_id") String truckDriverId,
				   @Param("factory_id") String factoryId,
				   @Param("voucher_at_from") Instant voucherAtFrom, @Param("voucher_at_to") Instant voucherAtTo);
}
