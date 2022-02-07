package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateCustomerProductDiscountRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerProductDiscountRequest;
import com.ceti.wholesale.dto.CustomerProductDiscountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface CustomerProductDiscountService {
    CustomerProductDiscountDto createCustomerProductDiscount(CreateCustomerProductDiscountRequest request, String userId);

    CustomerProductDiscountDto updateCustomerProductDiscount(String id, UpdateCustomerProductDiscountRequest request,String userId);

    Page<CustomerProductDiscountDto> findAll(Pageable pageable, String[] embed, MultiValueMap<String, String> where);
}
