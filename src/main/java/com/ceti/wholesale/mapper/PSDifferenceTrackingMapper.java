package com.ceti.wholesale.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PSDifferenceTrackingMapper {

	List<List<Object>> getAll(@Param("factory_id") String factoryId,
			@Param("month") Integer month, @Param("year") Integer year,
			@Param("truck_licence_plate_number") String truckLicencePlateNumber,
			@Param("has_ps_difference") Boolean hasPsDifference, @Param("page") long page, @Param("size") int size);
	
}
