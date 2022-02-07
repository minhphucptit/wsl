package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.AccountVoucherCodeDto;
import com.ceti.wholesale.service.AccountVoucherCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Validated
public class AccountVoucherCodeController {

    @Autowired
    private AccountVoucherCodeService accountVoucherCodeService;

    // get list
    @GetMapping(value = "/account-voucher-codes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<AccountVoucherCodeDto>> getAllAccountVoucherCode(
            @RequestParam MultiValueMap<String, String> where,
            Pageable pageable) {
        Page<AccountVoucherCodeDto> page =  accountVoucherCodeService.getAllWithFilter(where,pageable);
        ResponseBodyDto<AccountVoucherCodeDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

}
