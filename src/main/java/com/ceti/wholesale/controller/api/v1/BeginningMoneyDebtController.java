package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateMoneyDebtRequest;
import com.ceti.wholesale.controller.api.request.ForwardBeginningMoneyDebtRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.BeginningMoneyDebtDto;
import com.ceti.wholesale.dto.CustomerPriceDto;
import com.ceti.wholesale.mapper.BeginningMoneyDebtMapper;
import com.ceti.wholesale.service.BeginningMoneyDebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

@Controller
@RequestMapping("/v1")
@Validated
public class BeginningMoneyDebtController {

    @Autowired
    BeginningMoneyDebtService moneyDebtService;

    @Autowired
    BeginningMoneyDebtMapper beginningMoneyDebtMapper;

    // get all money debt
    @GetMapping(value = "/beginning-money-debts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<BeginningMoneyDebtDto>> getAllMoneyDebt(
            @RequestParam MultiValueMap<String, String> where,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        where.add("factory_id", factoryId);
        Page<BeginningMoneyDebtDto> page = moneyDebtService.getAllMoneyDebt(where, pageable);
        ResponseBodyDto<BeginningMoneyDebtDto> res = new ResponseBodyDto<>(pageable, page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create new money debt
    @PostMapping(value = "/beginning-money-debts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<BeginningMoneyDebtDto>> createMoneyDebt(@RequestBody CreateMoneyDebtRequest createMoneyDebtRequest,
                                                                                  @RequestHeader(name = "department_id") String factoryId) {
        BeginningMoneyDebtDto beginningMoneyDebtDto = moneyDebtService.createMoneyDebt(createMoneyDebtRequest, factoryId);
        ResponseBodyDto<BeginningMoneyDebtDto> res = new ResponseBodyDto<>(beginningMoneyDebtDto, ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //update money debt
    @PatchMapping(value = "/beginning-money-debts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<BeginningMoneyDebtDto>> updateMoneyDebt(
            @PathVariable(name = "id") String id, @RequestBody CreateMoneyDebtRequest request) {
        BeginningMoneyDebtDto beginningMoneyDebtDto = moneyDebtService.updateMoneyDebt(id, request);
        ResponseBodyDto<BeginningMoneyDebtDto> res = new ResponseBodyDto<>(beginningMoneyDebtDto, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/beginning-money-debts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteBeginningMoneyDebt(
            @PathVariable(name = "id") String id) {
        moneyDebtService.deleteBeginningMoneyDebt(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //forward
    @PostMapping(value = "/beginning-money-debts/forward", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<BeginningMoneyDebtDto>> setForward(
            @RequestBody ForwardBeginningMoneyDebtRequest forwardRequest,
            @RequestHeader(name = "department_id") String factoryId
            ){
        Integer yearFrom = forwardRequest.getYearFrom();
        Integer yearTo = forwardRequest.getYearTo();
        String userFullName = forwardRequest.getUserFullName();
        beginningMoneyDebtMapper.setForwardToNextYear(yearFrom,yearTo,factoryId,userFullName);

        ResponseBodyDto<BeginningMoneyDebtDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_200, "Kết chuyển thành công");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
