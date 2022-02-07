package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateCompanyRequest;
import com.ceti.wholesale.controller.api.request.UpdateCompanyRequest;
import com.ceti.wholesale.dto.CompanyDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.Company;
import com.ceti.wholesale.repository.CompanyRepository;
import com.ceti.wholesale.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Page<CompanyDto> getAllByCondition(String search, String id, String name, String address,
                                              String phoneNumber, Boolean isActive, Pageable pageable) {
        Page<Company> page = companyRepository
                .getAllByCondition(search, id, name, address, phoneNumber, isActive, pageable);
        return CommonMapper.toPage(page, CompanyDto.class, pageable);
    }

    @Override
    public CompanyDto createCompany(CreateCompanyRequest request, String factoryId) {
        if (companyRepository.existsById(request.getId())) {
            throw new BadRequestException("Mã công ty đã tồn tại");
        }
        Company company = new Company();
        CommonMapper.copyPropertiesIgnoreNull(request, company);
        company = companyRepository.save(company);
        return CommonMapper.map(company, CompanyDto.class);
    }

    // Update Company
    @Override
    public CompanyDto updateCompany(String id, UpdateCompanyRequest request) {
        Optional<Company> optional = companyRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Mã công ty không tồn tại");
        }
        Company company = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(request, company);
        company = companyRepository.save(company);
        return CommonMapper.map(company, CompanyDto.class);

    }
}
