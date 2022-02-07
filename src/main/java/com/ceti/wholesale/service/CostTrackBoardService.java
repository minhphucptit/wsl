package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateCostTrackBoardRequest;
import com.ceti.wholesale.controller.api.request.UpdateCostTrackBoardRequest;
import com.ceti.wholesale.dto.CostTrackBoardDto;

public interface CostTrackBoardService {
    CostTrackBoardDto createCostTrackBoard(CreateCostTrackBoardRequest request, String factoryId);

    CostTrackBoardDto updateCostTrackBoard(String id, UpdateCostTrackBoardRequest request);

    void deleteCostTrackBoard(String id);
}
