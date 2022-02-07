package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreatePaymentVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdatePaymentVoucherRequest;
import com.ceti.wholesale.dto.PaymentVoucherDto;

public interface PaymentVoucherService {

//    Page<PaymentVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    PaymentVoucherDto createPaymentVoucher(CreatePaymentVoucherRequest request, String factoryId);

    PaymentVoucherDto updatePaymentVoucher(String id, UpdatePaymentVoucherRequest request);

    void deletePaymentVoucher(String id);

    PaymentVoucherDto getDetailPaymentVoucher(String id);
}
