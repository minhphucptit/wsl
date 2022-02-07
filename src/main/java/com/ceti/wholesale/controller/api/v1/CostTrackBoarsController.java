package com.ceti.wholesale.controller.api.v1;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateCostTrackBoardRequest;
import com.ceti.wholesale.controller.api.request.UpdateCostTrackBoardRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.CostTrackBoardDto;
import com.ceti.wholesale.dto.CostTrackBoardTotalQuantityAndTotalItemsDto;
import com.ceti.wholesale.mapper.CostTrackBoardMapper;
import com.ceti.wholesale.service.CostTrackBoardService;

@RestController
@RequestMapping("/v1")
public class CostTrackBoarsController {

    @Autowired
    private CostTrackBoardService costTrackBoardService;

    @Autowired
    private CostTrackBoardMapper costTrackBoardMapper;

    // get list cost_track_board
    @GetMapping(value = "/cost_track_board", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CostTrackBoardDto>> getListCostTrackBoard(
            @RequestParam(name = "action_date_from", required = false) Long actionDateFrom,
            @RequestParam(name = "action_date_to", required = false) Long actionDateTo,
            @RequestParam(name = "truck_cost_type_id", required = false) String truckCostTypeId,
            @RequestParam(name = "factory_id", required = false) String factoryId,
            @RequestParam(name = "truck_license_plate_number", required = false) String truckLicensePlateNumber,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "cost_track_board_type", required = true) String costTrackBoardType,
            @RequestParam(name = "equipment_id", required = false) String equipmentId,
            @RequestParam(name = "equipment_cost_type_name", required = false) String equipmentCostTypeName,
            Pageable pageable) {
        Instant from = actionDateFrom == null ? null : Instant.ofEpochSecond(actionDateFrom);
        Instant to = actionDateTo == null ? null : Instant.ofEpochSecond(actionDateTo);
        List<CostTrackBoardDto> data = costTrackBoardMapper.getList(from, to, truckCostTypeId,
        		factoryId, truckLicensePlateNumber, type, costTrackBoardType, equipmentId, equipmentCostTypeName,  pageable.getOffset(), pageable.getPageSize());
        long totalItem = costTrackBoardMapper.countList(from, to, truckCostTypeId,factoryId, truckLicensePlateNumber, type,
        		costTrackBoardType, equipmentId, equipmentCostTypeName);
        ResponseBodyDto<CostTrackBoardDto> response = new ResponseBodyDto<>(data, ResponseCodeEnum.R_200, "OK",
                totalItem);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // get total cost_track_board
    @GetMapping(value = "/cost_track_board/get-total", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CostTrackBoardTotalQuantityAndTotalItemsDto>> getTotal(
            @RequestParam(name = "action_date_from", required = false) Long actionDateFrom,
            @RequestParam(name = "action_date_to", required = false) Long actionDateTo,
            @RequestParam(name = "truck_cost_type_id", required = false) String truckCostTypeId,
            @RequestParam(name = "factory_id", required = false) String factoryId,
            @RequestParam(name = "truck_license_plate_number", required = false) String truckLicensePlateNumber,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "cost_track_board_type", required = true) String costTrackBoardType,
            @RequestParam(name = "equipment_id", required = false) String equipmentId,
            @RequestParam(name = "equipment_cost_type_name", required = false) String equipmentCostTypeName
    ) {
        Instant from = actionDateFrom == null ? null : Instant.ofEpochSecond(actionDateFrom);
        Instant to = actionDateTo == null ? null : Instant.ofEpochSecond(actionDateTo);
        CostTrackBoardTotalQuantityAndTotalItemsDto data = costTrackBoardMapper.getTotal(from, to, truckCostTypeId,factoryId, 
        		truckLicensePlateNumber, type, costTrackBoardType, equipmentId, equipmentCostTypeName);
        ResponseBodyDto<CostTrackBoardTotalQuantityAndTotalItemsDto> response = new ResponseBodyDto<>(data, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    // Create new cost_track_board
    @PostMapping(value = "/cost_track_board", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CostTrackBoardDto>> createCostTrackBoard(
            @RequestBody CreateCostTrackBoardRequest request,
            @RequestHeader(name = "department_id") String factoryId) {
        CostTrackBoardDto costTrackBoardDto = costTrackBoardService.createCostTrackBoard(request, factoryId);
        ResponseBodyDto<CostTrackBoardDto> res = new ResponseBodyDto<>(costTrackBoardDto,
                ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // Update cost_track_board
    @PutMapping(value = "/cost_track_board/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CostTrackBoardDto>> updateCostTrackBoard(
            @PathVariable(name = "id") String id,
            @RequestBody UpdateCostTrackBoardRequest request) {
        CostTrackBoardDto costTrackBoardDto = costTrackBoardService.updateCostTrackBoard(id, request);
        ResponseBodyDto<CostTrackBoardDto> res = new ResponseBodyDto<>(costTrackBoardDto,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // delete Cost Track Board
    @DeleteMapping(value = "/cost_track_board/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteCostTrackBoard(
            @PathVariable(name = "id") String id) {
        costTrackBoardService.deleteCostTrackBoard(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
