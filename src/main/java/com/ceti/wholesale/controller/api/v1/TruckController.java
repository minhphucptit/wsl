package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateTruckRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.TruckDto;
import com.ceti.wholesale.mapper.TruckMapper;
import com.ceti.wholesale.service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class TruckController {

    @Autowired
    private TruckService truckService;

    @Autowired
    private TruckMapper truckMapper;

    //Get list truck
    // api lấy danh sách tất cả các xe ở tất cả các kho
    @GetMapping(value = "/trucks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckDto>> getListTrucks(
            @RequestParam Map<String, String> where,
            Pageable pageable) {
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "truck");
        List<TruckDto> list =truckMapper.getList(where,pagingStr);
        long totalItems = truckMapper.countList(where);
//        Page<TruckDto> page = truckService.getAllByConditionWithEmbed(where, pageable);
        ResponseBodyDto<TruckDto> res = new ResponseBodyDto<>(list, ResponseCodeEnum.R_200, "OK",totalItems);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // Create new Truck
    @PostMapping(value = "/trucks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckDto>> createTruck(
            @RequestBody CreateTruckRequest request,
            @RequestHeader(name = "department_id") String factoryId) {
        TruckDto truckDto = truckService.createTruck(request, factoryId);
        ResponseBodyDto<TruckDto> res = new ResponseBodyDto<>(truckDto,
                ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // Update Truck
    @PutMapping(value = "/trucks/{license_plate_number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckDto>> updateTruck(
            @PathVariable(name = "license_plate_number") String licensePlateNumber,
            @RequestBody UpdateTruckRequest request) {
        TruckDto truckDto = truckService.updateTruck(licensePlateNumber, request);
        ResponseBodyDto<TruckDto> res = new ResponseBodyDto<>(truckDto,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
