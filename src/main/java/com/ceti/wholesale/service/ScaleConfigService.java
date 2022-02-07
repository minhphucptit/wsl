package com.ceti.wholesale.service;

import com.ceti.wholesale.dto.ScaleConfigDto;


public interface ScaleConfigService {
    ScaleConfigDto getByFactoryId(String factoryId);
}
