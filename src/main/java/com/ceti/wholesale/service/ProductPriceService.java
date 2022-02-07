package com.ceti.wholesale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ceti.wholesale.dto.ProductPriceDto;
import org.springframework.util.MultiValueMap;

public interface ProductPriceService {
    Page<ProductPriceDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

}
