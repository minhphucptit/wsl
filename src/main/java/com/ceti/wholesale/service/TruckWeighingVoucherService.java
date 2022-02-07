package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateTruckWeighingVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckWeighingVoucherRequest;
import com.ceti.wholesale.dto.TruckWeighingVoucherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface TruckWeighingVoucherService {

    Page<TruckWeighingVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    TruckWeighingVoucherDto createTruckWeighingVoucher(CreateTruckWeighingVoucherRequest createTruckWeighingVoucherRequest, String factory_id);

    TruckWeighingVoucherDto updateTruckWeighingVoucher(String id, UpdateTruckWeighingVoucherRequest updateTruckWeighingVoucherRequest);

    void deleteTruckWeighingVoucher(String id);
}
