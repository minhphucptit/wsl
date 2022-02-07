package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.controller.api.request.CreateCustomerContactRequest;
import com.ceti.wholesale.controller.api.request.CreateListCustomerContactRequest;
import com.ceti.wholesale.controller.api.request.CreateListProductAccessoryRequest;
import com.ceti.wholesale.controller.api.request.CreateProductAccessoryRequest;
import com.ceti.wholesale.dto.CustomerContactDto;
import com.ceti.wholesale.dto.ProductAccessoryDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.CustomerContact;
import com.ceti.wholesale.model.ProductAccessory;
import com.ceti.wholesale.repository.CustomerContactRepository;
import com.ceti.wholesale.repository.ProductAccessoryRepository;
import com.ceti.wholesale.service.CustomerContactService;
import com.ceti.wholesale.service.ProductAccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerContactServiceImpl implements CustomerContactService {

    @Autowired
    private CustomerContactRepository customerContactRepository;

    @Override
    public List<CustomerContactDto> getAllByCustomerId(String customerId) {
        List<CustomerContact> customerContactList = customerContactRepository.findByCustomerId(customerId);
        return CommonMapper.toList(customerContactList, CustomerContactDto.class);
    }

    @Override
    public List<CustomerContactDto> updateByCustomerId(CreateListCustomerContactRequest request, String customerId) {
        customerContactRepository.deleteByCustomerId(customerId);
        List<CustomerContactDto> customerContactDtos = new ArrayList<>();
        for (CreateCustomerContactRequest createCustomerContactRequest : request.getCustomerContact()){
            CustomerContact customerContact = CommonMapper.map(createCustomerContactRequest, CustomerContact.class);
            customerContact.setCustomerId(customerId);
            customerContactRepository.save(customerContact);
            CustomerContactDto customerContactDto = CommonMapper.map(customerContact, CustomerContactDto.class);
            customerContactDtos.add(customerContactDto);
        }
        return customerContactDtos;
    }
}
