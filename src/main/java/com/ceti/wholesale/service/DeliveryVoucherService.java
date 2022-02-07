package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateDeliveryVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateDeliveryVoucherRequest;
import com.ceti.wholesale.dto.DeliveryVoucherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface DeliveryVoucherService {

//    Page<DeliveryVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    DeliveryVoucherDto createDeliveryVoucher(CreateDeliveryVoucherRequest createDeliveryVoucherRequest, String factoryId);

    DeliveryVoucherDto updateDeliveryVoucher(String id, UpdateDeliveryVoucherRequest request);

    void deleteDeliveryVoucher(String id);
}
