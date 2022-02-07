package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateProductCategoryRequest;
import com.ceti.wholesale.controller.api.request.UpdateProductCategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.ceti.wholesale.dto.ProductCategoryDto;


public interface ProductCategoryService {
    ProductCategoryDto createProductCategory(CreateProductCategoryRequest request);

    ProductCategoryDto updateProductCategory(String id, UpdateProductCategoryRequest request);

    Page<ProductCategoryDto> getAllByCondition(String search, String name, String productTypeId, Boolean isActive,
                                               Pageable pageable);
}
