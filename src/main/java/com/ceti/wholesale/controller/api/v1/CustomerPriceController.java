package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateCustomerPriceRequest;
import com.ceti.wholesale.controller.api.request.ForwardCustomerPriceRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerPriceRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.CustomerPriceDto;
import com.ceti.wholesale.service.CustomerPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
@Validated
public class CustomerPriceController {

    @Autowired
    private CustomerPriceService customerPriceService;

    // get list customer price
    @GetMapping(value = "/customer-prices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerPriceDto>> getAllCustomer(
            @RequestParam(name = "customer_name", required = false) String customerName ,
            @RequestParam(name = "customer_category", required = false) String customerCategory ,
            @RequestParam(name = "month", required = false) Integer month ,
            @RequestParam(name = "year", required = false) Integer year ,
            @RequestHeader(name = "department_id") String factory_id ,
            Pageable pageable) {
        Page<CustomerPriceDto> page = customerPriceService
                .getAllByCondition(customerName , customerCategory , month , year , factory_id , pageable);
        ResponseBodyDto<CustomerPriceDto> res = new ResponseBodyDto<>(pageable , page , ResponseCodeEnum.R_200 , "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create customer price
    @PostMapping(value = "/customer-prices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerPriceDto>> createCustomerPrice(
            @RequestBody CreateCustomerPriceRequest request ,
            @RequestHeader(name = "department_id") String factory_id) {

        CustomerPriceDto customerPriceDto = customerPriceService.createCustomerPrice(request , factory_id);
        ResponseBodyDto<CustomerPriceDto> res = new ResponseBodyDto<>(customerPriceDto ,
                ResponseCodeEnum.R_201 , "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //update customer price
    @PatchMapping(value = "/customer-prices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<List<CustomerPriceDto>>> updateCustomerPrice(
            @RequestBody List<UpdateCustomerPriceRequest> request ,
            @RequestHeader(name = "department_id") String factoryId) {

        List<CustomerPriceDto> customerPriceDto = customerPriceService.updateCustomerPrice(request , factoryId);
        ResponseBodyDto<List<CustomerPriceDto>> res = new ResponseBodyDto<>(customerPriceDto ,
                ResponseCodeEnum.R_200 , "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // setForward
    @PostMapping(value = "/customer-prices/forward", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerPriceDto>> setForward(
            @RequestBody ForwardCustomerPriceRequest forwardRequest ,
            @RequestHeader(name = "department_id") String factoryId
    ) {

        Integer monthFrom = forwardRequest.getMonthFrom();
        Integer yearFrom = forwardRequest.getYearFrom();
        Integer monthTo = forwardRequest.getMonthTo();
        Integer yearTo = forwardRequest.getYearTo();
        Boolean check = customerPriceService
                .setForwardCustomerPrice(monthFrom , yearFrom , monthTo , yearTo , factoryId);

        if (check == null) {
            ResponseBodyDto<CustomerPriceDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_500 , "Lỗi dữ liệu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        } else if (!check) {
            ResponseBodyDto<CustomerPriceDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_400 ,
                    "Tháng " + monthFrom + " chưa có giá");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        } else {
            ResponseBodyDto<CustomerPriceDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_200 , "Kết chuyển thành công");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }

    }

    // apply price
    @PostMapping(value = "/customer-prices/apply", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerPriceDto>> applyCustomerPrice(
            @RequestBody ForwardCustomerPriceRequest forwardRequest ,
            @RequestHeader(name = "department_id") String factory_id
    ) {

        Integer monthTo = forwardRequest.getMonthTo();
        Integer yearTo = forwardRequest.getYearTo();
        Boolean check = customerPriceService.applyCustomerPrice(monthTo , yearTo , factory_id);

        if (check == null || !check) {
            ResponseBodyDto<CustomerPriceDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_500 , "Lỗi dữ liệu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        } else {
            ResponseBodyDto<CustomerPriceDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_200 , "Áp dụng giá thành công");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
    }


    // delete price
    @PostMapping(value = "/customer-prices/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteCustomerPrice(
            @RequestBody List<UpdateCustomerPriceRequest> request ,
            @RequestHeader(name = "department_id") String factoryId
    ) {

        Boolean checkDeleted = customerPriceService.deleteCustomerPrice(request , factoryId);

        if (checkDeleted == null || !checkDeleted) {
            ResponseBodyDto<String> res = new ResponseBodyDto<>(ResponseCodeEnum.R_500 , "Lỗi dữ liệu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        } else {
            ResponseBodyDto<String> res = new ResponseBodyDto<>(ResponseCodeEnum.R_200 , "Xóa giá thành công");
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }
    }

}
