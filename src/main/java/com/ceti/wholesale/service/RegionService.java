package com.ceti.wholesale.service;

import com.ceti.wholesale.dto.RegionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RegionService {

    Page<RegionDto> getAll(Pageable  pageable);

}
