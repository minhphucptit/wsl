package com.ceti.wholesale.mapper;

import java.time.Instant;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ceti.wholesale.dto.EquipmentDto;

@Mapper
public interface EquipmentMapper {
	List<EquipmentDto> getList(@Param("inspection_date_from") Instant signDateFrom,
			@Param("inspection_date_to") Instant signDateTo, @Param("name") String name,@Param("origin") String origin,@Param("brand") String brand,
			@Param("symbol") String symbol,@Param("model") String model,@Param("manufacture_year") String manufactureYear,@Param("factory_id") String factoryId,
			@Param("is_active")String isActive, @Param("pagingStr") String pagingStr);

	long countList(@Param("inspection_date_from") Instant signDateFrom,
			@Param("inspection_date_to") Instant signDateTo, @Param("name") String name,@Param("origin") String origin,@Param("brand") String brand,
			@Param("symbol") String symbol,@Param("model") String model,@Param("manufacture_year") String manufactureYear,
		   @Param("factory_id") String factoryId,@Param("is_active")String isActive);
}
