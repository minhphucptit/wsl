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
import com.ceti.wholesale.controller.api.request.CreateMoneyDebtRequest;
import com.ceti.wholesale.dto.BeginningMoneyDebtDto;
import com.ceti.wholesale.dto.CustomerDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.BeginningMoneyDebt;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.repository.BeginningMoneyDebtRepository;
import com.ceti.wholesale.repository.MoneyDebtDetailRepository;
import com.ceti.wholesale.service.BeginningMoneyDebtService;

@Service
@Transactional
public class BeginningMoneyDebtServiceImpl implements BeginningMoneyDebtService {

    @Autowired
    BeginningMoneyDebtRepository moneyDebtRepository;

    @Autowired
    MoneyDebtDetailRepository moneyDebtDetailRepository;

    @Override
    public Page<BeginningMoneyDebtDto> getAllMoneyDebt(MultiValueMap<String, String> where, Pageable pageable) {
        ResultPage<Object[]> page = moneyDebtDetailRepository.findAllWithFilter(pageable, where);
        List<BeginningMoneyDebtDto> beginningMoneyDebtDtos = new ArrayList<>();
        for (Object[] object : page.getPageList()) {
            BeginningMoneyDebt md = (BeginningMoneyDebt) object[0];
            BeginningMoneyDebtDto beginningMoneyDebtDto = CommonMapper.map(md, BeginningMoneyDebtDto.class);

            if (object[1] != null) {
                Customer customer = (Customer) object[1];
                CustomerDto customerDto = CommonMapper.map(customer, CustomerDto.class);
                beginningMoneyDebtDto.setCustomer(customerDto);
            }

            beginningMoneyDebtDtos.add(beginningMoneyDebtDto);
        }
        return new PageImpl<>(beginningMoneyDebtDtos, pageable, page.getTotalItems());
    }

    @Override
    public BeginningMoneyDebtDto createMoneyDebt(CreateMoneyDebtRequest request, String factoryId) {
        Instant now = Instant.now();
        BeginningMoneyDebt beginningMoneyDebt;
        beginningMoneyDebt = CommonMapper.map(request, BeginningMoneyDebt.class);
        beginningMoneyDebt
                .setId("" + beginningMoneyDebt.getYear() + beginningMoneyDebt.getCustomerId() + factoryId);

        if (moneyDebtRepository.existsById(beginningMoneyDebt.getId())) {
            throw new BadRequestException("đã tồn tại bản ghi công nợ tiền của khách hàng " + request.getCustomerId() + " năm " + request.getYear());
        }
        beginningMoneyDebt.setCreateAt(now);
        beginningMoneyDebt.setUpdateAt(now);
        beginningMoneyDebt.setFactoryId(factoryId);

        moneyDebtRepository.save(beginningMoneyDebt);
        return CommonMapper.map(beginningMoneyDebt, BeginningMoneyDebtDto.class);
    }

    @Override
    public BeginningMoneyDebtDto updateMoneyDebt(String id, CreateMoneyDebtRequest request) {
        Optional<BeginningMoneyDebt> optional = moneyDebtRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Mã công nợ không tồn tại");
        }
        BeginningMoneyDebt beginningMoneyDebt = optional.get();
        BeginningMoneyDebt newBeginningMoneyDebt = CommonMapper.map(request, BeginningMoneyDebt.class);
        newBeginningMoneyDebt.setUpdateAt(Instant.now());
        newBeginningMoneyDebt.setCreateAt(beginningMoneyDebt.getCreateAt());
        newBeginningMoneyDebt.setCreateBy(beginningMoneyDebt.getCreateBy());
        newBeginningMoneyDebt.setId(request.getYear() + request.getCustomerId() + beginningMoneyDebt.getFactoryId());
        if (!newBeginningMoneyDebt.getId().equals(beginningMoneyDebt.getId()) && moneyDebtRepository.existsById(newBeginningMoneyDebt.getId())) {
            throw new BadRequestException("đã tồn tại bản ghi công nợ tiền của khách hàng " + request.getCustomerId() + " năm " + request.getYear());
        }
        moneyDebtRepository.delete(beginningMoneyDebt);
        newBeginningMoneyDebt.setFactoryId(beginningMoneyDebt.getFactoryId());
        moneyDebtRepository.save(newBeginningMoneyDebt);
        return CommonMapper.map(newBeginningMoneyDebt, BeginningMoneyDebtDto.class);
    }

    @Override
    public void deleteBeginningMoneyDebt(String id) {
        if (!moneyDebtRepository.existsById(id)) {
            throw new NotFoundException("đầu kì công nợ tiền khách hàng không tồn tại");
        }
        moneyDebtRepository.deleteById(id);
    }

//    @Override
//    public Integer forwardToNextYear(Integer yearFrom, Integer yearTo, String factoryId, String userFullName) {
//        return moneyDebtRepository.setForwardToNextYear(yearFrom,yearTo,factoryId,userFullName);
//    }
}
