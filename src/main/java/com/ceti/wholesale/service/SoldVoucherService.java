package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateSoldVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateSoldVoucherRequest;
import com.ceti.wholesale.dto.SoldVoucherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface SoldVoucherService {

//    Page<SoldVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    SoldVoucherDto createSoldVoucher(CreateSoldVoucherRequest createSoldVoucherRequest, String factory_id);

    SoldVoucherDto updateSoldVoucher(String id, UpdateSoldVoucherRequest request);

    void deleteSoldVoucher(String id);
}
