package com.ceti.wholesale.mapper;

import java.time.Instant;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RevenueAndCycleStatisticMapper {
	
	List<List<Object>> getList(@Param("customer_code") String customerCode,
			   @Param("date_from") Instant dateFrom, @Param("date_to") Instant dateTo);
}
