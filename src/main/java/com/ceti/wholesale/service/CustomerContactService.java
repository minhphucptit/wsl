package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateCustomerContactRequest;
import com.ceti.wholesale.controller.api.request.CreateListCustomerContactRequest;
import com.ceti.wholesale.controller.api.request.CreateListProductAccessoryRequest;
import com.ceti.wholesale.dto.CustomerContactDto;
import com.ceti.wholesale.dto.ProductAccessoryDto;

import java.util.List;


public interface CustomerContactService {

    List<CustomerContactDto> getAllByCustomerId(String customerId);

    List<CustomerContactDto> updateByCustomerId(CreateListCustomerContactRequest request, String customerId);
}
