package com.ceti.wholesale.controller.api.v2;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.v2.CreateTruckWeighingVoucherRequest;
import com.ceti.wholesale.controller.api.request.v2.UpdateTruckWeighingVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.TruckWeighingVoucherDto;
import com.ceti.wholesale.service.v2.TruckWeighingVoucherServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v2")
@Validated
public class TruckWeighingVoucherControllerV2 {

    @Autowired
    private TruckWeighingVoucherServiceV2 truckWeighingVoucherServiceV2;


    // create truck weighing voucher
    @PostMapping(value = "/truck-weighing-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckWeighingVoucherDto>> createTruckWeighingVoucher(
            @Valid @RequestBody CreateTruckWeighingVoucherRequest request) {

        TruckWeighingVoucherDto truckWeighingVoucherDto = truckWeighingVoucherServiceV2.createTruckWeighingVoucher(request);
        ResponseBodyDto<TruckWeighingVoucherDto> res = new ResponseBodyDto<>(truckWeighingVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update truck weighing voucher by command reference id
    @PatchMapping(value = "/truck-weighing-vouchers/{command_reference_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckWeighingVoucherDto>> updateTruckWeighingVoucher(
            @Valid @RequestBody UpdateTruckWeighingVoucherRequest request,
            @PathVariable(name = "command_reference_id") String commandReferenceId) {

        TruckWeighingVoucherDto truckWeighingVoucherDto = truckWeighingVoucherServiceV2.updateTruckWeighingVoucher(commandReferenceId, request);
        ResponseBodyDto<TruckWeighingVoucherDto> res = new ResponseBodyDto<>(truckWeighingVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/truck-weighing-vouchers/{command_reference_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteTruckWeighingVoucherByCommandReferenceId(
            @PathVariable(name = "command_reference_id") String commandReferenceId) {
        truckWeighingVoucherServiceV2.deleteTruckWeighingVoucherByCommandReferenceId(commandReferenceId);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
