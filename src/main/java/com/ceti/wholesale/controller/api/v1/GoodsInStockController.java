package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateGoodsInStockRequest;
import com.ceti.wholesale.controller.api.request.ForwardBeginningMoneyDebtRequest;
import com.ceti.wholesale.controller.api.request.ForwardGoodInStockRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.BeginningCylinderDebtDto;
import com.ceti.wholesale.dto.BeginningMoneyDebtDto;
import com.ceti.wholesale.dto.GoodsInStockDto;
import com.ceti.wholesale.mapper.GoodsInStockMapper;
import com.ceti.wholesale.service.GoodsInStockService;
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
public class GoodsInStockController {
    @Autowired
    private GoodsInStockService goodsInStockService;

    @Autowired
    private GoodsInStockMapper goodsInStockMapper;

    @GetMapping(value = "/goods-in-stocks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GoodsInStockDto>> getAllGoodsInStock(
            @RequestParam MultiValueMap<String, String> where,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable
    ) {
        where.add("factory_id", factoryId);
        Page<GoodsInStockDto> page = goodsInStockService.getAllByCondition(where, pageable);

        ResponseBodyDto<GoodsInStockDto> res = new ResponseBodyDto<>(pageable, page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create new goods in stock
    @PostMapping(value = "/goods-in-stocks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GoodsInStockDto>> createGoodsInStock(
            @RequestBody CreateGoodsInStockRequest request,
            @RequestHeader(name = "department_id") String factoryId
    ) {
        GoodsInStockDto goodsInStockDto = goodsInStockService.createGoodsInStock(request, factoryId);
        ResponseBodyDto<GoodsInStockDto> res = new ResponseBodyDto<>(goodsInStockDto, ResponseCodeEnum.R_201, "CREATE");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //update goods in stock
    @PatchMapping(value = "/goods-in-stocks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GoodsInStockDto>> updateGoodsInStock(
            @PathVariable(name = "id") String id,
            @RequestBody CreateGoodsInStockRequest request
    ) {
        GoodsInStockDto goodsInStockDto = goodsInStockService.updateGoodsInStock(id, request);
        ResponseBodyDto<GoodsInStockDto> res = new ResponseBodyDto<>(goodsInStockDto, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/goods-in-stocks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteGoodsInStock(
            @PathVariable(name = "id") String id) {
        goodsInStockService.deleteGoodsInStock(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //forward
    @PostMapping(value = "/goods-in-stocks/forward", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GoodsInStockDto>> setForward(
            @RequestBody ForwardGoodInStockRequest forwardRequest,
            @RequestHeader(name = "department_id") String factoryId
    ){
        Integer yearFrom = forwardRequest.getYearFrom();
        Integer yearTo = forwardRequest.getYearTo();
        String userFullName = forwardRequest.getUserFullName();
        goodsInStockMapper.setForwardToNextYear(yearFrom,yearTo,factoryId,userFullName);
        ResponseBodyDto<GoodsInStockDto> res = new ResponseBodyDto<>(ResponseCodeEnum.R_200, "Kết chuyển thành công");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
