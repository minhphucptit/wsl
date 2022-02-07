package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateCustomerGroupRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerGroupRequest;
import com.ceti.wholesale.dto.CustomerGroupDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerGroupService {

    CustomerGroupDto createCustomerGroup(CreateCustomerGroupRequest request, String factoryId);

    CustomerGroupDto updateCustomerGroup(String id, UpdateCustomerGroupRequest request);

    Page<CustomerGroupDto> getAllByCondition(String search, String id, String name, String factoryId, Boolean isActive,
                                             Pageable pageable);
}
