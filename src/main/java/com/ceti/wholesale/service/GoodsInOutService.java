package com.ceti.wholesale.service;

import java.math.BigDecimal;
import java.util.List;

import com.ceti.wholesale.dto.GoodsInOutDto;

public interface GoodsInOutService {

//    Page<GoodsInOutDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    List<GoodsInOutDto> getGoodsInOutByVoucherId(String voucherId, Boolean isMainProduct);

    //factory rotation voucher
    List<GoodsInOutDto> getGoodsInOutBySoldVoucherIdAndOutQuantityEmptyOrInQuantityEmpty(String voucherId,
                                                                                         BigDecimal outQuantity,
                                                                                         Boolean checkInquantity,
                                                                                         String factoryId);

}
