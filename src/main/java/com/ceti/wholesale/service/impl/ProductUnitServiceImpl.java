package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.dto.ProductUnitDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.ProductUnit;
import com.ceti.wholesale.repository.ProductUnitRepository;
import com.ceti.wholesale.service.ProductUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductUnitServiceImpl implements ProductUnitService {
    @Autowired
    ProductUnitRepository productUnitRepository;
    @Override
    public List<ProductUnitDto> getFindAllOrderByAsc() {
        List<ProductUnit> truckCostTypeList = productUnitRepository.findAllByOrderBySortAsc();
        return CommonMapper.toList(truckCostTypeList, ProductUnitDto.class);
    }
}
