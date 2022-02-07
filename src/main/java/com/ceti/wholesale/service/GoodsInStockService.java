package com.ceti.wholesale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.controller.api.request.CreateGoodsInStockRequest;
import com.ceti.wholesale.dto.GoodsInStockDto;

@Service
public interface GoodsInStockService {
    Page<GoodsInStockDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    GoodsInStockDto createGoodsInStock(CreateGoodsInStockRequest request, String factoryId);

    GoodsInStockDto updateGoodsInStock(String id, CreateGoodsInStockRequest request);

    void deleteGoodsInStock(String id);

//    Integer forwardToNextYear(Integer yearFrom, Integer yearTo, String factoryId, String userFullName);
}
