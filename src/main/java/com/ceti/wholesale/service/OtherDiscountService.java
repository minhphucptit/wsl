package com.ceti.wholesale.service;

import com.ceti.wholesale.dto.OtherDiscountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OtherDiscountService {
    Page<OtherDiscountDto> getOtherDiscount(String customerCode, Integer month, Integer year, Pageable pageable);
}
