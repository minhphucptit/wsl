package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateCustomerProductDiscountRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerProductDiscountRequest;
import com.ceti.wholesale.dto.CustomerDto;
import com.ceti.wholesale.dto.CustomerProductDiscountDto;
import com.ceti.wholesale.dto.FactoryDto;
import com.ceti.wholesale.dto.ProductDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.CustomerProductDiscount;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.Product;
import com.ceti.wholesale.repository.CustomerProductDiscountRepository;
import com.ceti.wholesale.service.CustomerProductDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class CustomerProductDiscountServiceImpl implements CustomerProductDiscountService {

    @Autowired
    private CustomerProductDiscountRepository customerProductDiscountRepository;

    @Override
    public CustomerProductDiscountDto createCustomerProductDiscount(CreateCustomerProductDiscountRequest request, String userId) {
        if(customerProductDiscountRepository.existsByProductIdAndCustomerId(request.getProductId(),request.getCustomerId())){
            throw new BadRequestException("Chiết khấu cho sản phẩm: "+request.getProductId()+ " của khách hàng"+request.getCustomerId()+" đã tồn tại");
        }
        CustomerProductDiscount customerProductDiscount = new CustomerProductDiscount();
        CommonMapper.copyPropertiesIgnoreNull(request,customerProductDiscount);
        customerProductDiscount.setUpdateBy(userId);
        customerProductDiscount.setUpdateAt(Instant.now());
        customerProductDiscountRepository.save(customerProductDiscount);
        return CommonMapper.map(customerProductDiscount,CustomerProductDiscountDto.class);
    }

    @Override
    public CustomerProductDiscountDto updateCustomerProductDiscount(String id, UpdateCustomerProductDiscountRequest request,String userId) {
        Optional<CustomerProductDiscount> optional = customerProductDiscountRepository.findById(id);
        if(!optional.isPresent()){
            throw  new NotFoundException("Chiết khấu khách hàng không tồn tại");
        }
        CustomerProductDiscount customerProductDiscount = optional.get();
        if(!customerProductDiscount.getCustomerId().equals(request.getCustomerId())||!customerProductDiscount.getProductId().equals(request.getProductId())){
            if(customerProductDiscountRepository.existsByProductIdAndCustomerId(request.getProductId(),request.getCustomerId())){
                throw new BadRequestException("Chiết khấu cho sản phẩm: "+request.getProductId()+ " của khách hàng"+request.getCustomerId()+" đã tồn tại");
            }
        }
        CommonMapper.copyPropertiesIgnoreNull(request,customerProductDiscount);
        customerProductDiscount.setUpdateBy(userId);
        customerProductDiscount.setUpdateAt(Instant.now());
        customerProductDiscountRepository.save(customerProductDiscount);
        return CommonMapper.map(customerProductDiscount,CustomerProductDiscountDto.class);
    }

    @Override
    public Page<CustomerProductDiscountDto> findAll(Pageable pageable, String[] embed, MultiValueMap<String, String> where) {
        if(embed == null){
            ResultPage<CustomerProductDiscount> rs = customerProductDiscountRepository.findAll(pageable,where);
            long total = rs.getTotalItems();
            List<CustomerProductDiscount> customerProductDiscounts = rs.getPageList();
            return new PageImpl<CustomerProductDiscountDto>(CommonMapper.toList(customerProductDiscounts,CustomerProductDiscountDto.class),pageable,total);
        }
        List<String> embedTable = new ArrayList<>();
        List<String> allTable= new ArrayList<>(Arrays.asList("customer","product","factory"));
        for(String i :embed) {
            if (!allTable.contains(i)) {
                throw new BadRequestException("Table name \'" + i + "\' cannot be obtained!");
            }
            if (!embedTable.contains(i)) {
                embedTable.add(i);
            }
        }
            ResultPage<Object[]> rs = customerProductDiscountRepository.findAllWithEmbed(pageable,embedTable,where);

            List<Object[]> customerProductDiscountDetails = rs.getPageList();
            List<CustomerProductDiscountDto> listCustomerProductDiscountDto = new ArrayList<>();
            long total = rs.getTotalItems();

            customerProductDiscountDetails.stream().forEach((record)->{
                CustomerProductDiscount customerProductDiscount = (CustomerProductDiscount) record[0];
                CustomerProductDiscountDto customerProductDiscountDto1= CommonMapper.map(customerProductDiscount,CustomerProductDiscountDto.class);
                Integer temp = 1;
                if(embedTable.contains("customer")){
                    Customer customer = (Customer)record[temp++];
                    if(customer!=null){
                        customerProductDiscountDto1.setCustomer(CommonMapper.map(customer, CustomerDto.class));
                    }
                }
                if(embedTable.contains("product")){
                    Product product = (Product) record[temp++];
                    if(product!=null){
                        customerProductDiscountDto1.setProduct(CommonMapper.map(product, ProductDto.class));
                    }
                }
                if(embedTable.contains("factory")){
                    Factory factory = (Factory)record[temp++];
                    if(factory!=null){
                        customerProductDiscountDto1.setFactory(CommonMapper.map(factory, FactoryDto.class));
                    }
                }
                listCustomerProductDiscountDto.add(customerProductDiscountDto1);
            });
        return new PageImpl<>(listCustomerProductDiscountDto,pageable,total);
    }
}
