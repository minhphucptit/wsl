package com.ceti.wholesale.mapper;

import java.time.Instant;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ceti.wholesale.dto.GoodsInOutDto;

@Mapper
public interface GoodInOutMapper {

	List<GoodsInOutDto> getList(@Param("voucher_id") String voucherId, @Param("is_main_product") Boolean isMainProduct,@Param("embed") String[] embed);

	long countList(@Param("voucher_id") String voucherId, @Param("is_main_product") Boolean isMainProduct,@Param("embed") String[] embed);
	
	List<GoodsInOutDto> getListSuggesion(@Param("voucher_at") Instant voucherAt, @Param("factory_id") String factoryId, 
			@Param("gas_refueling_voucher_id") String gasRefuelingVoucherId, @Param("is_created") Boolean isCreated);
}
