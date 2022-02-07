package com.ceti.wholesale.service;


import com.ceti.wholesale.controller.api.request.CreateCustomerReceiptPaymentVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerReceiptPaymentVoucherRequest;
import com.ceti.wholesale.dto.CustomerReceiptPaymentVoucherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerReceiptPaymentVoucherService {

    Page<CustomerReceiptPaymentVoucherDto> getAllByCondition(
            String customerId, String customerFullName, String payer,
            Boolean payerMethod, String category, Boolean type, String factory_id, Pageable pageable);

    CustomerReceiptPaymentVoucherDto createCustomerReceiptPaymentVoucher(
            CreateCustomerReceiptPaymentVoucherRequest receiptPaymentRequest, String factoryId);

    CustomerReceiptPaymentVoucherDto updateCustomerReceiptPaymentVoucher(String no,
                                                                         UpdateCustomerReceiptPaymentVoucherRequest updateCustomerReceiptPaymentVoucherRequest);

    void deleteCustomerReceiptPaymentVoucher(String id);

}
