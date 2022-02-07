package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.BrandDto;
import com.ceti.wholesale.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping(value = "/brands")
    public ResponseEntity<ResponseBodyDto<BrandDto>> getAllBrand(Pageable pageable){
        Page<BrandDto> page = brandService.getAll(pageable);
        ResponseBodyDto<BrandDto> res = new ResponseBodyDto<>(pageable,page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
