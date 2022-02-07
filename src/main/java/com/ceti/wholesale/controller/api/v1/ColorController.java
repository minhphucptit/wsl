package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ColorDto;
import com.ceti.wholesale.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ColorController {
    @Autowired
    private ColorService colorService;

    @GetMapping(value = "/colors")
    public ResponseEntity<ResponseBodyDto<ColorDto>> getAllColor(Pageable pageable){
        Page<ColorDto> page = colorService.getAll(pageable);
        ResponseBodyDto<ColorDto> res = new ResponseBodyDto<>(pageable,page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
