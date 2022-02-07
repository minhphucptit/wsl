package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateBeginningCylinderDebtRequest;
import com.ceti.wholesale.dto.BeginningCylinderDebtDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface BeginningCylinderDebtService {

    Page<BeginningCylinderDebtDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    BeginningCylinderDebtDto createBeginningCylinderDebt(CreateBeginningCylinderDebtRequest request, String factoryId);

    BeginningCylinderDebtDto updateBeginningCylinderDebt(CreateBeginningCylinderDebtRequest request, String id);

    void deleteBeginningCylinderDebt(String id);

//    Integer forwardToNextYear(Integer yearFrom, Integer yearTo, String factoryId, String userFullName);

}
