package com.ceti.wholesale.controller.api.v1;

import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateEquipmentRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.EquipmentDto;
import com.ceti.wholesale.mapper.EquipmentMapper;
import com.ceti.wholesale.service.EquipmentService;

@RestController
@RequestMapping("/v1")
@Validated
public class EquipmentController {
	
	@Autowired
	EquipmentService equipmentService;
	
	@Autowired
	EquipmentMapper equipmentMapper;
	
	@GetMapping(value = "/equipments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<EquipmentDto>> getAllEquipment(
            @RequestParam HashMap<String, String> where,
//            @RequestHeader("department_id") String factoryId,
            Pageable pageable) {
        String tableName = "equipment";
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, tableName);
        List<EquipmentDto> list = equipmentService.getAll(DatetimeUtil.convertLongStringToInstant(where.get("inspection_date_from")),
        		DatetimeUtil.convertLongStringToInstant(where.get("inspection_date_to")),where.get("name"),where.get("origin"),where.get("brand")
        		,where.get("symbol"),where.get("model"),where.get("manufacture_year"),where.get("factory_id"),where.get("is_active"),pagingStr);
        Long totalItems = equipmentMapper.countList(DatetimeUtil.convertLongStringToInstant(where.get("inspection_date_from")),
                DatetimeUtil.convertLongStringToInstant(where.get("inspection_date_to")),where.get("name"),where.get("origin"),where.get("brand")
                ,where.get("symbol"),where.get("model"),where.get("manufacture_year"),where.get("factory_id"),where.get("is_active"));
        ResponseBodyDto<EquipmentDto> res = new ResponseBodyDto<>(list,
                ResponseCodeEnum.R_200, "OK",totalItems);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create 
    @PostMapping(value = "/equipments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<EquipmentDto>> createEquipment(
            @Valid @RequestBody CreateEquipmentRequest request,
            @RequestHeader("department_id") String factoryId) {

    	EquipmentDto equipmentDto = equipmentService.createEquipment(request,factoryId);
        ResponseBodyDto<EquipmentDto> res = new ResponseBodyDto<>(equipmentDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update 
    @PatchMapping(value = "/equipments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<EquipmentDto>> updateEquipment(
            @Valid @RequestBody CreateEquipmentRequest request,
            @PathVariable(name = "id") String id) {

    	EquipmentDto equipmentDto = equipmentService.updateEquipment(id,request);
        ResponseBodyDto<EquipmentDto> res = new ResponseBodyDto<>(equipmentDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
