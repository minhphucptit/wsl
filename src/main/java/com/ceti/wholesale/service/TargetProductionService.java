package com.ceti.wholesale.service;


import java.util.List;

import com.ceti.wholesale.controller.api.request.CreateAllTargetProductionRequest;
import com.ceti.wholesale.controller.api.request.CreateTargetProductionRequest;
import com.ceti.wholesale.controller.api.request.DeleteTargetProductionRequest;
import com.ceti.wholesale.controller.api.request.UpdateTargetProductionRequest;
import com.ceti.wholesale.dto.TargetProductionDto;

public interface TargetProductionService {
	
	TargetProductionDto createTargetProduction(CreateTargetProductionRequest request,String userId);
	
	TargetProductionDto updateTargetProduction(UpdateTargetProductionRequest request, String userId);
	
	void deleteTargetProduction(DeleteTargetProductionRequest request);
	
	List<TargetProductionDto> createAll(CreateAllTargetProductionRequest request, String userId);
}
