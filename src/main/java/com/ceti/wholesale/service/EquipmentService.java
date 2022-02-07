package com.ceti.wholesale.service;


import java.time.Instant;
import java.util.List;

import com.ceti.wholesale.controller.api.request.CreateEquipmentRequest;
import com.ceti.wholesale.dto.EquipmentDto;

public interface EquipmentService {
	EquipmentDto createEquipment(CreateEquipmentRequest request,String factoryId);
	EquipmentDto updateEquipment(String equipmentId,CreateEquipmentRequest request);
	
	List<EquipmentDto> getAll(Instant signDateFrom,Instant signDateTo, String name, String origin,String brand,
			 String symbol, String model, String manufactureYear, String factoryId,String isActive,String pagingStr);
}
