package com.ceti.wholesale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.controller.api.request.CreateExportImportVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateExportImportVoucherRequest;
import com.ceti.wholesale.dto.ExportImportVoucherDto;

public interface ExportImportVoucherService {

    Page<ExportImportVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable, String factoryId);

    ExportImportVoucherDto createImportExportVoucher(
            CreateExportImportVoucherRequest createImportVoucherRequest, String factoryId);

    ExportImportVoucherDto updateImportExportVoucher(String factoryId,
                                                     UpdateExportImportVoucherRequest updateExportImportVoucherRequest
    );
    
	ExportImportVoucherDto getDetail(String importVoucherId, String exportVoucherId);

    void deleteExportImportVoucher(String importVoucherId, String exportVoucherId);

}
