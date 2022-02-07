package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateFactoryExportVoucherRequest;
import com.ceti.wholesale.controller.api.request.CreateFactoryImportExportVoucherRequestFromWarehouse;
import com.ceti.wholesale.controller.api.request.UpdateFactoryExportVoucherRequest;
import com.ceti.wholesale.dto.FactoryExportVoucherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface FactoryExportVoucherService {

//    Page<FactoryExportVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    FactoryExportVoucherDto createFactoryExportVoucher(
            CreateFactoryExportVoucherRequest createFactoryExportVoucherRequest, String factory_id);

    FactoryExportVoucherDto updateFactoryExportVoucher(String id, UpdateFactoryExportVoucherRequest request);

    void deleteFactoryExportVoucher(String id);

    FactoryExportVoucherDto createFactoryExportVoucherFromWarehouse(CreateFactoryImportExportVoucherRequestFromWarehouse request, String factoryId, String userId);

}
