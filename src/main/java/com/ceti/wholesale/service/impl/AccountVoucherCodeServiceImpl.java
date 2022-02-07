package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.dto.AccountVoucherCodeDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.repository.AccountVoucherCodeDetailRepository;
import com.ceti.wholesale.service.AccountVoucherCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountVoucherCodeServiceImpl implements AccountVoucherCodeService {

    @Autowired
    private AccountVoucherCodeDetailRepository accountVoucherCodeDetailRepository;

    @Override
    public Page<AccountVoucherCodeDto> getAllWithFilter(MultiValueMap<String, String> where, Pageable pageable) {
        ResultPage<AccountVoucherCode> resultPage = accountVoucherCodeDetailRepository.findAll(pageable,where);
        List<AccountVoucherCodeDto> accountVoucherCodeDtos = new ArrayList<>();
        for (AccountVoucherCode accountVoucherCode : resultPage.getPageList()) {
            accountVoucherCodeDtos.add(CommonMapper.map(accountVoucherCode, AccountVoucherCodeDto.class));
        }
        return new PageImpl<>(accountVoucherCodeDtos, pageable, resultPage.getTotalItems());
    }
}
