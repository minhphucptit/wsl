package com.ceti.wholesale.controller.api.v1;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateTruckDistanceListRequest;
import com.ceti.wholesale.controller.api.request.CreateTruckDistanceRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckDistanceRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.GasSettlementCustomDto;
import com.ceti.wholesale.dto.GasSettlementDto;
import com.ceti.wholesale.dto.TruckDistanceDto;
import com.ceti.wholesale.mapper.TruckDistanceMapper;
import com.ceti.wholesale.service.TruckDistanceService;

@RestController
@RequestMapping("/v1")
public class TruckDistanceController {
	
	@Autowired
	TruckDistanceService truckDistanceService;
	
	@Autowired
	TruckDistanceMapper truckDistanceMapper;
	
    //Create truck distance
    @PostMapping(value = "/truck-distances", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckDistanceDto>> create(
            @RequestBody CreateTruckDistanceRequest request) {
    	TruckDistanceDto truckDistanceDto = truckDistanceService.create(request);
        ResponseBodyDto<TruckDistanceDto> res = new ResponseBodyDto<>(truckDistanceDto,
                ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //Create list truck distance
    @PostMapping(value = "/truck-distances-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckDistanceDto>> createList(
            @RequestBody CreateTruckDistanceListRequest request,
            @RequestHeader(name = "department_id") String factoryId){
        List<TruckDistanceDto> result =
                truckDistanceService.createList(request, factoryId);

        ResponseBodyDto<TruckDistanceDto> response = new ResponseBodyDto<>(result, ResponseCodeEnum.R_201,"CREATED",
                result.size());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    //Update truck distance
    @PatchMapping(value = "/truck-distances", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckDistanceDto>> update(
            @RequestParam(name = "truck_license_plate_number") String truckLicensePlateNumber,
            @RequestParam(name = "day") Long day,
            @RequestBody UpdateTruckDistanceRequest request) {
    	TruckDistanceDto truckDistanceDto = truckDistanceService.update(request, truckLicensePlateNumber, Instant.ofEpochSecond(day));
        ResponseBodyDto<TruckDistanceDto> res = new ResponseBodyDto<>(truckDistanceDto,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    
    //delete truck distance
    @DeleteMapping(value = "/truck-distances", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckDistanceDto>> delete(
            @RequestParam(name = "truck_license_plate_number") String truckLicensePlateNumber,
            @RequestParam(name = "day") Long day) {
    	truckDistanceService.delete(truckLicensePlateNumber, Instant.ofEpochSecond(day));
        ResponseBodyDto<TruckDistanceDto> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    
    //lấy quyết toán dầu theo tháng
    @GetMapping(value = "/truck-distances/gas-settlements", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GasSettlementCustomDto>> getGasSettlement(
            @RequestParam(name = "year") Integer year,
            @RequestParam(name = "month") Integer month,
            @RequestParam(name = "truck_license_plate_number") String truckLicensePlateNumber) {

    	List<List<Object>> list = truckDistanceMapper.getList(month, year, truckLicensePlateNumber);
    	
    	List<GasSettlementDto> gasSettlementDto = (List<GasSettlementDto>) (Object)list.get(0);
    	GasSettlementCustomDto gasSettlementCustomDto = (GasSettlementCustomDto) list.get(1).get(0);
    	
    	gasSettlementCustomDto.setGasSettlements(gasSettlementDto);
    	
    	ResponseBodyDto<GasSettlementCustomDto> res = new ResponseBodyDto<>(gasSettlementCustomDto,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
