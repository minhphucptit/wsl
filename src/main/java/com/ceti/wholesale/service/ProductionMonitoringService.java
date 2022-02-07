package com.ceti.wholesale.service;

import java.time.Instant;
import java.util.List;

import com.ceti.wholesale.controller.api.request.CreateProductionMonitoringRequest;
import com.ceti.wholesale.controller.api.request.v2.UpdateProductionMonitoringRequest;
import com.ceti.wholesale.dto.ProductionMonitoringDto;

public interface ProductionMonitoringService {
	
	List<ProductionMonitoringDto> create(CreateProductionMonitoringRequest request, String userId);

	ProductionMonitoringDto update(UpdateProductionMonitoringRequest request, String userId);

	void delete(String customerId, Instant voucherAt);
}
