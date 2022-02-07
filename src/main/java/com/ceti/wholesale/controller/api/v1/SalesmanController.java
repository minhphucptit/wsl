package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateSalemanRequest;
import com.ceti.wholesale.controller.api.request.UpdateSalemanRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.SalesmanDto;
import com.ceti.wholesale.dto.TruckDriverDto;
import com.ceti.wholesale.service.SalesmanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Validated
public class SalesmanController {

    @Autowired
    SalesmanService salesmanService;

//    //Get list salesmans
//    @GetMapping(value = "/salesmans", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ResponseBodyDto<SalesmanDto>> getAllSalesmans(
//            @RequestParam(name = "search", required = false) String search,
//            @RequestParam(name = "id", required = false) String id,
//            @RequestParam(name = "abbreviated_name", required = false) String abbreviatedName,
//            @RequestParam(name = "full_name", required = false) String fullName,
//            @RequestParam(name = "address", required = false) String address,
//            @RequestParam(name = "phone_number", required = false) String phoneNumber,
//            @RequestParam(name = "is_active", required = false) Boolean isActive,
//            @RequestHeader(name = "department_id") String factoryId,
//            Pageable pageable) {
//
//        Page<SalesmanDto> page = salesmanService.getAllByCondition(search, id, abbreviatedName, fullName, address, phoneNumber, factoryId, isActive, pageable);
//
//        ResponseBodyDto<SalesmanDto> res = new ResponseBodyDto<>(pageable, page, ResponseCodeEnum.R_200, "OK");
//        return ResponseEntity.status(HttpStatus.OK).body(res);
//    }

    //Get list salesmans
    @GetMapping(value = "/salesmans", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<SalesmanDto>> getAllTruckDrivers(

            @RequestParam MultiValueMap<String, String> where,
            @RequestParam(value = "embed", required = false) String embed,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        Page<SalesmanDto> page = salesmanService.getAllByCondition(where, embed, factoryId, pageable);
        ResponseBodyDto<SalesmanDto> res = new ResponseBodyDto<>(pageable, page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //create new Saleman
    @PostMapping(value = "salesmans", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<SalesmanDto>> createSaleman(
            @RequestBody CreateSalemanRequest request,
            @RequestHeader(name = "department_id") String factoryId) {
        SalesmanDto salesmanDto = salesmanService.createSaleman(request, factoryId);
        ResponseBodyDto<SalesmanDto> res = new ResponseBodyDto<>(salesmanDto, ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //update Saleman
    @PatchMapping(value = "salesmans/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<SalesmanDto>> updateSaleman(
            @PathVariable(name = "id") String id,
            @RequestBody UpdateSalemanRequest request
    ) {
        SalesmanDto salesmanDto = salesmanService.updateSaleman(id, request);
        ResponseBodyDto<SalesmanDto> res = new ResponseBodyDto<>(salesmanDto, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
