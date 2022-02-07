package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateProductTypeRequest;
import com.ceti.wholesale.controller.api.request.UpdateProductTypeRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ProductTypeDto;
import com.ceti.wholesale.service.ProductTypeService;
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


@RestController
@RequestMapping("/v1")
@Validated
public class ProductTypeController {
    @Autowired
    ProductTypeService productTypeService;

    //get list product category
    @GetMapping(value = "/product-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductTypeDto>> getAllProductType(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "is_active", required = false) Boolean isActive,

            Pageable pageable) {

        Page<ProductTypeDto> page = productTypeService.getAllByCondition(search, name, id, isActive, pageable);

        ResponseBodyDto<ProductTypeDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create new product type
    @PostMapping(value = "/product-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductTypeDto>> createProductType(
            @RequestBody CreateProductTypeRequest request) {
        ProductTypeDto productTypeDto = productTypeService.createProductType(request);
        ResponseBodyDto<ProductTypeDto> res = new ResponseBodyDto<>(productTypeDto, ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //update product type
    @PatchMapping(value = "/product-types/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductTypeDto>> updateProductType(
            @PathVariable(name = "id") String id,
            @RequestBody UpdateProductTypeRequest request) {
        ProductTypeDto productTypeDto = productTypeService.updateProductType(id, request);
        ResponseBodyDto<ProductTypeDto> res = new ResponseBodyDto<>(productTypeDto, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}

