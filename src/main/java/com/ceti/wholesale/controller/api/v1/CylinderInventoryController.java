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
import com.ceti.wholesale.dto.CylinderInventoryDto;
import com.ceti.wholesale.mapper.CylinderInventoryMapper;

@RestController
@RequestMapping("/v1")
public class CylinderInventoryController {

	@Autowired
	CylinderInventoryMapper cylinderInventoryMapper;
	
	@GetMapping(value = "/cylinder-inventories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CylinderInventoryDto>> getAllCylinderInventories(
    		@RequestParam("voucher_at_to")Long voucherAtTo) {

        List<CylinderInventoryDto> list = cylinderInventoryMapper.getList(Instant.ofEpochSecond(voucherAtTo));
        long size = Integer.toUnsignedLong(list.size());
        
        ResponseBodyDto<CylinderInventoryDto> res = new ResponseBodyDto<CylinderInventoryDto>(list, ResponseCodeEnum.R_200, "OK", size);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
