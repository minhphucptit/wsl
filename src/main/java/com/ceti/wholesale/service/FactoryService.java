package com.ceti.wholesale.service;


import com.ceti.wholesale.controller.api.request.UpdateFactoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ceti.wholesale.dto.FactoryDto;
import com.ceti.wholesale.model.Factory;

public interface FactoryService {

    FactoryDto getFactoryById(String id);
    
    Page<Factory> getList(Factory requestObject, Pageable pageable);

    FactoryDto updateFactory(String id, UpdateFactoryRequest request);

}
