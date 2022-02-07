package com.ceti.wholesale.controller.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.GoodsInventoryDto;
import com.ceti.wholesale.mapper.GoodsInventoryMapper;
import com.ceti.wholesale.service.GoodsInventoryService;

@RestController
@RequestMapping("/v1")
@Validated
public class GoodsInventoryController {

//    @Autowired
//    private GoodsInventoryService inventoryVoucherService;
    
    @Autowired
    private GoodsInventoryMapper goodsInventoryMapper;
    
    @PatchMapping(value = "/inventory-vouchers/{inventory_voucher_id}/goods-inventories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GoodsInventoryDto>> getAll(
            @PathVariable(name = "inventory_voucher_id") String inventoryVoucherId,
            @RequestParam(name = "product_type", required = false)String productType
    		) {
        List<GoodsInventoryDto> inventoryVoucherDtos = goodsInventoryMapper.getList(inventoryVoucherId, productType);
        ResponseBodyDto<GoodsInventoryDto> res = new ResponseBodyDto<GoodsInventoryDto>(inventoryVoucherDtos,
                ResponseCodeEnum.R_200, "OK", inventoryVoucherDtos.size());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
