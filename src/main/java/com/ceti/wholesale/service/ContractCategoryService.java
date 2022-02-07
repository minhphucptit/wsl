package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateContractCategoryRequest;
import com.ceti.wholesale.controller.api.request.UpdateContractCategoryRequest;
import com.ceti.wholesale.dto.ContractCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractCategoryService {

      public Page<ContractCategoryDto> getAllByConditions(String name, Boolean isActive, Pageable pageable);

      public ContractCategoryDto createContractCategory(CreateContractCategoryRequest createContractCategoryRequest);

      public ContractCategoryDto updateContractCategory(String contractCategoryId,
      UpdateContractCategoryRequest updateContractCategoryRequest);
}
