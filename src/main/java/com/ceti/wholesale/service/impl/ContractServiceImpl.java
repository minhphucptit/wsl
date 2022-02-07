package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateContractRequest;
import com.ceti.wholesale.controller.api.request.UpdateContractRequest;
import com.ceti.wholesale.dto.ContractDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.Contract;
import com.ceti.wholesale.repository.ContractCategoryRepository;
import com.ceti.wholesale.repository.ContractRepository;
import com.ceti.wholesale.service.ContractService;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContractServiceImpl implements ContractService {

      @Autowired
      ContractRepository contractRepository;

      @Autowired
      ContractCategoryRepository contractCategoryRepository;

//      @Override
//      public Page<ContractDto> getAllByConditions(String contractNumber,Long signDateFrom,Long signDateTo,
//      Long expireDateFrom,Long expireDateTo,String status,String deliveryMethod,String contractCategoryId, String factoryId,
//      Pageable pageable) {
//
//            Instant signFrom = signDateFrom == null?null: Instant.ofEpochSecond(signDateFrom);
//            Instant signTo = signDateTo == null?null: Instant.ofEpochSecond(signDateTo);
//            Instant expireFrom = expireDateFrom == null?null: Instant.ofEpochSecond(expireDateFrom);
//            Instant expireTo = expireDateTo == null?null: Instant.ofEpochSecond(expireDateTo);
//            Page<Contract> page = contractRepository.getAllByConditions(contractNumber,signFrom,signTo,expireFrom,
//            expireTo,status,deliveryMethod,contractCategoryId, factoryId, pageable);
//
//            return CommonMapper.toPage(page, ContractDto.class, pageable);
//      }

      @Override
      public ContractDto createContract(CreateContractRequest createContractRequest) {

            String contractCategoryId = createContractRequest.getContractCategoryId();

            if(!contractCategoryRepository.existsById(contractCategoryId)){
                  throw  new NotFoundException("Mã loại hợp đồng không tồn tại");
            }

            Contract contract = CommonMapper.map(createContractRequest,Contract.class);
            contractRepository.save(contract);

            return CommonMapper.map(contract,ContractDto.class);
      }

      @Override
      public ContractDto updateContract(String id, UpdateContractRequest updateContractRequest) {
            Optional<Contract> optional = contractRepository.findById(id);

            if(optional.isEmpty()){
                  throw new NotFoundException("Mã hợp đồng không tồn tại");
            }

            Contract contract = optional.get();

            CommonMapper.copyPropertiesIgnoreNull(updateContractRequest,contract);

            contractRepository.save(contract);

            return CommonMapper.map(contract,ContractDto.class);
      }
}
