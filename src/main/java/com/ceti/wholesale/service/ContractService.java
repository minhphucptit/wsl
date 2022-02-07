package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateContractRequest;
import com.ceti.wholesale.controller.api.request.UpdateContractRequest;
import com.ceti.wholesale.dto.ContractDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractService {
//      public Page<ContractDto> getAllByConditions(String contractNumber,Long signDateFrom,Long signDateTo,
//      Long expireDateFrom,Long expireDateTo,String status,String deliveryMethod,String contractCategoryId, String factoryId,
//      Pageable pageable);

      ContractDto createContract(CreateContractRequest createContractRequest);

      ContractDto updateContract(String contractNumber,UpdateContractRequest updateContractRequest);
}
