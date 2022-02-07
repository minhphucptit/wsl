package com.ceti.wholesale.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.controller.api.request.CreateListProductAccessoryRequest;
import com.ceti.wholesale.controller.api.request.CreateProductAccessoryRequest;
import com.ceti.wholesale.dto.ProductAccessoryDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.ProductAccessory;
import com.ceti.wholesale.repository.ProductAccessoryRepository;
import com.ceti.wholesale.service.ProductAccessoryService;

@Service
public class ProductAccessoryServiceImpl implements ProductAccessoryService {

    @Autowired
    private ProductAccessoryRepository productAccessoryRepository;

    @Override
    public List<ProductAccessoryDto> getByMainProductIdAndFactoryId(String mainProductId, String factoryId) {
        List<ProductAccessory> productAccessoryList = productAccessoryRepository.findByMainProductIdAndFactoryId(mainProductId, factoryId);
        return CommonMapper.toList(productAccessoryList, ProductAccessoryDto.class);
    }

    @Override
    @Transactional
    public List<ProductAccessoryDto> updateProductAccessories(CreateListProductAccessoryRequest request, String mainProductId, String factoryId) {
        productAccessoryRepository.deleteByMainProductIdAndFactoryId(mainProductId, factoryId);
        List<ProductAccessoryDto> productAccessoryDtoList = new ArrayList<>();
        for (CreateProductAccessoryRequest createProductAccessoryRequest : request.getProductAccessory()) {
            ProductAccessory productAccessory = CommonMapper.map(createProductAccessoryRequest, ProductAccessory.class);
            productAccessory.setMainProductId(mainProductId);
            productAccessory.setFactoryId(factoryId);
            productAccessoryRepository.save(productAccessory);
            ProductAccessoryDto productAccessoryDto = CommonMapper.map(productAccessory, ProductAccessoryDto.class);
            productAccessoryDtoList.add(productAccessoryDto);
        }
        return productAccessoryDtoList;
    }
}
