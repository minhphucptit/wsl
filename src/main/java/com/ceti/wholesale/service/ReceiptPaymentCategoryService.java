package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateReceiptPaymentCategoryRequest;
import com.ceti.wholesale.controller.api.request.UpdateReceiptPaymentCategoryRequest;
import com.ceti.wholesale.dto.ReceiptPaymentCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReceiptPaymentCategoryService {

    ReceiptPaymentCategoryDto createReceiptPaymentCategory(CreateReceiptPaymentCategoryRequest receiptPaymentCategoryRequest);

    ReceiptPaymentCategoryDto updateReceiptPaymentCategory(String id, UpdateReceiptPaymentCategoryRequest request);

    Page<ReceiptPaymentCategoryDto> getAllByCondition(String search, String id, Boolean type, Boolean isActive, Pageable pageable);
}
