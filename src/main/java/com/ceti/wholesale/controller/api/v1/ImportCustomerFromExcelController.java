package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.service.v2.ImportCustomerFromExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/v1")
public class ImportCustomerFromExcelController {

    @Autowired
    private ImportCustomerFromExcelService importCustomerFromExcelService;
    // create customer from excel file
    @PostMapping(value = "/import-customer-from-excel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> createInventoryVoucher(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(name = "department_id") String factoryId) {
        String message = "";
        try {
             message = importCustomerFromExcelService.importCustomerFromExcel(file, factoryId);
        }catch (IOException e){
            message = e.getMessage();
        }

        ResponseBodyDto<String> res = new ResponseBodyDto<>(message,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
