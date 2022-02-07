package com.ceti.wholesale.controller.api;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping(value = "/live", produces = "application/json")
    public ResponseEntity<Object> healthLive() {
        ResponseBodyDto<Object> dtoResult = new ResponseBodyDto<>();
        dtoResult.setCode(ResponseCodeEnum.R_200);
        dtoResult.setMessage("OK");
        return new ResponseEntity<>(dtoResult, HttpStatus.OK);
    }

    @GetMapping(value = "/ready", produces = "application/json")
    public ResponseEntity<Object> healthReady() {
        ResponseBodyDto<Object> dtoResult = new ResponseBodyDto<>();
        dtoResult.setCode(ResponseCodeEnum.R_200);
        dtoResult.setMessage("OK");
        return new ResponseEntity<>(dtoResult, HttpStatus.OK);
    }
}
