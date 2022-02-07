package com.ceti.wholesale.mapper;

import java.time.Instant;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ceti.wholesale.dto.ProductionMonitoringDto;
import com.ceti.wholesale.dto.plan.ProductionMonitoringStatisticDataDto;

@Mapper
public interface ProductionMonitoringMapper {
	
	List<ProductionMonitoringDto> getList(@Param("customer_id") String customerId,
			@Param("voucher_at_from") Instant voucherAtFrom,
			@Param("voucher_at_to") Instant voucherAtTo,
			@Param("page") String page);
	
	long countList(@Param("customer_id") String customerId,
			@Param("voucher_at_from") Instant voucherAtFrom,
			@Param("voucher_at_to") Instant voucherAtTo);
	
	List<ProductionMonitoringDto> getListFromWholesaleData(@Param("customer_code") String customerCode,
			@Param("voucher_at_from") Instant voucherAtFrom,
			@Param("voucher_at_to") Instant voucherAtTo);

	List<List<ProductionMonitoringStatisticDataDto>> getStatistic(@Param("customer_code") String customerCode,
			@Param("voucher_at_from") Instant voucherAtFrom,
			@Param("voucher_at_to") Instant voucherAtTo,
			@Param("region_id") String regionId,
			@Param("company_id") String companyId,
			@Param("owner_company") String ownerCompany,
			@Param("is_wholesale_customer") Boolean isWholesaleCustomer,
			@Param("customer_category") String customerCategory,
			@Param("is_target_production")Boolean isTargetProduction);
}
