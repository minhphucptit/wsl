package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ProductDto;
import com.ceti.wholesale.dto.ProductPriceDto;
import com.ceti.wholesale.service.ProductPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ProductPriceController {

    @Autowired
    private ProductPriceService productPriceService;

    // Get list Product
    @GetMapping(value = "/product-prices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductPriceDto>> getProducts(
            @RequestParam MultiValueMap<String, String> where,
            @RequestHeader(name = "department_id") String factoryId
            , Pageable pageable) {
        where.add("factory_id", factoryId);
        Page<ProductPriceDto> page = productPriceService.getAllByCondition(where, pageable);

        ResponseBodyDto<ProductPriceDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
