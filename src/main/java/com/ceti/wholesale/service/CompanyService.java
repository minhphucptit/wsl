package com.ceti.wholesale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ceti.wholesale.controller.api.request.CreateCompanyRequest;
import com.ceti.wholesale.controller.api.request.UpdateCompanyRequest;
import com.ceti.wholesale.dto.CompanyDto;

public interface CompanyService {

    CompanyDto createCompany(CreateCompanyRequest request, String factoryId);

    CompanyDto updateCompany(String companyId, UpdateCompanyRequest request);

    Page<CompanyDto> getAllByCondition(String search, String id, String name, String address, String phoneNumber,
                                       Boolean isActive, Pageable pageable);

}
