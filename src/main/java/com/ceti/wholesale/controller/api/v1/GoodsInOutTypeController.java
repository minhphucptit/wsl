package com.ceti.wholesale.controller.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.GoodsInOutTypeDto;
import com.ceti.wholesale.mapper.GoodsInOutTypeMapper;
import com.ceti.wholesale.service.GoodsInOutTypeService;

@RestController
@RequestMapping("/v1")
@Validated
public class GoodsInOutTypeController {

    @Autowired
    GoodsInOutTypeMapper goodsInOutTypeMapper;

    @Autowired
    GoodsInOutTypeService goodsInOutTypeService;

    @GetMapping(value = "/voucher-codes/{voucher_code}/goods-in-out-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GoodsInOutTypeDto>> getAll(
            @PathVariable(value = "voucher_code") String voucherCode,
            @RequestParam(name = "code_not_equal", required = false) String codeNotEqual,
            Pageable pageable) {
        List<GoodsInOutTypeDto> data = goodsInOutTypeMapper.getList(voucherCode, codeNotEqual, pageable.getOffset(), pageable.getPageSize());
        long totalItem = goodsInOutTypeMapper.countList(voucherCode, codeNotEqual);
        ResponseBodyDto<GoodsInOutTypeDto> response = new ResponseBodyDto<>(data,
                ResponseCodeEnum.R_200, "OK", totalItem);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/goods-in-out-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GoodsInOutTypeDto>> getAllGoodsInOutTypes(
            @RequestParam(name = "code", required = false) String code) {
        List<GoodsInOutTypeDto> list = goodsInOutTypeService.getGoodsInOutTypeByCode(code);
        ResponseBodyDto<GoodsInOutTypeDto> response = new ResponseBodyDto<>(list,
                ResponseCodeEnum.R_200, "OK", list.size());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
