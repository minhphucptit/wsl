package com.ceti.wholesale.service;

import com.ceti.wholesale.dto.BrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandService {

    Page<BrandDto> getAll(Pageable  pageable);

}
