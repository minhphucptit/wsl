package com.ceti.wholesale.mapper;

import java.time.Instant;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ceti.wholesale.dto.ContractDto;

@Mapper
public interface ContractMapper {

	List<ContractDto> getList(@Param("contract_number") String contractNumber, @Param("sign_date_from") Instant signDateFrom,
			@Param("sign_date_to") Instant signDateTo, @Param("expire_date_from") Instant expireDateFrom,
			@Param("expire_date_to") Instant expireDateTo, @Param("status") String status,
			@Param("delivery_method") String deliveryMethod, @Param("contract_category_id") String contractCategoryId,
			@Param("factory_id") String factoryId, @Param("customer_id") String customerId,
			@Param("pagingStr") String pagingStr);

	long countList(@Param("contract_number") String contractNumber, @Param("sign_date_from") Instant signDateFrom,
			@Param("sign_date_to") Instant signDateTo, @Param("expire_date_from") Instant expireDateFrom,
			@Param("expire_date_to") Instant expireDateTo, @Param("status") String status,
			@Param("delivery_method") String deliveryMethod, @Param("contract_category_id") String contractCategoryId,
			@Param("factory_id") String factoryId, @Param("customer_id") String customerId);
}
