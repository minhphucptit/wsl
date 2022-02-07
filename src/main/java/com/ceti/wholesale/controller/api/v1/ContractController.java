package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateContractRequest;
import com.ceti.wholesale.controller.api.request.UpdateContractRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ContractDto;
import com.ceti.wholesale.mapper.ContractMapper;
import com.ceti.wholesale.service.ContractService;

import java.time.Instant;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Validated
public class ContractController {
      @Autowired
      ContractService contractService;
      
      @Autowired
      ContractMapper contractMapper;

      @GetMapping(value = "/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
      public ResponseEntity<ResponseBodyDto<ContractDto>> getAllContracts(
      @RequestParam(value = "contract_number",required = false)String contractNumber,
      @RequestParam(value = "sign_date_from",required = false)Long signFrom,
      @RequestParam(value = "sign_date_to",required = false)Long signTo,
      @RequestParam(value = "expire_date_from",required = false)Long expireFrom,
      @RequestParam(value = "expire_date_to",required = false)Long expireTo,
      @RequestParam(value = "status",required = false)String status,
      @RequestParam(value = "delivery_method",required = false)String deliveryMethod,
      @RequestParam(value = "contract_category_id",required = false)String contractCategoryId,
      @RequestParam(value = "customer_id",required = false)String customerId,
      @RequestParam(name = "factory_id", required = false) String factoryId,
      Pageable pageable){
    	  
			Instant signAtFrom = signFrom == null ? null : Instant.ofEpochSecond(signFrom);
			Instant signAtTo = signTo == null ? null : Instant.ofEpochSecond(signTo);
			Instant expireAtFrom = expireFrom == null ? null : Instant.ofEpochSecond(expireFrom);
			Instant expireAtTo = expireTo == null ? null : Instant.ofEpochSecond(expireTo);
			String pagingStr = PageableProcess.PageToSqlQuery(pageable, "c1");
			List<ContractDto> data = contractMapper.getList(contractNumber, signAtFrom, signAtTo, expireAtFrom,
					expireAtTo, status, deliveryMethod, contractCategoryId, factoryId, customerId, pagingStr);
			long totalItem = contractMapper.countList(contractNumber, signAtFrom, signAtTo, expireAtFrom, expireAtTo,
					status, deliveryMethod, contractCategoryId, factoryId, customerId);
            	
            ResponseBodyDto<ContractDto> response = new ResponseBodyDto<>(data,
            ResponseCodeEnum.R_200,"OK", totalItem);

            return ResponseEntity.status(HttpStatus.OK).body(response);
      }

      @PostMapping(value = "/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
      public ResponseEntity<ResponseBodyDto<ContractDto>> createContract(
      @RequestBody CreateContractRequest createContractRequest){
            ContractDto contractDto =
            contractService.createContract(createContractRequest);

            ResponseBodyDto<ContractDto> response = new ResponseBodyDto<>(contractDto, ResponseCodeEnum.R_201,"CREATED");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
      }

      @PatchMapping(value = "/contracts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
      public ResponseEntity<ResponseBodyDto<ContractDto>> updateContractCategory(
      @PathVariable(name = "id") String id,
      @RequestBody UpdateContractRequest request) {
            ContractDto contractDto = contractService.updateContract(id, request);
            ResponseBodyDto<ContractDto> response = new ResponseBodyDto<>(contractDto,
            ResponseCodeEnum.R_200, "OK");
            return ResponseEntity.status(HttpStatus.OK).body(response);
      }
}
