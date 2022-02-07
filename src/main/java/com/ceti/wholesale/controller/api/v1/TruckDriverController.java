package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateTruckDriverRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckDriverRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.TruckDriverDto;
import com.ceti.wholesale.service.TruckDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Validated
public class TruckDriverController {

    @Autowired
    TruckDriverService truckDriverService;

    //Get list truck drivers
    @GetMapping(value = "/truck-drivers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckDriverDto>> getAllTruckDrivers(

            @RequestParam MultiValueMap<String, String> where,
            @RequestParam(value = "embed", required = false) String embed,
            Pageable pageable) {
        Page<TruckDriverDto> page = truckDriverService.getAllByCondition(where, embed, pageable);
        ResponseBodyDto<TruckDriverDto> res = new ResponseBodyDto<>(pageable, page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    // Create new Truck driver
    @PostMapping(value = "/truck-drivers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckDriverDto>> createTruckDriver(
            @RequestBody CreateTruckDriverRequest request,
            @RequestHeader(name = "department_id") String factoryId) {
        TruckDriverDto truckDriverDto = truckDriverService.createTruckDriver(request, factoryId);
        ResponseBodyDto<TruckDriverDto> res = new ResponseBodyDto<>(truckDriverDto,
                ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // Update Truck driver
    @PutMapping(value = "/truck-drivers/{truck-driver-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckDriverDto>> updateTruckDriver(
            @PathVariable(name = "truck-driver-id") String truckDriverId,
            @RequestBody UpdateTruckDriverRequest request) {
        TruckDriverDto truckDriverDto = truckDriverService.updateTruckDriver(truckDriverId, request);

        ResponseBodyDto<TruckDriverDto> res = new ResponseBodyDto<>(truckDriverDto,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
