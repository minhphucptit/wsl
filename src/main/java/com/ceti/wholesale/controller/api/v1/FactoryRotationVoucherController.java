package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateFactoryRotationVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateFactoryRotationVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.FactoryRotationVoucherDto;
import com.ceti.wholesale.service.FactoryRotationVoucherService;
import com.ceti.wholesale.service.GoodsInOutService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("/v1")
public class FactoryRotationVoucherController {
    @Autowired
    private FactoryRotationVoucherService factoryRotationVoucherService;

    @Autowired
    private GoodsInOutService goodsInOutService;

    // create factory rotation voucher
    @PostMapping(value = "/factory-rotation-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<FactoryRotationVoucherDto>> createFactoryRotationVoucher(
            @Valid @RequestBody CreateFactoryRotationVoucherRequest request,
            @RequestHeader(name = "department_id") String factory_id) {

        FactoryRotationVoucherDto factoryRotationVoucherDto = factoryRotationVoucherService.createFactoryRotationVoucher(request, factory_id);
        ResponseBodyDto<FactoryRotationVoucherDto> res = new ResponseBodyDto<>(factoryRotationVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // get list factory rotation voucher
    @GetMapping(value = "/factory-rotation-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<FactoryRotationVoucherDto>> getAllFactoryRotationVoucher(
            @RequestParam MultiValueMap<String, String> where,
            @RequestParam(value = "embed_goods_in_out", required = false) boolean embedGoodsInOut,
            @RequestParam(value = "rotation_show_quantity", required = false) String rotationShowQuantity,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
        where.add("factory_id", factoryId);
        Page<FactoryRotationVoucherDto> page = factoryRotationVoucherService.getAllByCondition(where, pageable);
        if (embedGoodsInOut) {
            if (!StringUtils.isEmpty(rotationShowQuantity)) {
                switch (rotationShowQuantity) {
                    case "in_quantity":
                        for (FactoryRotationVoucherDto factoryRotationVoucherDto : page) {
                            factoryRotationVoucherDto.setGoodsInOut(
                                    goodsInOutService.getGoodsInOutBySoldVoucherIdAndOutQuantityEmptyOrInQuantityEmpty(
                                            factoryRotationVoucherDto.getId(), BigDecimal.ZERO, false, factoryId));
                        }
                        break;
                    case "out_quantity":
                        for (FactoryRotationVoucherDto factoryRotationVoucherDto : page) {
                            factoryRotationVoucherDto.setGoodsInOut(
                                    goodsInOutService.getGoodsInOutBySoldVoucherIdAndOutQuantityEmptyOrInQuantityEmpty(
                                            factoryRotationVoucherDto.getId(), BigDecimal.ZERO, true, factoryId));
                        }
                        break;
                    case "all":
                        for (FactoryRotationVoucherDto factoryRotationVoucherDto : page) {
                            factoryRotationVoucherDto.setGoodsInOut(
                                    goodsInOutService
                                            .getGoodsInOutByVoucherId(factoryRotationVoucherDto.getId(), true));
                        }
                        break;
                    default:
                        break;
                }
            }

        }
        ResponseBodyDto<FactoryRotationVoucherDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // update factory rotation voucher
    @PatchMapping(value = "/factory-rotation-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<FactoryRotationVoucherDto>> updateFactoryRotationVoucher(
            @Valid @RequestBody UpdateFactoryRotationVoucherRequest request,
            @PathVariable(name = "id") String id) {

        FactoryRotationVoucherDto factoryRotationVoucherDto = factoryRotationVoucherService.updateFactoryRotationVoucher(id, request);
        ResponseBodyDto<FactoryRotationVoucherDto> res = new ResponseBodyDto<>(factoryRotationVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/factory-rotation-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteFactoryRotationVoucher(
            @PathVariable(name = "id") String id) {
        factoryRotationVoucherService.deleteFactoryRotationVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
