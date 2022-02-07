package com.ceti.wholesale.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ceti.wholesale.controller.api.request.CreateOtherDiscountRequest;
import com.ceti.wholesale.model.OtherDiscount;
import com.ceti.wholesale.repository.OtherDiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateAllTargetProductionRequest;
import com.ceti.wholesale.controller.api.request.CreateTargetProductionRequest;
import com.ceti.wholesale.controller.api.request.DeleteTargetProductionRequest;
import com.ceti.wholesale.controller.api.request.UpdateTargetProductionRequest;
import com.ceti.wholesale.dto.TargetProductionDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.TargetProduction;
import com.ceti.wholesale.model.TargetProductionId;
import com.ceti.wholesale.repository.CustomerRepository;
import com.ceti.wholesale.repository.TargetProductionRepository;
import com.ceti.wholesale.service.TargetProductionService;

@Service
public class TargetProductionServiceImpl implements TargetProductionService{

	@Autowired
	TargetProductionRepository targetProductionRepository;
	
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	OtherDiscountRepository otherDiscountRepository;
	
	@Override
	public TargetProductionDto createTargetProduction(CreateTargetProductionRequest request,String userId) {
		if(targetProductionRepository.existsByCustomerCodeAndMonthAndYear(request.getCustomerCode(), request.getMonth(), request.getYear())) {
			throw new NotFoundException("Khách hàng " + request.getCustomerCode() + " ở tháng " + request.getMonth() + " năm " + request.getYear() +
					" này đã tồn tại");
		}
		// TODO Auto-generated method stub
		TargetProduction targetProduction = new TargetProduction();
		
		CommonMapper.copyPropertiesIgnoreNull(request, targetProduction);
		
		targetProduction.setCreateBy(userId);
		targetProduction.setCreateAt(Instant.now());
		
		targetProductionRepository.save(targetProduction);
		if(request.getOtherDiscounts()!=null){
			for(CreateOtherDiscountRequest createOtherDiscountRequest : request.getOtherDiscounts()){
				OtherDiscount otherDiscount = new OtherDiscount();
				CommonMapper.copyPropertiesIgnoreNull(createOtherDiscountRequest,otherDiscount);
				otherDiscount.setCustomerCode(request.getCustomerCode());
				otherDiscount.setMonth(request.getMonth());
				otherDiscount.setYear(request.getYear());
				otherDiscountRepository.save(otherDiscount);
			}
		}
		
		return CommonMapper.map(targetProduction, TargetProductionDto.class);
	}

	@Override
	public TargetProductionDto updateTargetProduction(UpdateTargetProductionRequest request, String userId) {
		// TODO Auto-generated method stub
		
		Optional<TargetProduction> optional = Optional.ofNullable(targetProductionRepository
				.findByCustomerCodeAndMonthAndYear(request.getCustomerCode(),request.getMonth(), request.getYear()));
		
		if(!optional.isPresent()) {
			throw new NotFoundException("Chỉ tiêu của khách " + request.getCustomerCode() + " ở tháng " + request.getMonth() + " năm " + request.getYear() +
					" này không tồn tại");
		}
		
		TargetProduction targetProduction = optional.get();
		
		CommonMapper.copyPropertiesIgnoreNull(request, targetProduction);
		
		targetProduction.setCreateBy(userId);
		targetProduction.setCreateAt(Instant.now());
		
		targetProductionRepository.save(targetProduction);
		otherDiscountRepository.deleteAllByCustomerCodeAndMonthAndYear(request.getCustomerCode(), request.getMonth(), request.getYear());
		if(request.getOtherDiscounts()!=null){
			for(CreateOtherDiscountRequest createOtherDiscountRequest : request.getOtherDiscounts()){
				OtherDiscount otherDiscount = new OtherDiscount();
				CommonMapper.copyPropertiesIgnoreNull(createOtherDiscountRequest,otherDiscount);
				otherDiscount.setCustomerCode(request.getCustomerCode());
				otherDiscount.setMonth(request.getMonth());
				otherDiscount.setYear(request.getYear());
				otherDiscountRepository.save(otherDiscount);
			}
		}
		
		return CommonMapper.map(targetProduction, TargetProductionDto.class);
	}

	@Override
	public void deleteTargetProduction(DeleteTargetProductionRequest request) {
		
		if(!targetProductionRepository.existsByCustomerCodeAndMonthAndYear(request.getCustomerCode(), request.getMonth(), request.getYear())) {
			throw new NotFoundException("Khách hàng " + request.getCustomerCode() + " ở tháng " + request.getMonth() + " năm " + request.getYear() +
					" không tồn tại");
		}
		
		TargetProductionId targetProductionId = CommonMapper.map(request, TargetProductionId.class);
		targetProductionRepository.deleteById(targetProductionId);
		otherDiscountRepository.deleteAllByCustomerCodeAndMonthAndYear(request.getCustomerCode(), request.getMonth(), request.getYear());
	}
	
	@Override
	public List<TargetProductionDto> createAll(CreateAllTargetProductionRequest request, String userId) {
		List<String> error = new ArrayList<>();
		
		List<CreateTargetProductionRequest> targetProductions = request.getTargetProductions();
		List<TargetProduction> saveList = new ArrayList<>();
		Instant time = Instant.now();
		
		for(CreateTargetProductionRequest item : targetProductions) {
			if(item.getCustomerCode() == null || item.getMonth() == null || item.getYear() == null || item.getQuantity() == null) {
				error.add(item.getRowLocation().toString());
				continue;
			}
			
			
			if(!customerRepository.existsByCode(item.getCustomerCode())) {
				error.add(item.getRowLocation().toString());
				continue;
			}else{
				String customerName = customerRepository.findByName(item.getCustomerCode());
				item.setCustomerName(customerName);
			}
			
			TargetProduction targetProduction = CommonMapper.map(item, TargetProduction.class);

			targetProduction.setCreateBy(userId);
			targetProduction.setCreateAt(time);
			
			saveList.add(targetProduction);
			
		}
		
		targetProductionRepository.saveAll(saveList);
		
		if(error.size() != 0) {
			throw new BadRequestException("Lỗi ở dòng " + String.join(",", error));
		}
		
		return CommonMapper.toList(saveList, TargetProductionDto.class);
	}
}
