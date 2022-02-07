package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateMoneyInStockRequest;
import com.ceti.wholesale.controller.api.request.ForwardGoodInStockRequest;
import com.ceti.wholesale.controller.api.request.ForwardMoneyInStockRequest;
import com.ceti.wholesale.controller.api.request.UpdateMoneyInStockRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.GoodsInStockDto;
import com.ceti.wholesale.dto.MoneyInStockDto;
import com.ceti.wholesale.mapper.MoneyInStockMapper;
import com.ceti.wholesale.service.MoneyInStockService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Validated
public class MoneyInStockController {
    @Autowired
    MoneyInStockService moneyInStockService;

    @Autowired
    MoneyInStockMapper moneyInStockMapper;

    @GetMapping(value = "/money-in-stocks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<MoneyInStockDto>> getAll(
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        Page<MoneyInStockDto> page = moneyInStockService.getAllMoneyInStockByCondition(factoryId, pageable);
        ResponseBodyDto<MoneyInStockDto> res = new ResponseBodyDto<>(pageable, page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping(value = "/money-in-stocks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<MoneyInStockDto>> createReceiptPaymentCategory(
            @RequestBody CreateMoneyInStockRequest createMoneyInStockRequest,
            @RequestHeader(name = "department_id") String factoryId) {
        MoneyInStockDto moneyInStockDto = moneyInStockService.createMoneyInStock(createMoneyInStockRequest, factoryId);
        ResponseBodyDto<MoneyInStockDto> res = new ResponseBodyDto<>(moneyInStockDto, ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PatchMapping(value = "/money-in-stocks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<MoneyInStockDto>> update(
            @PathVariable(name = "id") String id,
            @RequestBody UpdateMoneyInStockRequest request) {
        MoneyInStockDto moneyInStockDto = moneyInStockService.updateMoneyInStock(id, request);
        ResponseBodyDto<MoneyInStockDto> res = new ResponseBodyDto<>(moneyInStockDto, ResponseCodeEnum.R_200, "ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/money-in-stocks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> delete(
            @PathVariable(name = "id") String id) {
        moneyInStockService.deleteMoneyInStock(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //forward
    @PostMapping(value = "/money-in-stocks/forward", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<MoneyInStockDto>> setForward(
            @RequestBody ForwardMoneyInStockRequest forwardRequest,
            @RequestHeader(name = "department_id") String factoryId
    ){
        Integer yearFrom = forwardRequest.getYearFrom();
        Integer yearTo = forwardRequest.getYearTo();
        String userFullName = forwardRequest.getUserFullName();
        moneyInStockMapper.setForwardToNextYear(yearFrom,yearTo,factoryId,userFullName);
        ResponseBodyDto<MoneyInStockDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_200, "Kết chuyển thành công");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
