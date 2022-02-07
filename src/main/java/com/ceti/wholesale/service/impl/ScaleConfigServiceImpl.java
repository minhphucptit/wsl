package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.dto.ScaleConfigDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.ScaleConfig;
import com.ceti.wholesale.repository.ScaleConfigRepository;
import com.ceti.wholesale.service.ScaleConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScaleConfigServiceImpl implements ScaleConfigService {

    @Autowired
    private ScaleConfigRepository scaleConfigRepository;

    @Override
    public ScaleConfigDto getByFactoryId(String factoryId) {
        ScaleConfig scaleConfig = scaleConfigRepository.findByFactoryId(factoryId);
        return CommonMapper.map(scaleConfig, ScaleConfigDto.class);
    }
}
