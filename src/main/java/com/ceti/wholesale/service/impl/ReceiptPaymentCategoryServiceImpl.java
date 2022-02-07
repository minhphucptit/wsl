package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateReceiptPaymentCategoryRequest;
import com.ceti.wholesale.controller.api.request.UpdateReceiptPaymentCategoryRequest;
import com.ceti.wholesale.dto.ReceiptPaymentCategoryDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.ReceiptPaymentCategory;
import com.ceti.wholesale.repository.ReceiptPaymentCategoryRepository;
import com.ceti.wholesale.service.ReceiptPaymentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReceiptPaymentCategoryServiceImpl implements ReceiptPaymentCategoryService {

    @Autowired
    private ReceiptPaymentCategoryRepository receiptPaymentCategoryRepository;

    //get all receipt payment categories
    @Override
    public Page<ReceiptPaymentCategoryDto> getAllByCondition(String search,String id, Boolean type, Boolean isActive, Pageable pageable) {
        Page<ReceiptPaymentCategory> page = receiptPaymentCategoryRepository.getAllByCondition(search, id, type, isActive, pageable);
        return CommonMapper.toPage(page, ReceiptPaymentCategoryDto.class, pageable);
    }

    //add new receipt payment category
    @Override
    public ReceiptPaymentCategoryDto createReceiptPaymentCategory(CreateReceiptPaymentCategoryRequest receiptPaymentCategoryRequest) {
        if (receiptPaymentCategoryRepository.existsById(receiptPaymentCategoryRequest.getId())) {
            throw new BadRequestException("Mã loại thu chi đã tồn tại");
        }
        ReceiptPaymentCategory receiptPaymentCategory = CommonMapper.map(receiptPaymentCategoryRequest, ReceiptPaymentCategory.class);
        receiptPaymentCategoryRepository.save(receiptPaymentCategory);
        return CommonMapper.map(receiptPaymentCategory, ReceiptPaymentCategoryDto.class);
    }

    //update receipt payment category
    @Override
    public ReceiptPaymentCategoryDto updateReceiptPaymentCategory(String id, UpdateReceiptPaymentCategoryRequest request) {
        Optional<ReceiptPaymentCategory> optional = receiptPaymentCategoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Danh mục không tồn tại");
        }
        ReceiptPaymentCategory receiptPaymentCategory = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(request, receiptPaymentCategory);
        receiptPaymentCategoryRepository.save(receiptPaymentCategory);
        return CommonMapper.map(receiptPaymentCategory, ReceiptPaymentCategoryDto.class);
    }
}
