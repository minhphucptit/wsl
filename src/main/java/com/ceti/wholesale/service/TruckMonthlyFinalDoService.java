package com.ceti.wholesale.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.controller.api.request.CreateTruckMonthlyFinalDoRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckMonthlyFinalDoRequest;
import com.ceti.wholesale.dto.TruckMonthlyFinalDoDto;

public interface TruckMonthlyFinalDoService {
	
    Page<TruckMonthlyFinalDoDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    TruckMonthlyFinalDoDto create(CreateTruckMonthlyFinalDoRequest request);
	
	TruckMonthlyFinalDoDto update(UpdateTruckMonthlyFinalDoRequest request, String id);
	
	void delete(String id);
	
    List<TruckMonthlyFinalDoDto> createAll(String factoryId, Integer month, Integer year, String truckLicencePlateNumber, Boolean hasPsDifference);

}
