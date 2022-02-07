

package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateProductTypeRequest;
import com.ceti.wholesale.controller.api.request.UpdateProductTypeRequest;
import com.ceti.wholesale.dto.ProductTypeDto;
import com.ceti.wholesale.model.ProductType;
import com.ceti.wholesale.repository.ProductTypeRepository;
import com.ceti.wholesale.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ceti.wholesale.dto.mapper.CommonMapper;

import java.util.Optional;


@Service
public class ProductTypeServiceImpl implements ProductTypeService {
    @Autowired
    ProductTypeRepository productTypeRepository;

    @Override
    public Page<ProductTypeDto> getAllByCondition(String search, String name, String id, Boolean isActive, Pageable pageable) {
        Page<ProductType> page = productTypeRepository.getAllByCondition(search, name, id, isActive, pageable);
        return CommonMapper.toPage(page, ProductTypeDto.class, pageable);
    }

    //create new Product type
    @Override
    public ProductTypeDto createProductType(CreateProductTypeRequest request) {
        if (productTypeRepository.existsById(request.getId())) {
            throw new BadRequestException("Mã loại hàng đã tồn tại");
        }
        ProductType productType = new ProductType();
        CommonMapper.copyPropertiesIgnoreNull(request, productType);
        productType = productTypeRepository.save(productType);
        return CommonMapper.map(productType, ProductTypeDto.class);

    }

    //  update Product type
    @Override
    public ProductTypeDto updateProductType(String id, UpdateProductTypeRequest request) {
        Optional<ProductType> optional = productTypeRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Mã loại hàng không tồn tại");
        }
        ProductType productType = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(request, productType);
        productType = productTypeRepository.save(productType);
        return CommonMapper.map(productType, ProductTypeDto.class);
    }
}
