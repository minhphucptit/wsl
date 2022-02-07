package com.ceti.wholesale.mapper;

import java.time.Instant;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RevenueTruckAndDriverStatisticMapper {

	List<List<Object>> getAll(@Param("factory_id") String factoryId,
			@Param("date_from") Instant dateFrom, @Param("date_to") Instant dateTo,
			@Param("type") String type,@Param("weight")String weight,@Param("page") int page, @Param("size") int size);
	
}
