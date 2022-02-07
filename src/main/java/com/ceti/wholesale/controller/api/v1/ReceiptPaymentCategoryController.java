package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateReceiptPaymentCategoryRequest;
import com.ceti.wholesale.controller.api.request.UpdateReceiptPaymentCategoryRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ReceiptPaymentCategoryDto;
import com.ceti.wholesale.service.ReceiptPaymentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@Validated
public class ReceiptPaymentCategoryController {
    @Autowired
    ReceiptPaymentCategoryService receiptPaymentCategoryService;

    @GetMapping(value = "/receipt-payment-categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ReceiptPaymentCategoryDto>> getAllReceiptPaymentCategories(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "type", required = false) Boolean type,
            @RequestParam(value = "is_active", required = false) Boolean isActive,
            Pageable pageable) {
        Page<ReceiptPaymentCategoryDto> page = receiptPaymentCategoryService.getAllByCondition(search, id, type, isActive, pageable);
        ResponseBodyDto<ReceiptPaymentCategoryDto> res = new ResponseBodyDto<>(pageable, page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping(value = "/receipt-payment-categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ReceiptPaymentCategoryDto>> createReceiptPaymentCategory(@RequestBody CreateReceiptPaymentCategoryRequest receiptPaymentCategoryRequest) {
        ReceiptPaymentCategoryDto receiptPaymentCategoryDto = receiptPaymentCategoryService.createReceiptPaymentCategory(receiptPaymentCategoryRequest);
        ResponseBodyDto<ReceiptPaymentCategoryDto> res = new ResponseBodyDto<>(receiptPaymentCategoryDto, ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PatchMapping(value = "/receipt-payment-categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ReceiptPaymentCategoryDto>> updateReceiptPaymentCategory(
            @PathVariable(name = "id") String id,
            @RequestBody UpdateReceiptPaymentCategoryRequest request) {
        ReceiptPaymentCategoryDto receiptPaymentCategoryDto = receiptPaymentCategoryService.updateReceiptPaymentCategory(id, request);
        ResponseBodyDto<ReceiptPaymentCategoryDto> res = new ResponseBodyDto<>(receiptPaymentCategoryDto, ResponseCodeEnum.R_200, "ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
