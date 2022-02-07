package com.ceti.wholesale.service;

import com.ceti.wholesale.controller.api.request.CreateInventoryVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateInventoryVoucherRequest;
import com.ceti.wholesale.dto.InventoryVoucherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryVoucherService {

    Page<InventoryVoucherDto> getAllByCondition(Long voucherAtFrom, Long voucherAtTo,String no, String factoryId, Pageable pageable);

    InventoryVoucherDto createInventoryVoucher(CreateInventoryVoucherRequest request, String factoryId, Boolean isCreated);

	InventoryVoucherDto updateInventoryVoucher(UpdateInventoryVoucherRequest request, String factoryId, String id, Boolean isCreated);

	void deleteInventoryVoucher(String id);

}
