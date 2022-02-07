package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateFactoryRotationVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateFactoryRotationVoucherRequest;
import com.ceti.wholesale.dto.FactoryRotationVoucherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface FactoryRotationVoucherService {
    FactoryRotationVoucherDto createFactoryRotationVoucher(
            CreateFactoryRotationVoucherRequest createFactoryRotationVoucherRequest, String factory_id);

    Page<FactoryRotationVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    FactoryRotationVoucherDto updateFactoryRotationVoucher(String id, UpdateFactoryRotationVoucherRequest request);

    void deleteFactoryRotationVoucher(String id);
}
