package com.ceti.wholesale.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ceti.wholesale.dto.TargetProductionDto;

@Mapper
public interface TargetProductionMapper {
	List<TargetProductionDto> getList(@Param("customerCode") String customerCode,
			@Param("customerFullName") String customerFullName, @Param("monthFrom") Integer monthFrom,
			@Param("monthTo") Integer monthTo, @Param("yearFrom") Integer yearFrom,
			@Param("yearTo") Integer yearTo,
			@Param("offset") long offset, @Param("size") int size);
	
	long countList(@Param("customerCode") String customerCode,
			@Param("customerFullName") String customerFullName, @Param("monthFrom") Integer monthFrom,
			@Param("monthTo") Integer monthTo, @Param("yearFrom") Integer yearFrom,
			@Param("yearTo") Integer yearTo);
}
