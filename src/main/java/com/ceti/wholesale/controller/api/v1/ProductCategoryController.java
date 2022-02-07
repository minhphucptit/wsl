package com.ceti.wholesale.controller.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateProductCategoryRequest;
import com.ceti.wholesale.controller.api.request.UpdateProductCategoryRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ProductCategoryDto;
import com.ceti.wholesale.service.ProductCategoryService;

@RestController
@RequestMapping("/v1")
@Validated
public class ProductCategoryController {
    @Autowired
    ProductCategoryService productCategoryService;

    //get list product category
    @GetMapping(value = "/product-categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductCategoryDto>> getAllProductCategory(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "is_active", required = false) Boolean isActive,
            @RequestParam(name = "product_type_id", required = false) String productTypeId,
            Pageable pageable) {

        Page<ProductCategoryDto> page = productCategoryService.getAllByCondition(search, name, productTypeId, isActive, pageable);

        ResponseBodyDto<ProductCategoryDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //create new Product category
    @PostMapping(value = "/product-categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductCategoryDto>> createProductCategory(
            @RequestBody CreateProductCategoryRequest request
    ) {
        ProductCategoryDto productCategoryDto = productCategoryService.createProductCategory(request);
        ResponseBodyDto<ProductCategoryDto> res = new ResponseBodyDto<>(productCategoryDto, ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //update Product category
    @PatchMapping(value = "/product-categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductCategoryDto>> updateProductCategory(
            @PathVariable(name = "id") String id,
            @RequestBody UpdateProductCategoryRequest request
    ) {
        ProductCategoryDto productCategoryDto = productCategoryService.updateProductCategory(id, request);
        ResponseBodyDto<ProductCategoryDto> res = new ResponseBodyDto<>(productCategoryDto, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
