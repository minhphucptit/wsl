package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.dto.CustomerCategoryDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.repository.CustomerCategoryRepository;
import com.ceti.wholesale.service.CustomerCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CustomerCategoryServiceImpl implements CustomerCategoryService {

    @Autowired
    private CustomerCategoryRepository customerCategoryRepository;

    @Override
    public Page<CustomerCategoryDto> getAll(Pageable pageable) {
         return CommonMapper.toPage(customerCategoryRepository.findAll(pageable),CustomerCategoryDto.class,pageable);
    }
}
