package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.repository.GoodsInOutDetailRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.service.GoodsInOutService;

@Service
@Transactional
public class GoodsInOutServiceImpl implements GoodsInOutService {

    @Autowired
    private GoodsInOutRepository goodsInOutRepository;

    @Autowired
    private GoodsInOutDetailRepository goodsInOutDetailRepository;

    @Override
    public List<GoodsInOutDto> getGoodsInOutByVoucherId(String voucherId, Boolean isMainProduct) {
        List<GoodsInOut> goodsInOut = goodsInOutRepository.getByVoucherIdAndIsMainProduct(voucherId, isMainProduct);
        return CommonMapper.toList(goodsInOut, GoodsInOutDto.class);
    }

    @Override
    public List<GoodsInOutDto> getGoodsInOutBySoldVoucherIdAndOutQuantityEmptyOrInQuantityEmpty(String voucherId,
                                                                                                BigDecimal zero,
                                                                                                Boolean checkInQuantity,
                                                                                                String factoryId) {
        List<GoodsInOut> goodsInOut;
        if (checkInQuantity) {
            goodsInOut = goodsInOutRepository.getByVoucherIdAndInQuantityAndFactoryId(voucherId, zero, factoryId);
        } else {
            goodsInOut = goodsInOutRepository.getByVoucherIdAndOutQuantityAndFactoryId(voucherId, zero, factoryId);
        }


        return CommonMapper.toList(goodsInOut, GoodsInOutDto.class);
    }
}
