package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateBeginningCylinderDebtRequest;
import com.ceti.wholesale.controller.api.request.ForwardBeginningCylinderDebtRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.BeginningCylinderDebtDto;
import com.ceti.wholesale.mapper.BeginningCylinderDebtMapper;
import com.ceti.wholesale.service.BeginningCylinderDebtService;
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

@RestController
@RequestMapping("/v1")
@Validated
public class BeginningCylinderDebtController {

    @Autowired
    private BeginningCylinderDebtService beginningCylinderDebtService;

    @Autowired
    private BeginningCylinderDebtMapper beginningCylinderDebtMapper;

    // get list beginning cylinder debt
    @GetMapping(value = "/beginning-cylinder-debts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<BeginningCylinderDebtDto>> getAllBeginningCylinderDebt(
            @RequestParam MultiValueMap<String, String> where,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        where.add("factory_id", factoryId);
        Page<BeginningCylinderDebtDto> page = beginningCylinderDebtService.getAllByCondition(where, pageable);
        ResponseBodyDto<BeginningCylinderDebtDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create beginning cylinder debt
    @PostMapping(value = "/beginning-cylinder-debts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<BeginningCylinderDebtDto>> createBeginningCylinderDebt(
            @RequestBody CreateBeginningCylinderDebtRequest request,
            @RequestHeader(name = "department_id") String factoryId) {
        BeginningCylinderDebtDto beginningCylinderDebtDto = beginningCylinderDebtService.createBeginningCylinderDebt(request, factoryId);
        ResponseBodyDto<BeginningCylinderDebtDto> res = new ResponseBodyDto<>(beginningCylinderDebtDto,
                ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update beginning cylinder debt
    @PatchMapping(value = "/beginning-cylinder-debts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<BeginningCylinderDebtDto>> updateBeginningCylinderDebt(
            @PathVariable(name = "id") String id,
            @RequestBody CreateBeginningCylinderDebtRequest request) {
        BeginningCylinderDebtDto beginningCylinderDebtDto = beginningCylinderDebtService.updateBeginningCylinderDebt(request, id);
        ResponseBodyDto<BeginningCylinderDebtDto> res = new ResponseBodyDto<>(beginningCylinderDebtDto,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/beginning-cylinder-debts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteBeginningCylinderDebt(
            @PathVariable(name = "id") String id) {
        beginningCylinderDebtService.deleteBeginningCylinderDebt(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //forward
    @PostMapping(value = "/beginning-cylinder-debts/forward", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<BeginningCylinderDebtDto>> setForward(
            @RequestBody ForwardBeginningCylinderDebtRequest forwardRequest,
            @RequestHeader(name = "department_id") String factoryId
    ){
        Integer yearFrom = forwardRequest.getYearFrom();
        Integer yearTo = forwardRequest.getYearTo();
        String userFullName = forwardRequest.getUserFullName();
        beginningCylinderDebtMapper.setForwardToNextYear(yearFrom,yearTo,factoryId,userFullName);

        ResponseBodyDto<BeginningCylinderDebtDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_200, "Kết chuyển thành công");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
