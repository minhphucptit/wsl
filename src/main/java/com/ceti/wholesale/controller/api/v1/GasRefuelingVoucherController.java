package com.ceti.wholesale.controller.api.v1;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateGasRefuelingVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.GasRefuelingVoucherDto;
import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.GoodInOutMapper;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.repository.GasRefuelingVoucherRepository;
import com.ceti.wholesale.service.GasRefuelingVoucherService;

@RestController
@RequestMapping("/v1")
public class GasRefuelingVoucherController {

    @Autowired
    private VoucherMapper voucherMapper;

    @Autowired
    private GasRefuelingVoucherService gasRefuelingVoucherService;
   

    @Autowired
    private GasRefuelingVoucherRepository gasRefuelingVoucherRepository;
    
    @Autowired
    private GoodInOutMapper goodsInOutMapper;

    // get list gas refueling voucher
    @GetMapping(value = "/gas-refueling-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<VoucherDto>> getAll(
            @RequestParam HashMap<String, String> where,
            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
    	
        where.put("factory_id", factoryId);
        String tableName = "gas_refueling_voucher";
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, tableName);
        List<VoucherDto> list = voucherMapper.getList(where, pagingStr,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        long totalItems = voucherMapper.countList(where,tableName,
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        ResponseBodyDto<VoucherDto> res = new ResponseBodyDto<>(list,
                ResponseCodeEnum.R_200, "OK",totalItems);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    
    // suggess create gas refueling voucher and goods in out 
    @GetMapping(value = "/gas-refueling-vouchers/suggestion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GoodsInOutDto>> getSuggesstion(
            @RequestParam(name = "voucher_at") Long voucherAt,
            @RequestHeader(name = "department_id") String factoryId) {
        List<GoodsInOutDto> goodsInOutDtos = goodsInOutMapper.getListSuggesion(Instant.ofEpochSecond(voucherAt), factoryId, null, false);
        
        ResponseBodyDto<GoodsInOutDto> res = new ResponseBodyDto<>(goodsInOutDtos,
                ResponseCodeEnum.R_200, "OK", goodsInOutDtos.size());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //update voucher
    @PatchMapping(value = "/gas-refueling-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GasRefuelingVoucherDto>> updateVoucher(
            @RequestBody CreateGasRefuelingVoucherRequest request, 
            @PathVariable(name = "id") String id,
            @RequestHeader(name = "department_id") String factoryId){
        GasRefuelingVoucherDto gasRefuelingVoucherDto = gasRefuelingVoucherService.updateVoucher(id,request, factoryId);
        ResponseBodyDto<GasRefuelingVoucherDto> res = new ResponseBodyDto<>(gasRefuelingVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    //delete voucher
    @DeleteMapping(value = "/gas-refueling-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteVoucher(@PathVariable(name = "id") String id){
        gasRefuelingVoucherService.deleteVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    // create voucher
    @PostMapping(value = "/gas-refueling-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GasRefuelingVoucherDto>> createVoucher(
            @Valid @RequestBody CreateGasRefuelingVoucherRequest request,
            @RequestHeader(name = "department_id") String factoryId) {

        GasRefuelingVoucherDto gasRefuelingVoucherDto = gasRefuelingVoucherService.createVoucher(request, factoryId);
        ResponseBodyDto<GasRefuelingVoucherDto> res = new ResponseBodyDto<>(gasRefuelingVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
    
    //api recalculator
    @GetMapping(value = "/gas-refueling-vouchers/{id}/recalculation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<GoodsInOutDto>> recalculator(
    		@PathVariable(name = "id") String id,
            @RequestParam(name = "voucher_at") Long voucherAt,
            @RequestHeader(name = "department_id") String factoryId) {

         gasRefuelingVoucherService.recalculation(Instant.ofEpochSecond(voucherAt), factoryId, id);
        ResponseBodyDto<GoodsInOutDto> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
