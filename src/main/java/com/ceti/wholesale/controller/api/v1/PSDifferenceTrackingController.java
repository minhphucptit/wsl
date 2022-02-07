package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.PSDifferenceTrackingDto;
import com.ceti.wholesale.dto.RevenueTruckAndDriverStatisticDto;
import com.ceti.wholesale.mapper.PSDifferenceTrackingMapper;
import com.ceti.wholesale.mapper.RevenueTruckAndDriverStatisticMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class PSDifferenceTrackingController {
	
	@Autowired
	private PSDifferenceTrackingMapper psDifferenceTrackingMapper;
	
    @GetMapping(value = "/ps-difference-trackings", produces = "application/json")
    public ResponseEntity<ResponseBodyDto<PSDifferenceTrackingDto>> getAll(
            @RequestParam(name = "factory_id", required = false) String factoryId ,
            @RequestParam(name = "truck_license_plate_number", required = false) String truckLicensePlateNumber ,
    		@RequestParam(name ="year") Integer year,
    		@RequestParam(name = "month") Integer month,
    		@RequestParam(name = "has_ps_difference") Boolean hasPsDifference,
    		Pageable pageable) {

    	List<List<Object>> result = psDifferenceTrackingMapper.getAll(factoryId, month, year,truckLicensePlateNumber, hasPsDifference,pageable.getOffset(), pageable.getPageSize());

    	List<PSDifferenceTrackingDto> psDifferenceTrackingDtos = (List<PSDifferenceTrackingDto>) (Object)result.get(0);
    	long totalItems = (long)result.get(1).get(0);

        ResponseBodyDto<PSDifferenceTrackingDto> responseBodyDto = new ResponseBodyDto<>(
				psDifferenceTrackingDtos, ResponseCodeEnum.R_200, "OK", totalItems);
        return new ResponseEntity<>(responseBodyDto, HttpStatus.OK);
    }

}
