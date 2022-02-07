package com.ceti.wholesale.service;

import com.ceti.wholesale.dto.CustomerCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerCategoryService {
    Page<CustomerCategoryDto> getAll(Pageable pageable);
}
