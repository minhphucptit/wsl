package com.ceti.wholesale.mapper;

import com.ceti.wholesale.dto.GasFillingQuantityByFactoryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;
import java.util.List;

@Mapper
public interface GasFillingQuantityByFactoryMapper {

    List<GasFillingQuantityByFactoryDto> getList(@Param("date_from") Instant dateFrom,
                                                 @Param("date_to") Instant dateTo,
                                                 @Param("factory_id") String factoryId);
}
