package com.ceti.wholesale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.controller.api.request.CreateMoneyDebtRequest;
import com.ceti.wholesale.dto.BeginningMoneyDebtDto;

public interface BeginningMoneyDebtService {
    Page<BeginningMoneyDebtDto> getAllMoneyDebt(MultiValueMap<String, String> where, Pageable pageable);

    BeginningMoneyDebtDto createMoneyDebt(CreateMoneyDebtRequest createMoneyDebtRequest, String factoryId);

    BeginningMoneyDebtDto updateMoneyDebt(String id, CreateMoneyDebtRequest updateMoneyDebtRequest);

    void deleteBeginningMoneyDebt(String id);

//    Integer forwardToNextYear(Integer yearFrom, Integer yearTo, String factoryId, String userFullName);
}
