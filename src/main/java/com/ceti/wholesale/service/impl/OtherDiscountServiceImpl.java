package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.dto.OtherDiscountDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.OtherDiscount;
import com.ceti.wholesale.repository.OtherDiscountRepository;
import com.ceti.wholesale.service.OtherDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OtherDiscountServiceImpl implements OtherDiscountService {

    @Autowired
    private OtherDiscountRepository otherDiscountRepository;

    @Override
    public Page<OtherDiscountDto> getOtherDiscount(String customerCode, Integer month, Integer year, Pageable pageable) {
        Page<OtherDiscount> otherDiscounts = otherDiscountRepository.getAllByCustomerCodeAndMonthAndYear(customerCode,month,year,pageable);
        return CommonMapper.toPage(otherDiscounts,OtherDiscountDto.class,pageable);
    }
}
