package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ScaleConfigDto;
import com.ceti.wholesale.service.ScaleConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1")
@Validated
public class ScaleConfigController {
    @Autowired
    ScaleConfigService scaleConfigService;

    @GetMapping(value = "/scale_config", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ScaleConfigDto>> getByFactoryId(
            @RequestHeader(name = "department_id") String factoryId) {
        ScaleConfigDto scaleConfig = scaleConfigService.getByFactoryId(factoryId);
        ResponseBodyDto<ScaleConfigDto> response = new ResponseBodyDto<>(scaleConfig,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
