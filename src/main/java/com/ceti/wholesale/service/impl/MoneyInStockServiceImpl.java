package com.ceti.wholesale.service.impl;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateMoneyInStockRequest;
import com.ceti.wholesale.controller.api.request.UpdateMoneyInStockRequest;
import com.ceti.wholesale.dto.MoneyInStockDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.MoneyInStock;
import com.ceti.wholesale.repository.MoneyInStockRepository;
import com.ceti.wholesale.service.MoneyInStockService;

@Service
@Transactional
public class MoneyInStockServiceImpl implements MoneyInStockService {
    @Autowired
    private MoneyInStockRepository moneyInStockRepository;

    @Override
    public Page<MoneyInStockDto> getAllMoneyInStockByCondition(String factoryId, Pageable pageable) {
        Page<MoneyInStock> page = moneyInStockRepository.getAllByCondition(factoryId, pageable);
        return CommonMapper.toPage(page, MoneyInStockDto.class, pageable);
    }

    @Override
    public MoneyInStockDto createMoneyInStock(CreateMoneyInStockRequest createMoneyInStockRequest, String factoryId) {
        if (moneyInStockRepository.existsById(createMoneyInStockRequest.getYear() + "-" + factoryId)) {
            throw new BadRequestException("Quỹ tiền mặt đã tồn tại");
        }
        new MoneyInStock();
        Instant instant = Instant.now();
        MoneyInStock moneyInStock;
        moneyInStock = CommonMapper.map(createMoneyInStockRequest, MoneyInStock.class);
        moneyInStock.setId(createMoneyInStockRequest.getYear() + "-" + factoryId);
        moneyInStock.setFactoryId(factoryId);
        moneyInStock.setCreateAt(instant);
        moneyInStock.setUpdateAt(instant);
        moneyInStockRepository.save(moneyInStock);
        return CommonMapper.map(moneyInStock, MoneyInStockDto.class);
    }

    @Override
    public MoneyInStockDto updateMoneyInStock(String id, UpdateMoneyInStockRequest updateMoneyInStockRequest) {
        Optional<MoneyInStock> res = moneyInStockRepository.findById(id);
        if (res.isEmpty()) {
            throw new NotFoundException("Quỹ tiền mặt không tồn tại");
        }
        Instant instant = Instant.now();
        MoneyInStock moneyInStock = res.get();
        CommonMapper.copyPropertiesIgnoreNull(updateMoneyInStockRequest, moneyInStock);
        moneyInStock.setUpdateAt(instant);
        moneyInStockRepository.save(moneyInStock);
        return CommonMapper.map(moneyInStock, MoneyInStockDto.class);
    }

    @Override
    public void deleteMoneyInStock(String id) {
        if (!moneyInStockRepository.existsById(id)) {
            throw new NotFoundException("đầu kì Quỹ tiền mặt không tồn tại");
        }
        moneyInStockRepository.deleteById(id);
    }

//    @Override
//    public Integer forwardToNextYear(Integer yearFrom, Integer yearTo, String factoryId, String userFullName) {
//        return moneyInStockRepository.setForwardToNextYear(yearFrom,yearTo,factoryId,userFullName);
//    }
}
