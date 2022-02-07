package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateCustomerPriceRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerPriceRequest;
import com.ceti.wholesale.dto.CustomerPriceDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.CustomerPrice;
import com.ceti.wholesale.repository.CustomerPriceRepository;
import com.ceti.wholesale.service.CustomerPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerPriceServiceImpl implements CustomerPriceService {

    @Autowired
    private CustomerPriceRepository customerPriceRepository;

    @Value(value = "${ceti.service.zoneId}")
    private String zoneId;

    @Override
    public Page<CustomerPriceDto> getAllByCondition(String customerName, String customerCategory, Integer month,
                                                    Integer year,
                                                    String factory_id, Pageable pageable) {

        Page<CustomerPrice> page = customerPriceRepository.getAllByCondition(customerName, customerCategory,
                month, year, factory_id, pageable);

        return CommonMapper.toPage(page, CustomerPriceDto.class, pageable);
    }

    @Override
    public CustomerPriceDto createCustomerPrice(CreateCustomerPriceRequest request, String factoryId) {
        if (customerPriceRepository.existsByCustomerIdAndMonthAndYearAndFactoryId(request.getCustomerId(), request.getMonth(), request.getYear(), factoryId)) {
            throw new BadRequestException("Khách hàng đã tồn tại");
        } else {
            CustomerPrice customerPrice = CommonMapper.map(request, CustomerPrice.class);
            Instant now = Instant.now();
            customerPrice.setCreatedAt(now);
            customerPrice.setFactoryId(factoryId);
            customerPriceRepository.save(customerPrice);

            return CommonMapper.map(customerPrice, CustomerPriceDto.class);
        }
    }

    @Override
    @Transactional
    public List<CustomerPriceDto> updateCustomerPrice(List<UpdateCustomerPriceRequest> request, String factoryId) {
        List<CustomerPrice> customerPrices = new ArrayList<>();
        List<CustomerPriceDto> customerPriceDtos = new ArrayList<>();

        CustomerPrice temp = null;
        if (!request.isEmpty()) {
            for (UpdateCustomerPriceRequest updateCustomerPriceRequest : request) {

                if (temp == null) {
                    Optional<CustomerPrice> optional = customerPriceRepository.findById(updateCustomerPriceRequest.getId());
                    if (!optional.isPresent()) {
                        throw new NotFoundException("Giá khách hàng không tồn tại");
                    }
                    temp = optional.get();

                }
                customerPriceRepository.deleteById(updateCustomerPriceRequest.getId());
                CustomerPrice customerPrice = CommonMapper.map(updateCustomerPriceRequest, CustomerPrice.class);
                customerPrice.setFactoryId(factoryId);
                customerPrices.add(customerPrice);
                customerPrice.setMonth(temp.getMonth());
                customerPrice.setYear(temp.getYear());
                customerPriceRepository.save(customerPrice);
                customerPriceDtos.add(CommonMapper.map(customerPrice, CustomerPriceDto.class));
            }
        }
        return customerPriceDtos;
    }

    @Override
    public Boolean setForwardCustomerPrice(Integer monthFrom, Integer yearFrom, Integer monthTo, Integer yearTo,
                                           String factoryId) {
        return customerPriceRepository.setForwardCustomerPrice(monthFrom, yearFrom, monthTo, yearTo, factoryId);
    }

    @Override
    public Boolean applyCustomerPrice(Integer monthTo, Integer yearTo, String factoryId) {
        return customerPriceRepository.applyCustomerPrice(monthTo, yearTo, factoryId);
    }

    @Transactional
    @Override
    public Boolean deleteCustomerPrice(List<UpdateCustomerPriceRequest> request, String factoryId) {
        for (UpdateCustomerPriceRequest updateCustomerPriceRequest : request) {
            if (customerPriceRepository.existsById(updateCustomerPriceRequest.getId())) {
                customerPriceRepository.deleteById(updateCustomerPriceRequest.getId());
            }
        }
        return true;
    }

}
