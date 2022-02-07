package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateListProductAccessoryRequest;
import com.ceti.wholesale.dto.ProductAccessoryDto;

import java.util.List;


public interface ProductAccessoryService {

    List<ProductAccessoryDto> getByMainProductIdAndFactoryId(String mainProductId, String factoryId);

    List<ProductAccessoryDto> updateProductAccessories(CreateListProductAccessoryRequest request, String mainProductId, String factoryId);
}
