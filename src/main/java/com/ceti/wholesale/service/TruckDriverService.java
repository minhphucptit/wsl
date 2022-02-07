package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateTruckDriverRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckDriverRequest;
import com.ceti.wholesale.dto.TruckDriverDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface TruckDriverService {
    TruckDriverDto createTruckDriver(CreateTruckDriverRequest request, String factory_id);

    TruckDriverDto updateTruckDriver(String truckDriverId, UpdateTruckDriverRequest request);

    Page<TruckDriverDto> getAllByCondition(MultiValueMap<String, String> where, String embed, Pageable pageable);

}
