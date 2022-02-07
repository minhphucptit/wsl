package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.TruckCostStatisticDto;
import com.ceti.wholesale.mapper.TruckCostStatisticMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class TruckCostStatisticController {

    @Autowired
    private TruckCostStatisticMapper truckCostStatisticMapper;

    // get list truck-cost-statistic
    @GetMapping(value = "/statistic/truck-cost-statistic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<TruckCostStatisticDto>> getListTruckCostTrackBoard(
            @RequestParam(name = "date_from", required = false) Long dateFrom,
            @RequestParam(name = "date_to", required = false) Long dateTo,
            @RequestParam(name = "factory_id", required = false) String factoryId,
            @RequestParam(name = "type") String type,
            @RequestParam(name = "truck_license_plate_number", required = false) String truckLicensePlateNumber) {
        Instant from = dateFrom == null ? null : Instant.ofEpochSecond(dateFrom);
        Instant to = dateTo == null ? null : Instant.ofEpochSecond(dateTo);
        List<TruckCostStatisticDto> data = truckCostStatisticMapper.getList(from, to, factoryId, truckLicensePlateNumber,type);
        ResponseBodyDto<TruckCostStatisticDto> response = new ResponseBodyDto<>(data, ResponseCodeEnum.R_200, "OK",
                data.size());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
