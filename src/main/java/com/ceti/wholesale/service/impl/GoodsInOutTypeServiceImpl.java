package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.dto.GoodsInOutTypeDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.GoodsInOutType;
import com.ceti.wholesale.repository.GoodsInOutTypeRepository;
import com.ceti.wholesale.service.GoodsInOutTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GoodsInOutTypeServiceImpl implements GoodsInOutTypeService {

    @Autowired
    private GoodsInOutTypeRepository goodsInOutTypeRepository;

    @Override
    public List<GoodsInOutTypeDto> getGoodsInOutTypeByCode(String code) {
        List<GoodsInOutType> goodsInOutTypes = goodsInOutTypeRepository.findAllByCode(code);
        return CommonMapper.toList(goodsInOutTypes, GoodsInOutTypeDto.class);
    }
}
