package com.ceti.wholesale.service.impl;

import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ceti.wholesale.dto.ProductStatusDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.repository.ProductStatusRepository;
import com.ceti.wholesale.service.ProductStatusService;

@Service
public class ProductStatusServiceImpl implements ProductStatusService {
	
	@Autowired
	ProductStatusRepository productStatusRepository;

	@Override
	public Page<ProductStatusDto> getAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return CommonMapper.toPage(productStatusRepository.findAll(pageable), ProductStatusDto.class, pageable);
	}

}
