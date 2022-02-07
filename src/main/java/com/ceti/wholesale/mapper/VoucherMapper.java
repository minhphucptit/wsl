package com.ceti.wholesale.mapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ceti.wholesale.dto.VoucherDto;

@Mapper
public interface VoucherMapper {

	List<VoucherDto> getList(@Param("where") HashMap<String,String>where, @Param("pagingStr") String pagingStr, @Param("tableNameStr") String tableName
			,@Param("voucher_at_from") Instant voucherAtFrom, @Param("voucher_at_to") Instant voucherAtTo );

	long countList(HashMap<String,String>where, @Param("tableNameStr") String tableName
			,@Param("voucher_at_from") Instant voucherAtFrom, @Param("voucher_at_to") Instant voucherAtTo);
}
