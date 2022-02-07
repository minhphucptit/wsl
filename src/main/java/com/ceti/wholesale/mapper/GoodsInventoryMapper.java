package com.ceti.wholesale.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ceti.wholesale.dto.GoodsInventoryDto;

@Mapper
public interface GoodsInventoryMapper {
	List<GoodsInventoryDto> getList(@Param("voucherId") String voucherId,
			@Param("productType") String productType);
	
	long countList(@Param("voucherId") String voucherId,
			@Param("productType") String productType);
}
