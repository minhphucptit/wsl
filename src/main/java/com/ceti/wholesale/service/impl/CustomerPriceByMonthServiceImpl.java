package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateCustomerPriceByMonthRequest;
import com.ceti.wholesale.controller.api.request.CreateCustomerPriceRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerPriceByMonthRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerPriceRequest;
import com.ceti.wholesale.dto.CustomerPriceByMonthDto;
import com.ceti.wholesale.dto.CustomerPriceDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.CustomerPriceByMonth;
import com.ceti.wholesale.repository.CustomerPriceByMonthRepository;
import com.ceti.wholesale.service.CustomerPriceByMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerPriceByMonthServiceImpl implements CustomerPriceByMonthService {

    @Autowired
    CustomerPriceByMonthRepository customerPriceByMonthRepository;

    @Value(value = "${ceti.service.zoneId}")
    private String zoneId;

    @Override
    public Page<CustomerPriceByMonthDto> getAllByCondition(String customerName, String customerCategory, Integer year, String factory_id, Pageable pageable) {
        Page<CustomerPriceByMonth> page = customerPriceByMonthRepository.getAllByCondition(customerName, customerCategory,year, factory_id, pageable);

        return CommonMapper.toPage(page, CustomerPriceByMonthDto.class, pageable);
    }

    @Override
    public CustomerPriceByMonthDto createCustomerPriceByMonth(CreateCustomerPriceByMonthRequest request, String factoryId) {
        if (customerPriceByMonthRepository.existsByCustomerIdAndYearAndFactoryId(request.getCustomerId(), request.getYear(), request.getFactoryId())) {
            throw new BadRequestException("Khách hàng đã tồn tại");
        } else {
            Instant now = Instant.now();
            LocalDate localDataNow = LocalDate.ofInstant(now, ZoneId.of(zoneId));
            CustomerPriceByMonth customerPricebyMonth = CommonMapper.map(request, CustomerPriceByMonth.class);
            customerPricebyMonth.setCreatedAt(now);
//            customerPricebyMonth.setYear(localDataNow.getYear());
            customerPricebyMonth.setFactoryId(factoryId);
            customerPriceByMonthRepository.save(customerPricebyMonth);

            return CommonMapper.map(customerPricebyMonth, CustomerPriceByMonthDto.class);
        }
    }

    @Override
    public List<CustomerPriceByMonthDto> updateCustomerPriceByMonth(List<UpdateCustomerPriceByMonthRequest> request, String factoryId) {
        List<CustomerPriceByMonth> customerPriceByMonths = new ArrayList<>();
        List<CustomerPriceByMonthDto> customerPriceByMonthDtos = new ArrayList<>();

        CustomerPriceByMonth temp = null;
        if (!request.isEmpty()) {
            for (UpdateCustomerPriceByMonthRequest updateCustomerPriceByMonthRequest : request) {

                if(temp==null){
                    Optional<CustomerPriceByMonth> optional=customerPriceByMonthRepository.findById(updateCustomerPriceByMonthRequest.getId());
                    if(!optional.isPresent()){
                        throw new NotFoundException("Giá khách hàng không tồn tại");
                    }
                    temp = optional.get();

                }

                customerPriceByMonthRepository.deleteById(updateCustomerPriceByMonthRequest.getId());
                CustomerPriceByMonth customerPricebyMonth = CommonMapper.map(updateCustomerPriceByMonthRequest, CustomerPriceByMonth.class);
                customerPricebyMonth.setFactoryId(factoryId);
                customerPriceByMonths.add(customerPricebyMonth);
                customerPricebyMonth.setYear(temp.getYear());
                customerPriceByMonthRepository.save(customerPricebyMonth);
                customerPriceByMonthDtos.add(CommonMapper.map(customerPricebyMonth, CustomerPriceByMonthDto.class));
            }
        }
        return customerPriceByMonthDtos;
    }

    @Override
    public Boolean setForwardCustomerPriceByMonth(Integer yearFrom, Integer yearTo, String factoryId) {
        return customerPriceByMonthRepository.setForwardCustomerPriceByMonth(yearFrom, yearTo, factoryId);
    }

    @Override
    public Boolean applyCustomerPriceByMonth(Integer year, String factoryId) {
        return customerPriceByMonthRepository.applyCustomerPriceByMonth(year, factoryId);
    }

    @Transactional
    @Override
    public Boolean deleteCustomerPriceByMonth(List<UpdateCustomerPriceByMonthRequest> request, String factoryId) {
        for (UpdateCustomerPriceByMonthRequest updateCustomerPriceByMonthRequest : request) {
            if (customerPriceByMonthRepository.existsById(updateCustomerPriceByMonthRequest.getId())) {
                customerPriceByMonthRepository.deleteById(updateCustomerPriceByMonthRequest.getId());
            }
        }
        return true;
    }
}
