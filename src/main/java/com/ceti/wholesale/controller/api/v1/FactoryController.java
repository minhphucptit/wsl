package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.controller.api.request.UpdateFactoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.FactoryFilterParam;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.FactoryDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.service.FactoryService;

@RestController
@RequestMapping("/v1")
@Validated
public class FactoryController {

    @Autowired
    FactoryService factoryService;

    @GetMapping(value = "/factories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<FactoryDto>> getFactoryById(
            @PathVariable(name = "id") String id) {
        FactoryDto factoryDto = factoryService.getFactoryById(id);
        ResponseBodyDto<FactoryDto> res = new ResponseBodyDto<>(factoryDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    
    // 22/07/2021 NamLH add
    @GetMapping(value = "/factories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<FactoryDto>> getList(
    		FactoryFilterParam param, Pageable pageable) {
    	
    	Factory requestObj = CommonMapper.map(param, Factory.class);
        Page<Factory> factories = factoryService.getList(requestObj, pageable);
        Page<FactoryDto> pageDto = CommonMapper.toPage(factories, FactoryDto.class, pageable);
		
        
        ResponseBodyDto<FactoryDto> res = new ResponseBodyDto<>(pageable, pageDto, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // Update Factory
    @PatchMapping (value = "/factories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<FactoryDto>> updateFactory(
            @PathVariable(name = "id") String id,
            @RequestBody UpdateFactoryRequest request) {
        FactoryDto factoryDto = factoryService.updateFactory(id, request);
        ResponseBodyDto<FactoryDto> res = new ResponseBodyDto<>(factoryDto,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
