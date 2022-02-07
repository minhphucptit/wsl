package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateRecallVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateRecallVoucherRequest;
import com.ceti.wholesale.dto.RecallVoucherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface RecallVoucherService {

//    Page<RecallVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable);

    RecallVoucherDto createRecallVoucher(CreateRecallVoucherRequest request, String factoryId);

    RecallVoucherDto updateRecallVoucher(String id, UpdateRecallVoucherRequest request);

    void deleteRecallVoucher(String id);
}
