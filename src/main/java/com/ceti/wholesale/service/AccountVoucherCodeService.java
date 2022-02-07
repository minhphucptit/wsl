package com.ceti.wholesale.service;

import com.ceti.wholesale.dto.AccountVoucherCodeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface AccountVoucherCodeService {

    Page<AccountVoucherCodeDto> getAllWithFilter(MultiValueMap<String,String> where, Pageable  pageable);

}
