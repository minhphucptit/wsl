package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateTruckRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckRequest;
import com.ceti.wholesale.dto.TruckDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface TruckService {
    TruckDto createTruck(CreateTruckRequest request, String factory_id);

    TruckDto updateTruck(String licensePlateNumber, UpdateTruckRequest request);

    //Page<TruckDto> getAllByCondition(String licensePlateNumber, String factoryId, Boolean isActive, Pageable pageable);

    //Page<TruckDto> getAllByConditionWithEmbed(MultiValueMap<String, String> where, Pageable pageable);
}
