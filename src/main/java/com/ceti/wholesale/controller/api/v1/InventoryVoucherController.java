package com.ceti.wholesale.controller.api.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.ceti.wholesale.controller.api.request.CreateInventoryVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateInventoryVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.InventoryVoucherDto;
import com.ceti.wholesale.service.InventoryVoucherService;

@RestController
@RequestMapping("/v1")
@Validated
public class InventoryVoucherController {

    @Autowired
    private InventoryVoucherService inventoryVoucherService;

    // get list inventory voucher
    @GetMapping(value = "/inventory-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<InventoryVoucherDto>> getAllInventoryVoucher(
            @RequestParam(value = "voucher_at_from", required = false) Long voucherAtFrom,
            @RequestParam(value = "voucher_at_to", required = false) Long voucherAtTo,
            @RequestParam(value = "no", required = false) String no,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        Page<InventoryVoucherDto> page = inventoryVoucherService.getAllByCondition(voucherAtFrom, voucherAtTo,no, factoryId, pageable);
        ResponseBodyDto<InventoryVoucherDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create inventory voucher
    @PostMapping(value = "/inventory-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<InventoryVoucherDto>> createInventoryVoucher(
            @Valid @RequestBody CreateInventoryVoucherRequest request,
            @RequestHeader(name = "department_id") String factoryId,
            @RequestParam(value = "is_created", defaultValue = "true") Boolean isCreated) {
        InventoryVoucherDto inventoryVoucherDto = inventoryVoucherService.createInventoryVoucher(request, factoryId, isCreated);
        ResponseBodyDto<InventoryVoucherDto> res = new ResponseBodyDto<>(inventoryVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
    
    // update inventory voucher
    @PatchMapping(value = "/inventory-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<InventoryVoucherDto>> updateInventoryVoucher(
            @Valid @RequestBody UpdateInventoryVoucherRequest request,
            @PathVariable(name = "id") String id,
            @RequestHeader(name = "department_id") String factoryId,
            @RequestParam(value = "is_created", defaultValue = "true") Boolean isCreated) {

        InventoryVoucherDto inventoryVoucherDto = inventoryVoucherService.updateInventoryVoucher(request, factoryId, id, isCreated);
        ResponseBodyDto<InventoryVoucherDto> res = new ResponseBodyDto<>(inventoryVoucherDto,
                ResponseCodeEnum.R_200, "Updated");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // delete inventory voucher
    @DeleteMapping(value = "/inventory-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteInventoryVoucher(
            @PathVariable(name = "id") String id,
            @RequestHeader(name = "department_id") String factoryId) {

        inventoryVoucherService.deleteInventoryVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
