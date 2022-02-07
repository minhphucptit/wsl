package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateContractCategoryRequest;
import com.ceti.wholesale.controller.api.request.UpdateContractCategoryRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ContractCategoryDto;
import com.ceti.wholesale.service.ContractCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
public class ContractCategoryController {
      @Autowired
      ContractCategoryService contractCategoryService;

      @GetMapping(value = "/contract-categories", produces = MediaType.APPLICATION_JSON_VALUE)
      public ResponseEntity<ResponseBodyDto<ContractCategoryDto>> getAllContractCategories(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "is_active", required = false)Boolean isActive,
      Pageable pageable){
            Page<ContractCategoryDto> page = contractCategoryService.getAllByConditions(name,isActive,pageable);

            ResponseBodyDto<ContractCategoryDto> response = new ResponseBodyDto<>(pageable,page,
            ResponseCodeEnum.R_200,"OK");

            return ResponseEntity.status(HttpStatus.OK).body(response);
      }

      @PostMapping(value = "/contract-categories", produces = MediaType.APPLICATION_JSON_VALUE)
      public ResponseEntity<ResponseBodyDto<ContractCategoryDto>> createContractCategory(
      @RequestBody CreateContractCategoryRequest createContractCategoryRequest){
            ContractCategoryDto contractCategoryDto =
            contractCategoryService.createContractCategory(createContractCategoryRequest);

            ResponseBodyDto<ContractCategoryDto> response = new ResponseBodyDto<>(contractCategoryDto, ResponseCodeEnum.R_201,"CREATED");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
      }

      @PatchMapping(value = "/contract-categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
      public ResponseEntity<ResponseBodyDto<ContractCategoryDto>> updateContractCategory(
      @PathVariable(name = "id") String id,
      @RequestBody UpdateContractCategoryRequest request) {
            ContractCategoryDto contractCategoryDto = contractCategoryService.updateContractCategory(id, request);

            ResponseBodyDto<ContractCategoryDto> response = new ResponseBodyDto<>(contractCategoryDto,
            ResponseCodeEnum.R_200, "OK");

            return ResponseEntity.status(HttpStatus.OK).body(response);
      }
}
