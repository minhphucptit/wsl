package com.ceti.wholesale.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateBeginningCylinderDebtRequest;
import com.ceti.wholesale.dto.BeginningCylinderDebtDto;
import com.ceti.wholesale.dto.CustomerDto;
import com.ceti.wholesale.dto.ProductDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.BeginningCylinderDebt;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.Product;
import com.ceti.wholesale.repository.BeginningCylinderDebtDetailRepository;
import com.ceti.wholesale.repository.BeginningCylinderDebtRepository;
import com.ceti.wholesale.service.BeginningCylinderDebtService;

@Service
@Transactional
public class BeginningCylinderDebtServiceImpl implements BeginningCylinderDebtService {

    @Autowired
    private BeginningCylinderDebtRepository beginningCylinderDebtRepository;

    @Autowired
    private BeginningCylinderDebtDetailRepository beginningCylinderDebtDetailRepository;

    @Override
    public Page<BeginningCylinderDebtDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
        ResultPage<Object[]> page = beginningCylinderDebtDetailRepository.findAllWithFilter(pageable, where);
        List<BeginningCylinderDebtDto> beginningCylinderDebtDtos = new ArrayList<>();
        for (Object[] object : page.getPageList()) {
            BeginningCylinderDebt bcd = (BeginningCylinderDebt) object[0];
            BeginningCylinderDebtDto beginningCylinderDebtDto = CommonMapper.map(bcd, BeginningCylinderDebtDto.class);

            if (object[1] != null) {
                Customer customer = (Customer) object[1];
                CustomerDto customerDto = CommonMapper.map(customer, CustomerDto.class);
                beginningCylinderDebtDto.setCustomer(customerDto);
            }

            if (object[2] != null) {
                Product product = (Product) object[2];
                ProductDto productDto = CommonMapper.map(product, ProductDto.class);
                beginningCylinderDebtDto.setProduct(productDto);
            }

            beginningCylinderDebtDtos.add(beginningCylinderDebtDto);
        }
        return new PageImpl<>(beginningCylinderDebtDtos, pageable, page.getTotalItems());
    }

    @Override
    public BeginningCylinderDebtDto createBeginningCylinderDebt(CreateBeginningCylinderDebtRequest request,
                                                                String factoryId) {
        Instant now = Instant.now();
        BeginningCylinderDebt beginningCylinderDebt = CommonMapper.map(request, BeginningCylinderDebt.class);
        beginningCylinderDebt.setCreateAt(now);
        beginningCylinderDebt.setUpdateAt(now);
        beginningCylinderDebt.setUpdateBy(beginningCylinderDebt.getCreateBy());
        beginningCylinderDebt
                .setId(request.getYear() + request.getCustomerId() + request.getProductId() + factoryId);
        beginningCylinderDebt.setFactoryId(factoryId);
        if (beginningCylinderDebtRepository.existsById(beginningCylinderDebt.getId())) {
            throw new BadRequestException(
                    "Đã tồn tại bản ghi đầu kì công nợ vỏ " + request.getProductId() + " của khách hàng " + request.getCustomerId());
        }
        beginningCylinderDebtRepository.save(beginningCylinderDebt);
        return CommonMapper.map(beginningCylinderDebt, BeginningCylinderDebtDto.class);
    }

    @Override
    public BeginningCylinderDebtDto updateBeginningCylinderDebt(CreateBeginningCylinderDebtRequest request, String id) {
        Optional<BeginningCylinderDebt> optional = beginningCylinderDebtRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("đầu kì công nợ vỏ khách hàng không tồn tại");
        }
        BeginningCylinderDebt beginningCylinderDebt = optional.get();
        BeginningCylinderDebt newBeginningCylinderDebt = CommonMapper.map(request, BeginningCylinderDebt.class);
        newBeginningCylinderDebt.setId(request.getYear() + request.getCustomerId() + request.getProductId() + beginningCylinderDebt.getFactoryId());
        if (!newBeginningCylinderDebt.getId().equals(beginningCylinderDebt.getId()) && beginningCylinderDebtRepository
                .existsById(newBeginningCylinderDebt.getId())) {
            throw new BadRequestException(
                    "Đã tồn tại bản ghi đầu kì công nợ vỏ " + request.getProductId() + " của khách hàng " + request.getCustomerId());
        }
        newBeginningCylinderDebt.setUpdateAt(Instant.now());
        newBeginningCylinderDebt.setCreateAt(beginningCylinderDebt.getCreateAt());
        newBeginningCylinderDebt.setCreateBy(beginningCylinderDebt.getCreateBy());
        newBeginningCylinderDebt.setFactoryId(beginningCylinderDebt.getFactoryId());
        beginningCylinderDebtRepository.delete(beginningCylinderDebt);
        beginningCylinderDebtRepository.save(newBeginningCylinderDebt);
        return CommonMapper.map(newBeginningCylinderDebt, BeginningCylinderDebtDto.class);

    }

    @Override
    public void deleteBeginningCylinderDebt(String id) {
        if (!beginningCylinderDebtRepository.existsById(id)) {
            throw new NotFoundException("đầu kì công nợ vỏ khách hàng không tồn tại");
        }
        beginningCylinderDebtRepository.deleteById(id);
    }

//    @Override
//    public Integer forwardToNextYear(Integer yearFrom, Integer yearTo, String factoryId, String userFullName) {
//        return beginningCylinderDebtRepository.setForwardToNextYear(yearFrom,yearTo,factoryId,userFullName);
//    }
}
