package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.dto.BrandDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.repository.BrandRepository;
import com.ceti.wholesale.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Page<BrandDto> getAll(Pageable pageable) {
        return CommonMapper.toPage(brandRepository.findAll(pageable), BrandDto.class,pageable);
    }
}
