
package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateProductCategoryRequest;
import com.ceti.wholesale.controller.api.request.UpdateProductCategoryRequest;
import com.ceti.wholesale.dto.ProductCategoryDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.ProductCategory;
import com.ceti.wholesale.repository.ProductCategoryRepository;
import com.ceti.wholesale.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Override
    public Page<ProductCategoryDto> getAllByCondition(String search, String name, String productTypeId, Boolean isActive, Pageable pageable) {
        Page<ProductCategory> page = productCategoryRepository.getAllByCondition(search, name, productTypeId, isActive, pageable);
        return CommonMapper.toPage(page, ProductCategoryDto.class, pageable);
    }

    //create product category
    @Override
    public ProductCategoryDto createProductCategory(CreateProductCategoryRequest request) {
        if (productCategoryRepository.existsById(request.getId())) {
            throw new BadRequestException("Mã nhóm hàng đã tồn tại");
        }
        ProductCategory productCategory = new ProductCategory();
        CommonMapper.copyPropertiesIgnoreNull(request, productCategory);
        productCategory = productCategoryRepository.save(productCategory);
        return CommonMapper.map(productCategory, ProductCategoryDto.class);

    }

    //update product category
    @Override
    public ProductCategoryDto updateProductCategory(String id, UpdateProductCategoryRequest request) {
        Optional<ProductCategory> optional = productCategoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Mã nhóm hàng không tồn tại");
        }
        ProductCategory productCategory = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(request, productCategory);
        productCategory = productCategoryRepository.save(productCategory);
        return CommonMapper.map(productCategory, ProductCategoryDto.class);
    }

}
