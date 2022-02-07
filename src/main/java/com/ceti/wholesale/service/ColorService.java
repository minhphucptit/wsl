package com.ceti.wholesale.service;

import com.ceti.wholesale.dto.ColorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ColorService {

    Page<ColorDto> getAll(Pageable  pageable);

}
