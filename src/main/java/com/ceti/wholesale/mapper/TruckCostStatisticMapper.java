package com.ceti.wholesale.mapper;

import com.ceti.wholesale.dto.TruckCostStatisticDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;
import java.util.List;

@Mapper
public interface TruckCostStatisticMapper {

    List<TruckCostStatisticDto> getList(@Param("action_date_from") Instant dateFrom,
                                            @Param("action_date_to") Instant dateTo,
                                            @Param("factory_id") String factoryId,
                                            @Param("truck_license_plate_number") String truckLicensePlateNumber,@Param("type") String type);
}
