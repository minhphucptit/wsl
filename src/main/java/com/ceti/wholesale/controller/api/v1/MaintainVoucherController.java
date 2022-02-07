package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateMaintainVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateMaintainVoucherRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.MaintainVoucherDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.service.MaintainVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/v1")
@Validated
public class MaintainVoucherController {

    @Autowired
    private MaintainVoucherService maintainVoucherService;

    @Autowired
    private VoucherMapper voucherMapper;

    // get list maintain voucher
    @GetMapping(value = "/maintain-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<VoucherDto>> getAllMaintainVoucher(
            @RequestParam HashMap<String, String> where,
//            @RequestHeader(name = "department_id") String factoryId,
            Pageable pageable) {
//        where.add("factory_id", factoryId);
        String tableName = "maintain_voucher";
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, tableName);
        List<VoucherDto> list = voucherMapper.getList(where, pagingStr,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        long totalItems = voucherMapper.countList(where,tableName, 
        		DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_from")), DatetimeUtil.convertLongStringToInstant(where.get("voucher_at_to")));
        ResponseBodyDto<VoucherDto> res = new ResponseBodyDto<>(list,
                ResponseCodeEnum.R_200, "OK",totalItems);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // create maintain voucher
    @PostMapping(value = "/maintain-vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<MaintainVoucherDto>> createMaintainVoucher(
            @Valid @RequestBody CreateMaintainVoucherRequest request,
            @RequestHeader(name = "department_id") String factoryId) {

        MaintainVoucherDto maintainVoucherDto = maintainVoucherService
                .createMaintainVoucher(request, factoryId);
        ResponseBodyDto<MaintainVoucherDto> res = new ResponseBodyDto<>(maintainVoucherDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // update maintain voucher
    @PatchMapping(value = "/maintain-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<MaintainVoucherDto>> updateMaintainVoucher(
            @Valid @RequestBody UpdateMaintainVoucherRequest request,
            @PathVariable(name = "id") String id) {

        MaintainVoucherDto maintainVoucherDto = maintainVoucherService.updateMaintainVoucher(id, request);
        ResponseBodyDto<MaintainVoucherDto> res = new ResponseBodyDto<>(maintainVoucherDto,
                ResponseCodeEnum.R_200, "Ok");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @DeleteMapping(value = "/maintain-vouchers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> deleteMaintainVoucher(
            @PathVariable(name = "id") String id) {
        maintainVoucherService.deleteMaintainVoucher(id);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
