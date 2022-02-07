package com.ceti.wholesale.controller.api.v1;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateTruckMonthlyFinalDoRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckMonthlyFinalDoRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.TruckMonthlyFinalDoDto;
import com.ceti.wholesale.service.TruckMonthlyFinalDoService;

@RestController
@RequestMapping("/v1")
@Validated
public class TruckMonthyFinalDoController {
	
    @Autowired
    TruckMonthlyFinalDoService truckMonthlyFinalDoService;
    
    // get list truck monthly final do
    @GetMapping(value = "/truck-monthly-final-dos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckMonthlyFinalDoDto>> getAll(
            @RequestParam MultiValueMap<String, String> where,
            Pageable pageable) {
    	Page<TruckMonthlyFinalDoDto> page = truckMonthlyFinalDoService.getAllByCondition(where, pageable);
        ResponseBodyDto<TruckMonthlyFinalDoDto> res = new ResponseBodyDto<>(page.getContent(),
                ResponseCodeEnum.R_200, "OK",page.getTotalElements());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    
    // create truck monthly final do
    @PostMapping(value = "/truck-monthly-final-dos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckMonthlyFinalDoDto>> create(
    		@Valid @RequestBody CreateTruckMonthlyFinalDoRequest request) {

    	TruckMonthlyFinalDoDto result = truckMonthlyFinalDoService.create(request);
        ResponseBodyDto<TruckMonthlyFinalDoDto> res = new ResponseBodyDto<>(result,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
    
    // update truck monthly final do
    @PatchMapping(value = "/truck-monthly-final-dos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckMonthlyFinalDoDto>> update(
    		@Valid @RequestBody  UpdateTruckMonthlyFinalDoRequest request,
            @PathVariable(name = "id") String id) {

    	TruckMonthlyFinalDoDto result = truckMonthlyFinalDoService.update(request, id);
        ResponseBodyDto<TruckMonthlyFinalDoDto> res = new ResponseBodyDto<>(result,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    
    // delete truck monthly final do
    @DeleteMapping(value = "/truck-monthly-final-dos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckMonthlyFinalDoDto>> delete(
            @PathVariable(name = "id") String id) {

    	truckMonthlyFinalDoService.delete(id);
        ResponseBodyDto<TruckMonthlyFinalDoDto> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    
    // create list truck monthly final do
    @PostMapping(value = "/truck-monthly-final-dos/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckMonthlyFinalDoDto>> createAll(
            @RequestParam(name = "factory_id", required = false) String factoryId ,
            @RequestParam(name = "truck_license_plate_number", required = false) String truckLicensePlateNumber ,
    		@RequestParam(name ="year") Integer year,
    		@RequestParam(name = "month") Integer month,
    		@RequestParam(name = "has_ps_difference") Boolean hasPsDifference) {

    	List<TruckMonthlyFinalDoDto> result = truckMonthlyFinalDoService.createAll(factoryId, month, year, truckLicensePlateNumber,hasPsDifference);
        ResponseBodyDto<TruckMonthlyFinalDoDto> res = new ResponseBodyDto<>(result,
                ResponseCodeEnum.R_201, "Created",result.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

}
