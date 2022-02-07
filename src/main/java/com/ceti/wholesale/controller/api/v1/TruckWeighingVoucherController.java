package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateTruckWeighingVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckWeighingVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.TruckWeighingVoucherDto;
import com.ceti.wholesale.service.TruckWeighingVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@Validated
public class TruckWeighingVoucherController {

    @Autowired
    private TruckWeighingVoucherService truckWeighingVoucherService;

    // get list truck weighing voucher
    @GetMapping(value = "/truck-weighing-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckWeighingVoucherDto>> getAllTruckWeighingVoucher(
            @RequestParam MultiValueMap<String, String> where,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        where.add("factory_id", factoryId);
        Page<TruckWeighingVoucherDto> page = truckWeighingVoucherService.getAllByCondition(where, pageable);
        ResponseBodyDto<TruckWeighingVoucherDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create truck weighing voucher
    @PostMapping(value = "/truck-weighing-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckWeighingVoucherDto>> createTruckWeighingVoucher(
            @Valid @RequestBody CreateTruckWeighingVoucherRequest request,
            @RequestHeader(name = "department_id") String factoryId) {

        TruckWeighingVoucherDto truckWeighingVoucherDto = truckWeighingVoucherService.createTruckWeighingVoucher(request, factoryId);
        ResponseBodyDto<TruckWeighingVoucherDto> res = new ResponseBodyDto<>(truckWeighingVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update truck weighing voucher
    @PatchMapping(value = "/truck-weighing-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckWeighingVoucherDto>> updateTruckWeighingVoucher(
            @Valid @RequestBody UpdateTruckWeighingVoucherRequest request,
            @PathVariable(name = "id") String id) {

        TruckWeighingVoucherDto truckWeighingVoucherDto = truckWeighingVoucherService.updateTruckWeighingVoucher(id, request);
        ResponseBodyDto<TruckWeighingVoucherDto> res = new ResponseBodyDto<>(truckWeighingVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/truck-weighing-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteTruckWeighingVoucher(
            @PathVariable(name = "id") String id) {
        truckWeighingVoucherService.deleteTruckWeighingVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
