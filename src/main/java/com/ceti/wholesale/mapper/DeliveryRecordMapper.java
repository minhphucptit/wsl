package com.ceti.wholesale.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ceti.wholesale.dto.DeliveryRecordDto;

@Mapper
public interface DeliveryRecordMapper {

	List<DeliveryRecordDto> getList(@Param("factory_id") String factoryId, @Param("voucher_id") String voucherId);
}
