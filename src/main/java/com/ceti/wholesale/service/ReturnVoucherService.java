package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateReturnVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateReturnVoucherRequest;
import com.ceti.wholesale.dto.ReturnVoucherDto;

public interface ReturnVoucherService {

    ReturnVoucherDto createReturnVoucher(CreateReturnVoucherRequest request, String factoryId);

    ReturnVoucherDto updateReturnVoucher(String id, UpdateReturnVoucherRequest request);

    void deleteReturnVoucher(String id);
}
