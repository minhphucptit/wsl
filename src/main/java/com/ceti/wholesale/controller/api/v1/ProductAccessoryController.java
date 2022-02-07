package com.ceti.wholesale.controller.api.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateListProductAccessoryRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ProductAccessoryDto;
import com.ceti.wholesale.service.ProductAccessoryService;

@RestController
@RequestMapping("/v1")
public class ProductAccessoryController {

    @Autowired
    private ProductAccessoryService productAccessoryService;

    // Get list Product accessory
    @GetMapping(value = "/factory-products/{product_id}/product-accessories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductAccessoryDto>> getProductAccessories(
            @PathVariable("product_id") String productId,
            @RequestHeader(name = "department_id") String factoryId) {
        List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(productId, factoryId);

        ResponseBodyDto<ProductAccessoryDto> res = new ResponseBodyDto<>(list, ResponseCodeEnum.R_200, "OK", list.size());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //update Product accessory
    @PatchMapping(value = "/factory-products/{product_id}/product-accessories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductAccessoryDto>> updateProductAccessories(
            @Valid @RequestBody CreateListProductAccessoryRequest request,
            @PathVariable("product_id") String productId,
            @RequestHeader(name = "department_id") String factoryId) {
        List<ProductAccessoryDto> list = productAccessoryService.updateProductAccessories(request, productId, factoryId);
        ResponseBodyDto<ProductAccessoryDto> res = new ResponseBodyDto<>(list, ResponseCodeEnum.R_200, "OK", list.size());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
