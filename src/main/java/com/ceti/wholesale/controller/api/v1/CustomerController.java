package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateCustomerRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.CustomerDto;
import com.ceti.wholesale.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // get list customer
    @GetMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerDto>> getAllCustomer(
            @RequestParam MultiValueMap<String, String> where,
            @RequestHeader(name = "department_id") String factory_id,
            Pageable pageable) {
        where.add("factory_id", factory_id);
        Page<CustomerDto> page = customerService.getAllByConditionWithEmbed(where, pageable);
        ResponseBodyDto<CustomerDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create new Customer
    @PostMapping(value = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerDto>> createCustomer(
            @RequestBody CreateCustomerRequest request
//            @RequestHeader(name = "department_id") String factory_id
    ) {
        CustomerDto customerDto = customerService.createCustomer(request);
        ResponseBodyDto<CustomerDto> res = new ResponseBodyDto<>(customerDto, ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //update cusstomer
    @PatchMapping(value = "/customers/{customer-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerDto>> updateCustomer(
            @PathVariable(name = "customer-id") String customerId,
            @RequestBody UpdateCustomerRequest request
    ) {
        CustomerDto customerDto = customerService.updateCustomer(customerId, request);
        ResponseBodyDto<CustomerDto> res = new ResponseBodyDto<>(customerDto, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // 09/09/2021 NamLH: thêm api lấy danh sách tất cả KH ở tất cả các kho
    @GetMapping(value = "/customers/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerDto>> getListCustomer(
            @RequestParam MultiValueMap<String, String> where,
            Pageable pageable) {
        Page<CustomerDto> page = customerService.getAllByConditionWithEmbed(where, pageable);
        ResponseBodyDto<CustomerDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
