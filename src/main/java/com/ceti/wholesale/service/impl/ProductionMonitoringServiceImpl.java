package com.ceti.wholesale.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateProductionMonitoringRequest;
import com.ceti.wholesale.controller.api.request.v2.UpdateProductionMonitoringRequest;
import com.ceti.wholesale.dto.ProductionMonitoringDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.ProductionMonitoring;
import com.ceti.wholesale.repository.CustomerRepository;
import com.ceti.wholesale.repository.ProductionMonitoringRepository;
import com.ceti.wholesale.service.ProductionMonitoringService;

@Service
public class ProductionMonitoringServiceImpl implements ProductionMonitoringService {

	@Autowired
	private ProductionMonitoringRepository productionMonitoringRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public List<ProductionMonitoringDto> create(CreateProductionMonitoringRequest request, String userId) {
		
		List<String> error = new ArrayList<String>();
		
		List<CreateProductionMonitoringRequest.Create> productionMonitorings = request.getProductionMonitorings();
		List<ProductionMonitoring> saveList = new ArrayList<ProductionMonitoring>();
		Instant time = Instant.now();
		
		for(CreateProductionMonitoringRequest.Create item : productionMonitorings) {
			if((!request.isCheckExists() && item.getCustomerId() == null) || item.getVoucherAt() == null || item.getQuantity() == null) {
				error.add(item.getRowLocation().toString());
				continue;
			}
			
			if(request.isCheckExists()) {
				String customerId = customerRepository.getFirstCustomerIdByCustomerCode(item.getCustomerCode());
				
				if(customerId == null) {
					error.add(item.getRowLocation().toString());
					continue;
				}else {
					item.setCustomerId(customerId);
				}
			}
			
			ProductionMonitoring productionMonitoring = new ProductionMonitoring();
			productionMonitoring = CommonMapper.map(item, ProductionMonitoring.class);

			productionMonitoring.setCreateBy(userId);
			productionMonitoring.setCreateAt(time);
			
			saveList.add(productionMonitoring);
			
		}
		
		productionMonitoringRepository.saveAll(saveList);
		
		if(error.size() != 0) {
			throw new BadRequestException("Lỗi ở dòng " + String.join(",", error));
		}
		
		return CommonMapper.toList(saveList, ProductionMonitoringDto.class);
	}

	@Override
	public ProductionMonitoringDto update(UpdateProductionMonitoringRequest request, String userId) {
		
		ProductionMonitoring productionMonitoring = productionMonitoringRepository.getByCustomerIdAndVoucherAt(request.getCustomerId(), request.getVoucherAt());
		
		if(productionMonitoring == null) {
			throw new NotFoundException("Kết quả sản lượng không tồn tại");
		}
		
		productionMonitoring.setQuantity(request.getQuantity());
		productionMonitoring.setCreateAt(Instant.now());
		productionMonitoring.setCreateBy(userId);
		
		productionMonitoringRepository.save(productionMonitoring);
		
		return CommonMapper.map(productionMonitoring, ProductionMonitoringDto.class);
	}


	@Override
	public void delete(String customerId, Instant voucherAt) {
		if (!productionMonitoringRepository.existsByCustomerIdAndVoucherAt(customerId, voucherAt)) {
			throw new NotFoundException("Sản lượng không tồn tại");
		}
		productionMonitoringRepository.deleteByCustomerIdAndVoucherAt(customerId,voucherAt);
	}

}
