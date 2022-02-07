package com.ceti.wholesale.mapper;

import com.ceti.wholesale.dto.TruckDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TruckMapper {

	List<TruckDto> getList(@Param("where") Map<String,String> where, @Param("pagingStr") String pagingStr);

	long countList(@Param("where")Map<String,String>where);
}
