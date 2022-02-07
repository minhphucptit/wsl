package com.ceti.wholesale.service;

import java.time.Instant;
import java.util.List;

import com.ceti.wholesale.controller.api.request.CreateTruckDistanceListRequest;
import com.ceti.wholesale.controller.api.request.CreateTruckDistanceRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckDistanceRequest;
import com.ceti.wholesale.dto.TruckDistanceDto;

public interface TruckDistanceService {

	List<TruckDistanceDto> createList(CreateTruckDistanceListRequest request, String factoryId);

	TruckDistanceDto create(CreateTruckDistanceRequest request);
	
	TruckDistanceDto update(UpdateTruckDistanceRequest request, String truckLicensePlateNumber, Instant day);
	
	void delete(String truckLicensePlateNumber, Instant day);
}
