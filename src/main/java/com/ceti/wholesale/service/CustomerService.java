package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateCustomerRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerRequest;
import com.ceti.wholesale.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface CustomerService {

//    Page<CustomerDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    Page<CustomerDto> getAllByConditionWithEmbed(MultiValueMap<String, String> where, Pageable pageable);

    CustomerDto createCustomer(CreateCustomerRequest request);

    CustomerDto updateCustomer(String customerId, UpdateCustomerRequest request);

}
