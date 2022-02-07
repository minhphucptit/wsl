package com.ceti.wholesale.mapper;

import com.ceti.wholesale.dto.CostTrackBoardDto;
import com.ceti.wholesale.dto.CostTrackBoardTotalQuantityAndTotalItemsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;
import java.util.List;

@Mapper
public interface CostTrackBoardMapper {

    List<CostTrackBoardDto> getList(@Param("action_date_from") Instant actionDateFrom,
                                         @Param("action_date_to") Instant actionDateTo,
                                         @Param("truck_cost_type_id") String truckCostTypeId,
                                         @Param("factory_id") String factoryId,
                                         @Param("truck_license_plate_number") String truckLicensePlateNumber,
                                         @Param("type") String type,
                                         @Param("cost_track_board_type") String costTrackBoardType,
                                         @Param("equipment_id") String equipmentId,
                                         @Param("equipment_cost_type_name") String equipmentCostTypeName,
                                         @Param("offset") long offset, @Param("size") int size);

    CostTrackBoardTotalQuantityAndTotalItemsDto getTotal(@Param("action_date_from") Instant actionDateFrom,
                                                              @Param("action_date_to") Instant actionDateTo,
                                                              @Param("truck_cost_type_id") String truckCostTypeId,
                                                              @Param("factory_id") String factoryId,
                                                              @Param("truck_license_plate_number") String truckLicensePlateNumber,
                                                              @Param("type") String type,
                                                              @Param("cost_track_board_type") String costTrackBoardType,
                                                              @Param("equipment_id") String equipmentId,
                                                              @Param("equipment_cost_type_name") String equipmentCostTypeName);
    long countList(@Param("action_date_from") Instant actionDateFrom,
                   @Param("action_date_to") Instant actionDateTo,
                   @Param("truck_cost_type_id") String truckCostTypeId,
                   @Param("factory_id") String factoryId,
                   @Param("truck_license_plate_number") String truckLicensePlateNumber,
                   @Param("type") String type,
                   @Param("cost_track_board_type") String costTrackBoardType,
                   @Param("equipment_id") String equipmentId,
                   @Param("equipment_cost_type_name") String equipmentCostTypeName);
}
