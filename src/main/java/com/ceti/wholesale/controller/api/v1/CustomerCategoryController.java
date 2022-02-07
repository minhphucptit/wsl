package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.CustomerCategoryDto;
import com.ceti.wholesale.service.CustomerCategoryService;
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
public class CustomerCategoryController {
    @Autowired
    private CustomerCategoryService customerCategoryService;

    @GetMapping(value = "/customer-categories")
    public ResponseEntity<ResponseBodyDto<CustomerCategoryDto>> getAllCustomerCategory(Pageable pageable){
        Page<CustomerCategoryDto> page = customerCategoryService.getAll(pageable);
        ResponseBodyDto<CustomerCategoryDto> res = new ResponseBodyDto<>(pageable,page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
