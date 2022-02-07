package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateFactoryImportVoucherRequest;
import com.ceti.wholesale.controller.api.request.CreateFactoryImportExportVoucherRequestFromWarehouse;
import com.ceti.wholesale.controller.api.request.UpdateFactoryImportVoucherRequest;
import com.ceti.wholesale.dto.FactoryImportVoucherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface FactoryImportVoucherService {

//    Page<FactoryImportVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    FactoryImportVoucherDto createFactoryImportVoucher(
            CreateFactoryImportVoucherRequest createFactoryImportVoucherRequest, String factory_id);

    FactoryImportVoucherDto updateFactoryImportVoucher(String id, UpdateFactoryImportVoucherRequest request);

    void deleteFactoryImportVoucher(String id);

    FactoryImportVoucherDto createFactoryImportVoucherFromWarehouse(CreateFactoryImportExportVoucherRequestFromWarehouse request, String factoryId, String userId);
}
