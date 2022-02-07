package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateCustomerRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerRequest;
import com.ceti.wholesale.dto.CustomerCategoryDto;
import com.ceti.wholesale.dto.CustomerDto;
import com.ceti.wholesale.dto.FactoryDto;
import com.ceti.wholesale.dto.RegionDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.CustomerCategory;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.Region;
import com.ceti.wholesale.repository.CustomerDetailRepository;
import com.ceti.wholesale.repository.CustomerRepository;
import com.ceti.wholesale.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    CustomerDetailRepository customerDetailRepository;

//    @Override
//    public Page<CustomerDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
//        ResultPage<Customer> page = customerDetailRepository.findAllWithFilter(pageable, where);
//        List<CustomerDto> customerDto = new ArrayList<>();
//        for (Customer customer : page.getPageList()) {
//            customerDto
//                    .add(CommonMapper.map(customer, CustomerDto.class));
//        }
//        return new PageImpl<>(customerDto, pageable, page.getTotalItems());
//    }

    @Override
    public Page<CustomerDto> getAllByConditionWithEmbed(MultiValueMap<String, String> where, Pageable pageable) {
        ResultPage<Object[]> page=customerDetailRepository.findAllWithEmbed(pageable,where);
        List<CustomerDto> customerList = new ArrayList<>();
        for(Object[] objects:page.getPageList()){
            Customer customer = (Customer) objects[0];
            CustomerDto customerDto = CommonMapper.map(customer,CustomerDto.class);
            if(objects[1]!=null){
                Factory factory = (Factory) objects[1];
                FactoryDto factoryDto = CommonMapper.map(factory,FactoryDto.class);
                customerDto.setFactory(factoryDto);
            }
            if(objects[2]!=null){
                Region region = (Region) objects[2];
                RegionDto regionDto = CommonMapper.map(region,RegionDto.class);
                customerDto.setRegionDto(regionDto);
            }
            if(objects[3]!=null){
                CustomerCategory customerCategory = (CustomerCategory) objects[3];
                CustomerCategoryDto customerCategoryDto = CommonMapper.map(customerCategory,CustomerCategoryDto.class);
                customerDto.setCustomerCategory(customerCategoryDto);
            }
            customerList.add(customerDto);
        }
        return new PageImpl<>(customerList, pageable, page.getTotalItems());
    }

    //create new customer
    @Override
    public CustomerDto createCustomer(CreateCustomerRequest request) {

        Customer customer = new Customer();
        CommonMapper.copyPropertiesIgnoreNull(request, customer);
        customer = customerRepository.save(customer);
        return CommonMapper.map(customer, CustomerDto.class);

    }

    //update customer
    @Override
    public CustomerDto updateCustomer(String customerId, UpdateCustomerRequest request) {
        Optional<Customer> optional = customerRepository.findById(customerId);
        if (optional.isEmpty()) {
            throw new NotFoundException("Mã khách hàng không tồn tại");
        }
        Customer customer = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(request, customer);
        customer = customerRepository.save(customer);
        return CommonMapper.map(customer, CustomerDto.class);
    }
}
