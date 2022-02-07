package com.ceti.wholesale.controller.api.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateAllTargetProductionRequest;
import com.ceti.wholesale.controller.api.request.CreateTargetProductionRequest;
import com.ceti.wholesale.controller.api.request.DeleteTargetProductionRequest;
import com.ceti.wholesale.controller.api.request.UpdateTargetProductionRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.TargetProductionDto;
import com.ceti.wholesale.mapper.TargetProductionMapper;
import com.ceti.wholesale.service.TargetProductionService;


@RestController
@RequestMapping("/v1")
@Validated
public class TargetProductionController {
	
	@Autowired
	TargetProductionService targetProductionService;
	
	@Autowired
	TargetProductionMapper targetProductionMapper;
	
	@PostMapping(value = "/target-productions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TargetProductionDto>> createTargetProduction(
    @RequestBody @Valid CreateTargetProductionRequest createRequest,
    @RequestHeader(name = "user_id") String userId){
		TargetProductionDto targetProductionDto =
		targetProductionService.createTargetProduction(createRequest, userId);

          ResponseBodyDto<TargetProductionDto> response = new ResponseBodyDto<>(targetProductionDto, ResponseCodeEnum.R_201,"CREATED");

          return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(value = "/target-productions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TargetProductionDto>> updateTargetProduction(
    @RequestBody @Valid UpdateTargetProductionRequest updateRequest,
    @RequestHeader(name = "user_id") String userId) {
    	TargetProductionDto targetProductionDto =
    			targetProductionService.updateTargetProduction(updateRequest,userId);
          ResponseBodyDto<TargetProductionDto> response = new ResponseBodyDto<>(targetProductionDto,
          ResponseCodeEnum.R_200, "OK");
          return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @GetMapping(value = "/target-productions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TargetProductionDto>> getAllContracts(
    @RequestParam(value = "customer_code",required = false)String customerCode,
    @RequestParam(value = "customer_full_name",required = false)String customerFullName,
    @RequestParam(value = "month_from",required = false)Integer monthFrom,
    @RequestParam(value = "month_to",required = false)Integer monthTo,
    @RequestParam(value = "year_from",required = false)Integer yearFrom,
    @RequestParam(value = "year_to",required = false)Integer yearTo,
    Pageable pageable){
    	List<TargetProductionDto> data = targetProductionMapper.getList(customerCode, customerFullName, monthFrom, monthTo, yearFrom, yearTo,
    			pageable.getOffset(), pageable.getPageSize());
		long totalItem = targetProductionMapper.countList(customerCode, customerFullName, monthFrom, monthTo, yearFrom, yearTo);

		ResponseBodyDto<TargetProductionDto> res = new ResponseBodyDto<>(data, ResponseCodeEnum.R_200,
				"OK", totalItem);
		return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    
    @DeleteMapping(value = "/target-productions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TargetProductionDto>> deleteTargetProduction(
    @RequestBody @Valid DeleteTargetProductionRequest deleteRequest) {
    	
    	  targetProductionService.deleteTargetProduction(deleteRequest);
    	  
          ResponseBodyDto<TargetProductionDto> response = new ResponseBodyDto<>(null,
          ResponseCodeEnum.R_200, "OK");
          return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
	@PostMapping(value = "/target-productions/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TargetProductionDto>> createAll(
    @RequestBody CreateAllTargetProductionRequest request,
    @RequestHeader(name = "user_id") String userId){
		List<TargetProductionDto> targetProductionDtos =
		targetProductionService.createAll(request, userId);

          ResponseBodyDto<TargetProductionDto> response = new ResponseBodyDto<>(targetProductionDtos, ResponseCodeEnum.R_201,"CREATED", targetProductionDtos.size());

          return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
