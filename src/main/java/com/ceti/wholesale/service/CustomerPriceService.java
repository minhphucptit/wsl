package com.ceti.wholesale.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ceti.wholesale.controller.api.request.CreateCustomerPriceRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerPriceRequest;
import com.ceti.wholesale.dto.CustomerPriceDto;

public interface CustomerPriceService {

  Page<CustomerPriceDto> getAllByCondition(String customerName, String customerCategory, Integer month, Integer year,
      String factory_id, Pageable pageable);

  CustomerPriceDto createCustomerPrice(CreateCustomerPriceRequest request, String factory_id);

  List<CustomerPriceDto> updateCustomerPrice(List<UpdateCustomerPriceRequest> request, String factoryId);

  Boolean setForwardCustomerPrice(Integer monthFrom, Integer yearFrom, Integer monthTo, Integer yearTo,
      String factoryId);

  Boolean applyCustomerPrice(Integer monthTo, Integer yearTo,String factoryId);

  Boolean deleteCustomerPrice(List<UpdateCustomerPriceRequest> request, String factoryId);

}
