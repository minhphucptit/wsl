package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateMoneyInStockRequest;
import com.ceti.wholesale.controller.api.request.UpdateMoneyInStockRequest;
import com.ceti.wholesale.dto.MoneyInStockDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MoneyInStockService {
    Page<MoneyInStockDto> getAllMoneyInStockByCondition(String factoryId, Pageable pageable);

    MoneyInStockDto createMoneyInStock(CreateMoneyInStockRequest createMoneyInStockRequest, String factoryId);

    MoneyInStockDto updateMoneyInStock(String year, UpdateMoneyInStockRequest updateMoneyInStockRequest);

    void deleteMoneyInStock(String id);

//    Integer forwardToNextYear(Integer yearFrom, Integer yearTo, String factoryId, String userFullName);
}
