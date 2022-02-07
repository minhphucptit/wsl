package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateCustomerProductDiscountRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerProductDiscountRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.CustomerProductDiscountDto;
import com.ceti.wholesale.service.CustomerProductDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;

@RestController
@RequestMapping("/v1")
public class CustomerProductDiscountController {

    @Autowired
    private CustomerProductDiscountService customerProductDiscountService;

    @PostMapping(value = "/customer-product-discounts",produces = "application/json")
    public ResponseEntity<ResponseBodyDto<CustomerProductDiscountDto>> create(
            @RequestHeader("user_id") String userId,
            @Valid @RequestBody CreateCustomerProductDiscountRequest request
            ){
        CustomerProductDiscountDto productDiscountDto = customerProductDiscountService.createCustomerProductDiscount(request,userId);
        ResponseBodyDto<CustomerProductDiscountDto> responseBodyDto = new ResponseBodyDto<>();
        responseBodyDto.setItem(productDiscountDto).setCode(ResponseCodeEnum.R_201)
                .setMessage("CREATED");
        return new ResponseEntity<ResponseBodyDto<CustomerProductDiscountDto>>(responseBodyDto, HttpStatus.CREATED);
    }
    @GetMapping(value = "/customer-product-discounts", produces = "application/json")
    public ResponseEntity<ResponseBodyDto<CustomerProductDiscountDto>> getAllCustomerProductDiscount(
            Pageable pageable,
            @QueryParam("embed") String[] embed, @RequestParam MultiValueMap<String, String> where) {

//        where.add("factory_id",departmentId);
        Page<CustomerProductDiscountDto> page = customerProductDiscountService.findAll(pageable,embed, where);

        ResponseBodyDto<CustomerProductDiscountDto> responseBodyDto = new ResponseBodyDto<CustomerProductDiscountDto>(
                pageable, page, ResponseCodeEnum.R_200, "OK");
        return new ResponseEntity<ResponseBodyDto<CustomerProductDiscountDto>>(responseBodyDto, HttpStatus.OK);
    }

    @PatchMapping(value = "/customer-product-discounts/{id}", produces = "application/json")
    public ResponseEntity<ResponseBodyDto<CustomerProductDiscountDto>> update(
            @PathVariable(name = "id") String id,
            @RequestHeader("user_id") String userId,
            @RequestBody UpdateCustomerProductDiscountRequest request) {

        CustomerProductDiscountDto customerProductDiscountDto = customerProductDiscountService
                .updateCustomerProductDiscount(id, request,userId);

        ResponseBodyDto<CustomerProductDiscountDto> responseBodyDto = new ResponseBodyDto<>();
        responseBodyDto.setItem(customerProductDiscountDto).setCode(ResponseCodeEnum.R_200)
                .setMessage("OK");
        return new ResponseEntity<ResponseBodyDto<CustomerProductDiscountDto>>(responseBodyDto, HttpStatus.OK);
    }
}
