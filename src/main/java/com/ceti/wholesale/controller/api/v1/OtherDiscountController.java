package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.OtherDiscountDto;
import com.ceti.wholesale.service.OtherDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class OtherDiscountController {
    @Autowired
    private OtherDiscountService otherDiscountService;

    @GetMapping(value = "/other-discounts",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<OtherDiscountDto>>
        getAllOtherDiscountByCustomerCodeAndMonthAndYear(
                @RequestParam(name = "customer_code") String customerCode,
                @RequestParam(name = "month") Integer month, @RequestParam(name = "year") Integer year, Pageable pageable){
        Page<OtherDiscountDto> page = otherDiscountService.getOtherDiscount(customerCode,month,year,pageable);
        ResponseBodyDto<OtherDiscountDto> res = new ResponseBodyDto<>(pageable,page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
