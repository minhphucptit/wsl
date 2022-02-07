package com.ceti.wholesale.mapper;

import com.ceti.wholesale.dto.GoodsInOutTypeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsInOutTypeMapper {

    List<GoodsInOutTypeDto> getList(@Param("voucher_code") String voucherCode,
                                    @Param("code_not_equal") String codeNotEqual,
                                    @Param("offset") long offset, @Param("size") int size);

    long countList(@Param("voucher_code") String voucherCode,
                   @Param("code_not_equal") String codeNotEqual);
}
