package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateCustomerProductPriceRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerProductPriceRequest;
import com.ceti.wholesale.dto.CustomerDto;
import com.ceti.wholesale.dto.CustomerProductPriceDto;
import com.ceti.wholesale.dto.FactoryDto;
import com.ceti.wholesale.dto.ProductDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.CustomerProductPrice;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.Product;
import com.ceti.wholesale.repository.CustomerProductPriceRepository;
import com.ceti.wholesale.service.CustomerProductPriceService;
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
public class CustomerProductPriceServiceImpl implements CustomerProductPriceService {

    @Autowired
    private CustomerProductPriceRepository customerProductPriceRepository;

    @Override
    public CustomerProductPriceDto createCustomerProductPrice(CreateCustomerProductPriceRequest request,String userId) {
        if(customerProductPriceRepository.existsByProductIdAndCustomerId(request.getProductId(),request.getCustomerId())){
            throw new BadRequestException("Giá cho sản phẩm: "+request.getProductId()+ " của khách hàng"+request.getCustomerId()+" đã tồn tại");
        }
        CustomerProductPrice customerProductPrice = new CustomerProductPrice();
        CommonMapper.copyPropertiesIgnoreNull(request,customerProductPrice);
        customerProductPrice.setUpdateBy(userId);
        customerProductPrice.setUpdateAt(Instant.now());
        customerProductPriceRepository.save(customerProductPrice);
        return CommonMapper.map(customerProductPrice,CustomerProductPriceDto.class);
    }

    @Override
    public CustomerProductPriceDto updateCustomerProductPrice(String id, UpdateCustomerProductPriceRequest request,String userId) {
        Optional<CustomerProductPrice> optional = customerProductPriceRepository.findById(id);
        if(!optional.isPresent()){
            throw  new NotFoundException("Giá khách hàng không tồn tại");
        }
        CustomerProductPrice customerProductPrice = optional.get();
        if(!customerProductPrice.getCustomerId().equals(request.getCustomerId())||!customerProductPrice.getProductId().equals(request.getProductId())){
            if(customerProductPriceRepository.existsByProductIdAndCustomerId(request.getProductId(),request.getCustomerId())){
                throw new BadRequestException("Giá cho sản phẩm: "+request.getProductId()+ " của khách hàng"+request.getCustomerId()+" đã tồn tại");
            }
        }
        CommonMapper.copyPropertiesIgnoreNull(request,customerProductPrice);
        customerProductPrice.setUpdateBy(userId);
        customerProductPrice.setUpdateAt(Instant.now());
        customerProductPriceRepository.save(customerProductPrice);
        return CommonMapper.map(customerProductPrice,CustomerProductPriceDto.class);
    }

    @Override
    public Page<CustomerProductPriceDto> findAll(Pageable pageable, String[] embed, MultiValueMap<String, String> where) {
        if(embed == null){
            ResultPage<CustomerProductPrice> rs = customerProductPriceRepository.findAll(pageable,where);
            long total = rs.getTotalItems();
            List<CustomerProductPrice> customerProductPrices = rs.getPageList();
            return new PageImpl<CustomerProductPriceDto>(CommonMapper.toList(customerProductPrices,CustomerProductPriceDto.class),pageable,total);
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
            ResultPage<Object[]> rs = customerProductPriceRepository.findAllWithEmbed(pageable,embedTable,where);

            List<Object[]> customerProductPriceDetails = rs.getPageList();
            List<CustomerProductPriceDto> listCustomerProductPriceDto = new ArrayList<>();
            long total = rs.getTotalItems();

            customerProductPriceDetails.stream().forEach((record)->{
                CustomerProductPrice customerProductPrice = (CustomerProductPrice) record[0];
                CustomerProductPriceDto customerProductPriceDto1= CommonMapper.map(customerProductPrice,CustomerProductPriceDto.class);
                Integer temp = 1;
                if(embedTable.contains("customer")){
                    Customer customer = (Customer)record[temp++];
                    if(customer!=null){
                        customerProductPriceDto1.setCustomer(CommonMapper.map(customer, CustomerDto.class));
                    }
                }
                if(embedTable.contains("product")){
                    Product product = (Product) record[temp++];
                    if(product!=null){
                        customerProductPriceDto1.setProduct(CommonMapper.map(product, ProductDto.class));
                    }
                }
                if(embedTable.contains("factory")){
                    Factory factory = (Factory)record[temp++];
                    if(factory!=null){
                        customerProductPriceDto1.setFactory(CommonMapper.map(factory, FactoryDto.class));
                    }
                }
                listCustomerProductPriceDto.add(customerProductPriceDto1);
            });
        return new PageImpl<>(listCustomerProductPriceDto,pageable,total);
    }
}
