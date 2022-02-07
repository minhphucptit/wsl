package com.ceti.wholesale.controller.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.ceti.wholesale.controller.api.request.CreateCompanyRequest;
import com.ceti.wholesale.controller.api.request.UpdateCompanyRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.CompanyDto;
import com.ceti.wholesale.service.CompanyService;

@RestController
@RequestMapping("/v1")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    //Get list company
    @GetMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CompanyDto>> getAllCompanies(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "id", required = false) String id,
            @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "phone_number", required = false) String phoneNumber,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "is_active", required = false) Boolean isActive,
            Pageable pageable) {

        Page<CompanyDto> page = companyService.getAllByCondition(search, id, name, address, phoneNumber, isActive, pageable);

        ResponseBodyDto<CompanyDto> res = new ResponseBodyDto<>(pageable, page, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //Create new Company
    @PostMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CompanyDto>> createCompany(
            @RequestBody CreateCompanyRequest request,
            @RequestHeader(name = "department_id") String factoryId) {
        CompanyDto companyDto = companyService.createCompany(request, factoryId);
        ResponseBodyDto<CompanyDto> res = new ResponseBodyDto<>(companyDto,
                ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //update Company
    @PatchMapping(value = "/companies/{company_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CompanyDto>> updateCompany(
            @PathVariable(name = "company_id") String companyId,
            @RequestBody UpdateCompanyRequest request) {
        CompanyDto companyDto = companyService.updateCompany(companyId, request);
        ResponseBodyDto<CompanyDto> res = new ResponseBodyDto<>(companyDto, ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


}
