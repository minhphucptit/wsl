package com.ceti.wholesale.controller.api.v1;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.TotalCylinderDto;
import com.ceti.wholesale.mapper.TotalCylinderMapper;

@RestController
@RequestMapping("/v1")
public class TotalCylinderController {
	@Autowired
	TotalCylinderMapper totalCylinderMapper;
	
	@GetMapping(value = "/total-cylinders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TotalCylinderDto>> getAllTotalCylinders(
    		@RequestParam("voucher_at_to")Long voucherAtTo) {

        List<TotalCylinderDto> list = totalCylinderMapper.getList(Instant.ofEpochSecond(voucherAtTo));
        long size = Integer.toUnsignedLong(list.size());
        
        ResponseBodyDto<TotalCylinderDto> res = new ResponseBodyDto<TotalCylinderDto>(list, ResponseCodeEnum.R_200, "OK", size);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
