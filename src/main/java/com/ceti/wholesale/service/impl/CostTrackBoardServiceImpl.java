package com.ceti.wholesale.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateCostTrackBoardRequest;
import com.ceti.wholesale.controller.api.request.UpdateCostTrackBoardRequest;
import com.ceti.wholesale.dto.CostTrackBoardDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.CostTrackBoard;
import com.ceti.wholesale.repository.CostTrackBoardRepository;
import com.ceti.wholesale.service.CostTrackBoardService;


@Service
@Transactional
public class CostTrackBoardServiceImpl implements CostTrackBoardService {
    @Autowired
    private CostTrackBoardRepository costTrackBoardRepository;

    @Override
    public CostTrackBoardDto createCostTrackBoard(CreateCostTrackBoardRequest request, String factoryId) {

        CostTrackBoard costTrackBoard = new CostTrackBoard();
        CommonMapper.copyPropertiesIgnoreNull(request, costTrackBoard);
        costTrackBoard.setFactoryId(factoryId);
        costTrackBoard = costTrackBoardRepository.save(costTrackBoard);
        return CommonMapper.map(costTrackBoard, CostTrackBoardDto.class);
    }

    @Override
    public CostTrackBoardDto updateCostTrackBoard(String id, UpdateCostTrackBoardRequest request) {

        Optional<CostTrackBoard> optional = costTrackBoardRepository.findById(id);

        if(optional.isEmpty()){
            throw new NotFoundException("Chi phí xe tải/thiết bị này không tồn tại");
        }
        CostTrackBoard costTrackBoard = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(request,costTrackBoard);
        costTrackBoardRepository.save(costTrackBoard);
        return CommonMapper.map(costTrackBoard, CostTrackBoardDto.class);
    }

    @Override
    public void deleteCostTrackBoard(String id) {
        if (!costTrackBoardRepository.existsById(id)) {
            throw new NotFoundException("Chi phí xe tải/thiết bị này không tồn tại");
        }
        costTrackBoardRepository.deleteById(id);
    }
}
