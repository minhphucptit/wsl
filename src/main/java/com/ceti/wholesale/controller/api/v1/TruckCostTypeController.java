package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.TruckCostTypeDto;
import com.ceti.wholesale.service.TruckCostTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class TruckCostTypeController {

    @Autowired
    private TruckCostTypeService truckCostTypeService;

    //Get list truck_cost_type
    @GetMapping(value = "/truck_cost_type", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckCostTypeDto>> getListAllTruckCostType() {

        List<TruckCostTypeDto> list = truckCostTypeService.getFindAll();
        ResponseBodyDto<TruckCostTypeDto> res = new ResponseBodyDto<>(list, ResponseCodeEnum.R_200, "OK", list.size());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
