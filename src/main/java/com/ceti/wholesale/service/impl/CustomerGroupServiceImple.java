package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateCustomerGroupRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerGroupRequest;
import com.ceti.wholesale.dto.CustomerGroupDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.CustomerGroup;
import com.ceti.wholesale.repository.CustomerGroupRepository;
import com.ceti.wholesale.service.CustomerGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerGroupServiceImple implements CustomerGroupService {

    @Autowired
    private CustomerGroupRepository customerGroupRepository;

    @Override
    public Page<CustomerGroupDto> getAllByCondition(String search, String id, String name, String factoryId, Boolean isActive, Pageable pageable) {
        Page<CustomerGroup> page = customerGroupRepository.getAllByCondition(search, id, name, factoryId, isActive, pageable);
        return CommonMapper.toPage(page, CustomerGroupDto.class, pageable);
    }

    //Create new Customer group
    @Override
    public CustomerGroupDto createCustomerGroup(CreateCustomerGroupRequest request, String factoryId) {
        if (customerGroupRepository.existsById(request.getId())) {
            throw new BadRequestException("Mã tuyến đã tồn tại");
        }
        CustomerGroup customerGroup = new CustomerGroup();
        CommonMapper.copyPropertiesIgnoreNull(request, customerGroup);
        customerGroup.setFactoryId(factoryId);
        customerGroup = customerGroupRepository.save(customerGroup);
        return CommonMapper.map(customerGroup, CustomerGroupDto.class);

    }

    //update Customer group
    @Override
    public CustomerGroupDto updateCustomerGroup(String id, UpdateCustomerGroupRequest request) {
        Optional<CustomerGroup> optional = customerGroupRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Mã tuyến không tồn tại");
        }
        CustomerGroup customerGroup = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(request, customerGroup);
        customerGroup = customerGroupRepository.save(customerGroup);
        return CommonMapper.map(customerGroup, CustomerGroupDto.class);
    }

}
