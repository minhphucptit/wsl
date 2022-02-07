package com.ceti.wholesale.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TruckDistanceMapper {

	List<List<Object>> getList(@Param("month") Integer month, @Param("year") Integer year,
			@Param("truck_license_plate_number") String truckLicensePlateNumber);
}
