package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateContractCategoryRequest;
import com.ceti.wholesale.controller.api.request.UpdateContractCategoryRequest;
import com.ceti.wholesale.dto.ContractCategoryDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.ContractCategory;
import com.ceti.wholesale.repository.ContractCategoryRepository;
import com.ceti.wholesale.service.ContractCategoryService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContractCategoryServiceImpl implements ContractCategoryService {

      @Autowired
      ContractCategoryRepository contractCategoryRepository;

      @Override
      public Page<ContractCategoryDto> getAllByConditions(String name, Boolean isActive, Pageable pageable) {
            Page<ContractCategory> page = contractCategoryRepository.getAllByConditions(name,isActive,pageable);

            return CommonMapper.toPage(page,ContractCategoryDto.class,pageable);
      }

      @Override
      public ContractCategoryDto createContractCategory(CreateContractCategoryRequest createContractCategoryRequest) {

            ContractCategory contractCategory = CommonMapper.map(createContractCategoryRequest,ContractCategory.class);

            contractCategoryRepository.save(contractCategory);

            return CommonMapper.map(contractCategory,ContractCategoryDto.class);
      }

      @Override
      public ContractCategoryDto updateContractCategory(String contractCategoryId,
      UpdateContractCategoryRequest updateContractCategoryRequest) {
            Optional<ContractCategory> optional = contractCategoryRepository.findById(contractCategoryId);

            if(optional.isEmpty()){
                  throw new NotFoundException("Mã loại hợp đồng không tồn tại");
            }

            ContractCategory contractCategory = optional.get();

            CommonMapper.copyPropertiesIgnoreNull(updateContractCategoryRequest,contractCategory);

            contractCategoryRepository.save(contractCategory);

            return CommonMapper.map(contractCategory,ContractCategoryDto.class);
      }
}
