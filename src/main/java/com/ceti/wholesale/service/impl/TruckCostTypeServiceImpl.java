package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.dto.TruckCostTypeDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.TruckCostType;
import com.ceti.wholesale.repository.TruckCostTypeRepository;
import com.ceti.wholesale.service.TruckCostTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TruckCostTypeServiceImpl implements TruckCostTypeService {
    @Autowired
    TruckCostTypeRepository truckCostTypeRepository;
    @Override
    public List<TruckCostTypeDto> getFindAll() {
        List<TruckCostType> truckCostTypeList = truckCostTypeRepository.findAll();
        return CommonMapper.toList(truckCostTypeList, TruckCostTypeDto.class);
    }
}
