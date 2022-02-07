package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateCustomerGroupRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerGroupRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.CustomerGroupDto;
import com.ceti.wholesale.service.CustomerGroupService;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Validated
public class CustomerGroupController {

    @Autowired
    CustomerGroupService customerGroupService;

    //Get list customer groups
    @GetMapping(value = "/customer-groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerGroupDto>> getAllCustomerGroups(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "is_active", required = false) Boolean isActive,
            @RequestParam(name = "factory_id",required = false) String factoryId,
            Pageable pageable) {

        Page<CustomerGroupDto> page = customerGroupService.getAllByCondition(search, id, name, factoryId, isActive, pageable);

        ResponseBodyDto<CustomerGroupDto> res = new ResponseBodyDto<>(pageable, page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //create new Customer group
    @PostMapping(value = "/customer-groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerGroupDto>> createCustomerGroup(
            @RequestBody CreateCustomerGroupRequest request,
            @RequestHeader(name = "department_id") String factoryId
    ) {
        CustomerGroupDto customerGroupDto = customerGroupService.createCustomerGroup(request, factoryId);
        ResponseBodyDto<CustomerGroupDto> res = new ResponseBodyDto<>(customerGroupDto, ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update Customer group
    @PatchMapping(value = "/customer-groups/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerGroupDto>> updateCustomerGroup(
            @PathVariable(name = "id") String id,
            @RequestBody UpdateCustomerGroupRequest request
    ) {
        CustomerGroupDto customerGroupDto = customerGroupService.updateCustomerGroup(id, request);
        ResponseBodyDto<CustomerGroupDto> res = new ResponseBodyDto<>(customerGroupDto, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
