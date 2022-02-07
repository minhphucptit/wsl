package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.dto.ColorDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.repository.ColorRepository;
import com.ceti.wholesale.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ColorServiceImpl implements ColorService {

    @Autowired
    private ColorRepository colorRepository;

    @Override
    public Page<ColorDto> getAll(Pageable pageable) {
        return CommonMapper.toPage(colorRepository.findAll(pageable), ColorDto.class,pageable);
    }
}
