package com.ceti.wholesale.controller.api.v1;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.RevenueTruckAndDriverStatisticDto;
import com.ceti.wholesale.mapper.RevenueTruckAndDriverStatisticMapper;

@RestController
@RequestMapping("/v1")
public class RevenueTruckAndDriverStatisticController {
	
	@Autowired
	private RevenueTruckAndDriverStatisticMapper revenueTruckAndDriverStatisticMapper;
	
    @GetMapping(value = "/revenue-truck-and-driver-statistics", produces = "application/json")
    public ResponseEntity<ResponseBodyDto<RevenueTruckAndDriverStatisticDto>> getAll(
            @RequestParam(name = "factory_id", required = false) String factoryId ,
            @RequestParam(name = "type") String type ,
            @RequestParam(name = "weight", required = false) String weight,
    		@RequestParam(name ="date_from") Long dateFrom,
    		@RequestParam(name = "date_to") Long dateTo,
    		Pageable pageable) {

    	Instant from = Instant.ofEpochSecond(dateFrom);
    	Instant to = Instant.ofEpochSecond(dateTo);

    	List<List<Object>> result = revenueTruckAndDriverStatisticMapper.getAll(factoryId, from, to, type,weight, pageable.getPageNumber(), pageable.getPageSize());

    	List<RevenueTruckAndDriverStatisticDto> revenueTruckAndDriverStatisticDtos = (List<RevenueTruckAndDriverStatisticDto>) (Object)result.get(0);
    	long totalItems = (long)result.get(1).get(0);
        
 
        ResponseBodyDto<RevenueTruckAndDriverStatisticDto> responseBodyDto = new ResponseBodyDto<>(
        		revenueTruckAndDriverStatisticDtos, ResponseCodeEnum.R_200, "OK", totalItems);
        return new ResponseEntity<ResponseBodyDto<RevenueTruckAndDriverStatisticDto>>(responseBodyDto, HttpStatus.OK);
    }

}
