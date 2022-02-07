package com.ceti.wholesale.mapper;

import java.time.Instant;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ceti.wholesale.dto.CylinderInventoryDto;

@Mapper
public interface CylinderInventoryMapper {
	List<CylinderInventoryDto> getList(@Param("voucher_at_to")Instant voucherAtTo);
	long countList();
}
