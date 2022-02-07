package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.dto.RegionDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.repository.RegionRepository;
import com.ceti.wholesale.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionRepository regionRepository;

    @Override
    public Page<RegionDto> getAll(Pageable pageable) {
        return CommonMapper.toPage(regionRepository.findAll(pageable), RegionDto.class,pageable);
    }
}
