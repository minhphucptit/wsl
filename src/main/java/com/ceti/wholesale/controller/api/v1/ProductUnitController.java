package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ProductUnitDto;
import com.ceti.wholesale.service.ProductUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class ProductUnitController {

    @Autowired
    private ProductUnitService productUnitService;

    //Get list product_unit
    @GetMapping(value = "/product_unit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductUnitDto>> getFindAllOrderByAsc() {

        List<ProductUnitDto> list = productUnitService.getFindAllOrderByAsc();
        ResponseBodyDto<ProductUnitDto> res = new ResponseBodyDto<>(list, ResponseCodeEnum.R_200, "OK", list.size());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
