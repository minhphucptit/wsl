package com.ceti.wholesale.controller.api.v1;

import java.util.*;
import java.util.stream.Collectors;

import com.ceti.wholesale.mapper.GoodInOutMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.service.GoodsInOutService;

@RestController
@RequestMapping("/v1")
@Validated
public class GoodsInOutController {

    @Autowired
    private GoodsInOutService goodsInOutService;

    @Autowired
    private GoodInOutMapper goodInOutMapper;

    // get list good in out by voucher id
    @GetMapping(value = "/goods-in-out", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GoodsInOutDto>> getGoodsInOutByVoucherId(
            @RequestParam(name = "voucher_id") String voucherId,
            @RequestParam(name = "embed",required = false) String[] embed) {
        if (embed ==null || embed.length ==0){
            embed = new String[]{""};
        }
            List<GoodsInOutDto> list = goodInOutMapper.getList(voucherId,true, embed);
            long totalItems = goodInOutMapper.countList(voucherId,true,embed);
            ResponseBodyDto<GoodsInOutDto> res = new ResponseBodyDto<>(list,
                    ResponseCodeEnum.R_200, "OK", totalItems);
            return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
