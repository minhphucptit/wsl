package com.ceti.wholesale.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsInStockMapper {

	Integer setForwardToNextYear(@Param("year_from") Integer yearFrom,
								 @Param("year_to") Integer yearTo,
								 @Param("factory_id") String factoryId,
								 @Param("user_full_name") String userFullName);
}
