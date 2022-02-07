package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateCustomerProductPriceRequest;
import com.ceti.wholesale.controller.api.request.CustomerProductPriceUpdatePriceRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerProductPriceRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.CustomerProductPriceDto;
import com.ceti.wholesale.mapper.CustomerProductPriceMapper;
import com.ceti.wholesale.service.CustomerProductPriceService;
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
public class CustomerProductPriceController {

    @Autowired
    private CustomerProductPriceService customerProductPriceService;

    @Autowired
    private CustomerProductPriceMapper customerProductPriceMapper;

    @PostMapping(value = "/customer-product-prices",produces = "application/json")
    public ResponseEntity<ResponseBodyDto<CustomerProductPriceDto>> create(
            @RequestHeader("user_id") String userId,
            @Valid @RequestBody CreateCustomerProductPriceRequest request
            ){
        CustomerProductPriceDto productPriceDto = customerProductPriceService.createCustomerProductPrice(request,userId);
        ResponseBodyDto<CustomerProductPriceDto> responseBodyDto = new ResponseBodyDto<>();
        responseBodyDto.setItem(productPriceDto).setCode(ResponseCodeEnum.R_201)
                .setMessage("CREATED");
        return new ResponseEntity<ResponseBodyDto<CustomerProductPriceDto>>(responseBodyDto, HttpStatus.CREATED);
    }
    @GetMapping(value = "/customer-product-prices", produces = "application/json")
    public ResponseEntity<ResponseBodyDto<CustomerProductPriceDto>> getAllCustomerProductDiscount(
            Pageable pageable,
            @QueryParam("embed") String[] embed, @RequestParam MultiValueMap<String, String> where) {

        Page<CustomerProductPriceDto> page = customerProductPriceService.findAll(pageable,embed, where);

        ResponseBodyDto<CustomerProductPriceDto> responseBodyDto = new ResponseBodyDto<CustomerProductPriceDto>(
                pageable, page, ResponseCodeEnum.R_200, "OK");
        return new ResponseEntity<ResponseBodyDto<CustomerProductPriceDto>>(responseBodyDto, HttpStatus.OK);
    }

    @PatchMapping(value = "/customer-product-prices/{id}", produces = "application/json")
    public ResponseEntity<ResponseBodyDto<CustomerProductPriceDto>> update(
            @PathVariable(name = "id") String id,
            @RequestHeader("user_id") String userId,
            @RequestBody UpdateCustomerProductPriceRequest request) {

        CustomerProductPriceDto customerProductPriceDto = customerProductPriceService
                .updateCustomerProductPrice(id, request,userId);

        ResponseBodyDto<CustomerProductPriceDto> responseBodyDto = new ResponseBodyDto<>();
        responseBodyDto.setItem(customerProductPriceDto).setCode(ResponseCodeEnum.R_200)
                .setMessage("OK");
        return new ResponseEntity<ResponseBodyDto<CustomerProductPriceDto>>(responseBodyDto, HttpStatus.OK);
    }
    
    // update price
    @PostMapping(value = "/customer-product-prices/update-prices",produces = "application/json")
    public ResponseEntity<ResponseBodyDto<String>> updatePrice(
            @RequestBody CustomerProductPriceUpdatePriceRequest request
    ) {
        Integer check =  customerProductPriceMapper.updateCustomerProductPrice(request.getPrice(),request.getFactoryId(), request.getListCustomerId(),request.getListProductId());
        ResponseBodyDto<String> res = new ResponseBodyDto<>(ResponseCodeEnum.R_200 , "Đã cập nhật "+check +" bản ghi thành công");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
