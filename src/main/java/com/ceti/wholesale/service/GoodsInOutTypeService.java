package com.ceti.wholesale.service;

import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.dto.GoodsInOutTypeDto;

import java.util.List;

public interface GoodsInOutTypeService {
    List<GoodsInOutTypeDto> getGoodsInOutTypeByCode(String code);
}
