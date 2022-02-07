package com.ceti.wholesale.service;

import java.time.Instant;
import java.util.List;

import com.ceti.wholesale.controller.api.request.CreateGasRefuelingVoucherRequest;
import com.ceti.wholesale.dto.GasRefuelingVoucherDto;
import com.ceti.wholesale.dto.GoodsInOutDto;

public interface GasRefuelingVoucherService {

    GasRefuelingVoucherDto createVoucher(CreateGasRefuelingVoucherRequest request, String factoryId);

    GasRefuelingVoucherDto updateVoucher(String id, CreateGasRefuelingVoucherRequest request, String factoryId );

    void deleteVoucher(String id);
    
    List<GoodsInOutDto> suggestionCreateVoucher(Instant voucherAt, String factoryId);
    
    void recalculation(Instant voucherAt, String factoryId, String gasRefuelingVoucherId);

}
