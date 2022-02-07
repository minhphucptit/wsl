package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateSoldDeliveryVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateSoldDeliveryVoucherRequest;
import com.ceti.wholesale.dto.SoldDeliveryVoucherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface SoldDeliveryVoucherService {

//    Page<SoldDeliveryVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    SoldDeliveryVoucherDto createSoldDeliveryVoucher(CreateSoldDeliveryVoucherRequest request, String factory_id);

    SoldDeliveryVoucherDto updateSoldDeliveryVoucher(String id, UpdateSoldDeliveryVoucherRequest request);

    void deleteSoldDeliveryVoucher(String id);
}
