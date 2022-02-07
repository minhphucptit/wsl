package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.GasFillingQuantityByFactoryDto;
import com.ceti.wholesale.mapper.GasFillingQuantityByFactoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/v1")
@Validated
public class GasFillingQuantityByFactoryController {

    @Autowired
    GasFillingQuantityByFactoryMapper gasFillingQuantityByFactoryMapper;

    @GetMapping(value = "/gas-filling-quantity-by-factory", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GasFillingQuantityByFactoryDto>> getAllGasFillingQuantityByFactorys(
            @RequestParam(value = "date_from", required = false) Long dateFrom,
            @RequestParam(value = "date_to", required = false) Long dateTo,
            @RequestParam(name = "factory_id", required = false) String factoryId) {
        Instant from = dateFrom == null ? null : Instant.ofEpochSecond(dateFrom);
        Instant to = dateTo == null ? null : Instant.ofEpochSecond(dateTo);
        List<GasFillingQuantityByFactoryDto> data = gasFillingQuantityByFactoryMapper.getList(from, to, factoryId);
        ResponseBodyDto<GasFillingQuantityByFactoryDto> response = new ResponseBodyDto<>(data,
                ResponseCodeEnum.R_200, "OK", data.size());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
