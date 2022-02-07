package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateCustomerPriceByMonthRequest;
import com.ceti.wholesale.controller.api.request.ForwardCustomerPriceByMonthRequest;
import com.ceti.wholesale.controller.api.request.ForwardCustomerPriceRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerPriceByMonthRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerPriceRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.CustomerPriceByMonthDto;
import com.ceti.wholesale.dto.CustomerPriceDto;
import com.ceti.wholesale.service.CustomerPriceByMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@Validated
public class CustomerPriceByMonthController {

    @Autowired
    private CustomerPriceByMonthService customerPriceByMonthService;

    // get list customer price
    @GetMapping(value = "/customer-price-by-months", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerPriceByMonthDto>> getAllCustomer(
            @RequestParam(name = "customer_name", required = false) String customerName,
            @RequestParam(name = "customer_category", required = false) String customerCategory,
            @RequestParam(name = "year", required = false) Integer year,
            @RequestHeader(name = "department_id") String factory_id,
            Pageable pageable) {
        Page<CustomerPriceByMonthDto> page = customerPriceByMonthService
                .getAllByCondition(customerName, customerCategory, year, factory_id, pageable);
        ResponseBodyDto<CustomerPriceByMonthDto> res = new ResponseBodyDto<>(pageable, page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create customer price
    @PostMapping(value = "/customer-price-by-months", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerPriceByMonthDto>> createCustomerPrice(
            @RequestBody CreateCustomerPriceByMonthRequest request,
            @RequestHeader(name = "department_id") String factory_id) {

        CustomerPriceByMonthDto customerPriceByMonthDto = customerPriceByMonthService.createCustomerPriceByMonth(request, factory_id);
        ResponseBodyDto<CustomerPriceByMonthDto> res = new ResponseBodyDto<>(customerPriceByMonthDto,
                ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //update customer price
    @PatchMapping(value = "/customer-price-by-months", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<List<CustomerPriceByMonthDto>>> updateCustomerPrice(
            @RequestBody List<UpdateCustomerPriceByMonthRequest> request,
            @RequestHeader(name = "department_id") String factoryId) {

        List<CustomerPriceByMonthDto> customerPriceByMonthDtos = customerPriceByMonthService.updateCustomerPriceByMonth(request, factoryId);
        ResponseBodyDto<List<CustomerPriceByMonthDto>> res = new ResponseBodyDto<>(customerPriceByMonthDtos,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // setForward
    @PostMapping(value = "/customer-price-by-months/forward", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerPriceByMonthDto>> setForward(
            @RequestBody ForwardCustomerPriceByMonthRequest forwardRequest ,
            @RequestHeader(name = "department_id") String factoryId
    ) {

        Integer yearFrom = forwardRequest.getYearFrom();
        Integer yearTo = forwardRequest.getYearTo();
        Boolean check = customerPriceByMonthService
                .setForwardCustomerPriceByMonth(yearFrom , yearTo , factoryId);

        if (check == null) {
            ResponseBodyDto<CustomerPriceByMonthDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_500 , "Lỗi dữ liệu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        } else if (!check) {
            ResponseBodyDto<CustomerPriceByMonthDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_400 ,
                    "Năm " + yearFrom + " chưa có giá");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } else {
            ResponseBodyDto<CustomerPriceByMonthDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_200 , "Kết chuyển thành công");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }

    }

    // apply price
    @PostMapping(value = "/customer-price-by-months/apply", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerPriceByMonthDto>> applyCustomerPrice(
            @RequestBody ForwardCustomerPriceByMonthRequest forwardRequest ,
            @RequestHeader(name = "department_id") String factory_id
    ) {

        Integer year = forwardRequest.getYearTo();
        Boolean check = customerPriceByMonthService.applyCustomerPriceByMonth(year , factory_id);

        if (check == null || !check) {
            ResponseBodyDto<CustomerPriceByMonthDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_500 , "Lỗi dữ liệu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        } else {
            ResponseBodyDto<CustomerPriceByMonthDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_200 , "Áp dụng giá thành công");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
    }

    // delete price
    @PostMapping(value = "/customer-prices-by-months/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteCustomerPrice(
            @RequestBody List<UpdateCustomerPriceByMonthRequest> request ,
            @RequestHeader(name = "department_id") String factoryId
    ) {

        Boolean checkDeleted = customerPriceByMonthService.deleteCustomerPriceByMonth(request , factoryId);

        if (checkDeleted == null || !checkDeleted) {
            ResponseBodyDto<String> res = new ResponseBodyDto<>(ResponseCodeEnum.R_500 , "Lỗi dữ liệu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        } else {
            ResponseBodyDto<String> res = new ResponseBodyDto<>(ResponseCodeEnum.R_200 , "Xóa giá thành công");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
    }

}
