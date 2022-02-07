package com.ceti.wholesale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ceti.wholesale.controller.api.request.CreateSalemanRequest;
import com.ceti.wholesale.controller.api.request.UpdateSalemanRequest;
import com.ceti.wholesale.dto.SalesmanDto;
import org.springframework.util.MultiValueMap;

public interface SalesmanService {

    SalesmanDto createSaleman(CreateSalemanRequest request, String factory_id);

    SalesmanDto updateSaleman(String id, UpdateSalemanRequest request);

    Page<SalesmanDto> getAllByCondition(MultiValueMap<String, String> where, String embed, String factoryId, Pageable pageable);
}
