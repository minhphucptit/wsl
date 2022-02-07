package com.ceti.wholesale.service.v2;

import com.ceti.wholesale.controller.api.request.v2.CreateTruckWeighingVoucherRequest;
import com.ceti.wholesale.controller.api.request.v2.UpdateTruckWeighingVoucherRequest;
import com.ceti.wholesale.dto.TruckWeighingVoucherDto;

public interface TruckWeighingVoucherServiceV2 {

    TruckWeighingVoucherDto createTruckWeighingVoucher(CreateTruckWeighingVoucherRequest createTruckWeighingVoucherRequest);

    TruckWeighingVoucherDto updateTruckWeighingVoucher(String commandReferenceId, UpdateTruckWeighingVoucherRequest updateTruckWeighingVoucherRequest);

    void deleteTruckWeighingVoucherByCommandReferenceId(String commandReferenceId);
}
