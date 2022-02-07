package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateCustomerProductPriceRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerProductPriceRequest;
import com.ceti.wholesale.dto.CustomerProductPriceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface CustomerProductPriceService {
    CustomerProductPriceDto createCustomerProductPrice(CreateCustomerProductPriceRequest request,String userId);

    CustomerProductPriceDto updateCustomerProductPrice(String id, UpdateCustomerProductPriceRequest request,String userId);

    Page<CustomerProductPriceDto> findAll(Pageable pageable, String[] embed, MultiValueMap<String, String> where);
}
