package com.ceti.wholesale.controller.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ProductStatusDto;
import com.ceti.wholesale.service.ProductStatusService;

@RestController
@RequestMapping("/v1")
public class ProductStatusController {
	
	 @Autowired
	 ProductStatusService productStatusService;
	
	 @GetMapping(value = "/product-status", produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<ResponseBodyDto<ProductStatusDto>> getAllCompanies(
	          
	            Pageable pageable) {

	        Page<ProductStatusDto> page = productStatusService.getAll(pageable);

	        ResponseBodyDto<ProductStatusDto> res = new ResponseBodyDto<>(pageable, page, ResponseCodeEnum.R_200, "OK");
	        return ResponseEntity.status(HttpStatus.OK).body(res);
	    }
}
