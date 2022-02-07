package com.ceti.wholesale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ceti.wholesale.dto.ProductStatusDto;

public interface ProductStatusService {
     Page<ProductStatusDto> getAll(Pageable pageable);
}
