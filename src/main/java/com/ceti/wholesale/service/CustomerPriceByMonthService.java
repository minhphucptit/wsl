package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateCustomerPriceByMonthRequest;
import com.ceti.wholesale.controller.api.request.CreateCustomerPriceRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerPriceByMonthRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerPriceRequest;
import com.ceti.wholesale.dto.CustomerPriceByMonthDto;
import com.ceti.wholesale.dto.CustomerPriceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerPriceByMonthService {
    Page<CustomerPriceByMonthDto> getAllByCondition(String customerName, String customerCategory, Integer year,
                                                    String factory_id, Pageable pageable);

    CustomerPriceByMonthDto createCustomerPriceByMonth(CreateCustomerPriceByMonthRequest request, String factoryId);

    List<CustomerPriceByMonthDto> updateCustomerPriceByMonth(List<UpdateCustomerPriceByMonthRequest> request, String factoryId);

    Boolean setForwardCustomerPriceByMonth(Integer yearFrom, Integer yearTo,
                                    String factoryId);

    Boolean applyCustomerPriceByMonth(Integer year,String factoryId);

    Boolean deleteCustomerPriceByMonth(List<UpdateCustomerPriceByMonthRequest> request, String factoryId);
}
