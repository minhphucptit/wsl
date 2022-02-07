package com.ceti.wholesale.controller.api.v1;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.RevenueAndCycleStatisticCustomerDto;
import com.ceti.wholesale.dto.RevenueAndCycleStatisticDto;
import com.ceti.wholesale.mapper.RevenueAndCycleStatisticMapper;

@RestController
@RequestMapping("/v1")
public class RevenueAndCycleStatisticController {
	
    @Autowired
    private RevenueAndCycleStatisticMapper revenueAndCycleStatisticMapper;
    
    @GetMapping(value = "/revenue-and-cycles", produces = "application/json")
    public ResponseEntity<ResponseBodyDto<RevenueAndCycleStatisticCustomerDto>> getAll(
            @RequestParam(name = "customer_code", required = false) String customerCode ,
    		@RequestParam(name ="date_from") Long dateFrom,
    		@RequestParam(name = "date_to") Long dateTo) {

        List<List<Object>> result = revenueAndCycleStatisticMapper.getList(customerCode,
        		Instant.ofEpochSecond(dateFrom), Instant.ofEpochSecond(dateTo));
        
        List<RevenueAndCycleStatisticDto> revenueAndCycleStatisticDtos = (List<RevenueAndCycleStatisticDto>) (Object)result.get(0);
        
        RevenueAndCycleStatisticCustomerDto customer = ((List<RevenueAndCycleStatisticCustomerDto>) (Object)result.get(1)).get(0);
        
        if(customer != null) {
        	customer.setRevenueAndCycleStatistic(revenueAndCycleStatisticDtos);
        }
 
        ResponseBodyDto<RevenueAndCycleStatisticCustomerDto> responseBodyDto = new ResponseBodyDto<>(
        		customer, ResponseCodeEnum.R_200, "OK");
        return new ResponseEntity<ResponseBodyDto<RevenueAndCycleStatisticCustomerDto>>(responseBodyDto, HttpStatus.OK);
    }
}
