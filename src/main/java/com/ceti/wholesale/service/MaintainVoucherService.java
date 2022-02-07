package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateMaintainVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateMaintainVoucherRequest;
import com.ceti.wholesale.dto.MaintainVoucherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface MaintainVoucherService {

//    Page<MaintainVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    MaintainVoucherDto createMaintainVoucher(
            CreateMaintainVoucherRequest createMaintainVoucherRequest, String factoryId);

    MaintainVoucherDto updateMaintainVoucher(String id, UpdateMaintainVoucherRequest request);

    void deleteMaintainVoucher(String id);

}
