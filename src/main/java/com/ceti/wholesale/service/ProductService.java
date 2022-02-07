package com.ceti.wholesale.service;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.controller.api.request.CreateProductRequest;
import com.ceti.wholesale.controller.api.request.UpdateProductRequest;
import com.ceti.wholesale.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(CreateProductRequest request, String factoryId) throws BadRequestException;

    Page<ProductDto> getListProduct(MultiValueMap<String, String> where, Pageable pageable);

    Page<ProductDto> findAllWithEmbed(MultiValueMap<String, String> where, Pageable pageable,String[] embed);

    List<String> getAllProductId(String id,String factoryId);

    ProductDto updateProduct(String productId, String factoryId, UpdateProductRequest request);

    ProductDto findProductById(String productId);

    void updateProductPriceByWeight(String factoryId, UpdateProductRequest request);
    public List<String> getAllListWeights(String factoryId);
}
