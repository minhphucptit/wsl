package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.UpdateFactoryRequest;
import com.ceti.wholesale.dto.FactoryDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.service.FactoryService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FactoryServiceImpl implements FactoryService {

    @Autowired
    private FactoryRepository factoryRepository;

    @Override
    public FactoryDto getFactoryById(String id) {
        return CommonMapper.map(factoryRepository.getById(id), FactoryDto.class);
    }

	@Override
	public Page<Factory> getList(Factory requestObject, Pageable pageable) {
		ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.CONTAINING).withIgnoreNullValues();
		Example<Factory> filter = Example.of(requestObject, matcher);
		
		return factoryRepository.findAll(filter, pageable);
	}

    @Override
    public FactoryDto updateFactory(String id, UpdateFactoryRequest request) {
        Optional<Factory> optional = factoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Kho không tồn tại");
        }
        Factory factory = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(request, factory);
        factory = factoryRepository.save(factory);
        return CommonMapper.map(factory, FactoryDto.class);
    }

}
