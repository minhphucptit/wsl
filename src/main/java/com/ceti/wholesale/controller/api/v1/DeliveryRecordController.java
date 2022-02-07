package com.ceti.wholesale.controller.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.DeliveryRecordDto;
import com.ceti.wholesale.mapper.DeliveryRecordMapper;

@RestController
@RequestMapping("/v1")
@Validated
public class DeliveryRecordController {
	

    @Autowired
    private DeliveryRecordMapper deliveryRecordMapper;
	
    // get list delivery record
    @GetMapping(value = "/delivery-records/{voucher_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<DeliveryRecordDto>> getAll(
            @PathVariable(name = "voucher_id") String voucherId,
            @RequestHeader(name = "department_id") String factoryId) {
    	
        List<DeliveryRecordDto> deliveryRecordDtos = deliveryRecordMapper.getList(factoryId, voucherId);
        
        ResponseBodyDto<DeliveryRecordDto> res = new ResponseBodyDto<>(deliveryRecordDtos,
                ResponseCodeEnum.R_200, "OK",deliveryRecordDtos.size());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
