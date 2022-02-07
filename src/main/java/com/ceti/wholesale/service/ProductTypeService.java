
package com.ceti.wholesale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ceti.wholesale.controller.api.request.CreateProductTypeRequest;
import com.ceti.wholesale.controller.api.request.UpdateProductTypeRequest;
import com.ceti.wholesale.dto.ProductTypeDto;

public interface ProductTypeService {
    ProductTypeDto createProductType(CreateProductTypeRequest request);

    ProductTypeDto updateProductType(String id, UpdateProductTypeRequest request);

    Page<ProductTypeDto> getAllByCondition(String search, String name, String id, Boolean isActive, Pageable pageable);
}

