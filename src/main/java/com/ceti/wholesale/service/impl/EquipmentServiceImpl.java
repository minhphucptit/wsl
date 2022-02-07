package com.ceti.wholesale.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateEquipmentAccessorieRequest;
import com.ceti.wholesale.controller.api.request.CreateEquipmentRequest;
import com.ceti.wholesale.dto.EquipmentAccessorieDto;
import com.ceti.wholesale.dto.EquipmentDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.mapper.EquipmentMapper;
import com.ceti.wholesale.model.Equipment;
import com.ceti.wholesale.model.EquipmentAccessorie;
import com.ceti.wholesale.repository.EquipmentAccessorieRepository;
import com.ceti.wholesale.repository.EquipmentRepository;
import com.ceti.wholesale.service.EquipmentService;

@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService {
	
	@Autowired
	EquipmentRepository equipmentRepository;
	
	@Autowired
	EquipmentAccessorieRepository equipmentAccessorieRepository;
	
	@Autowired
	EquipmentMapper equipmentMapper;

	@Override
	public EquipmentDto createEquipment(CreateEquipmentRequest request,String factoryId) {
		Equipment equipment = new Equipment();
		
		CommonMapper.copyPropertiesIgnoreNull(request, equipment);
		
		equipment.setFactoryId(factoryId);
		
		equipmentRepository.save(equipment);
		
		EquipmentDto equipmentDto = CommonMapper.map(equipment, EquipmentDto.class);
		
		if(!request.getEquipmentAccessories().isEmpty()) {
			List<CreateEquipmentAccessorieRequest> accessories = request.getEquipmentAccessories();
			
			for(CreateEquipmentAccessorieRequest accessorie : accessories) {
				EquipmentAccessorie equipmentAccessorie = new EquipmentAccessorie();
				
				CommonMapper.copyPropertiesIgnoreNull(accessorie, equipmentAccessorie);
				
				equipmentAccessorie.setEquipmentId(equipment.getId());
				equipmentAccessorie.setFactoryId(factoryId);
				
				equipmentAccessorieRepository.save(equipmentAccessorie);
				
//				EquipmentAccessorieDto equipmentAccessorieDto = CommonMapper.map(equipmentAccessorie, EquipmentAccessorieDto.class);
//				
//				equipmentDto.getEquipmentAccessories().add(equipmentAccessorieDto);
			}
		}
		
		return equipmentDto;
	}

	@Override
	public EquipmentDto updateEquipment(String equipmentId,CreateEquipmentRequest request) {
		 Optional<Equipment> optional = equipmentRepository.findById(equipmentId);
	        if (optional.isEmpty()) {
	            throw new NotFoundException("Thiết bị, máy móc không tồn tại");
	        }else {
	        	Equipment equipment = optional.get();
	        	
	        	CommonMapper.copyPropertiesIgnoreNull(request, equipment);
	        	
	        	equipmentRepository.save(equipment);	        	
	        	
	        	EquipmentDto equipmentDto = CommonMapper.map(equipment, EquipmentDto.class);
	        	
	        	List<CreateEquipmentAccessorieRequest> accessories = request.getEquipmentAccessories();
	        	
	        	if(accessories!=null) {
	        		equipmentAccessorieRepository.deleteByEquipmentId(equipmentId);
	        		for(CreateEquipmentAccessorieRequest accessorie : accessories) {
	        			EquipmentAccessorie equipmentAccessorie = new EquipmentAccessorie();
	        			
	        			CommonMapper.copyPropertiesIgnoreNull(accessorie, equipmentAccessorie);
	        			
	        			equipmentAccessorie.setEquipmentId(equipment.getId());
	    				equipmentAccessorie.setFactoryId(equipment.getFactoryId());
	        			
	        			equipmentAccessorieRepository.save(equipmentAccessorie);
	        			
//	        			EquipmentAccessorieDto equipmentAccessorieDto = CommonMapper.map(equipmentAccessorie, EquipmentAccessorieDto.class);
//	    				
//	    				equipmentDto.getEquipmentAccessories().add(equipmentAccessorieDto);
	        		}
	        	}
	        	
	        	return equipmentDto;
	        }
	}

	@Override
	public List<EquipmentDto> getAll(Instant signDateFrom, Instant signDateTo, String name, String origin, String brand,
			String symbol, String model, String manufactureYear, String factoryId, String isActive, String pagingStr) {
		List<EquipmentDto> list = equipmentMapper.getList(signDateFrom,signDateTo,name,origin,  brand,symbol,  model,  
				manufactureYear,factoryId,isActive,pagingStr);
		
		for(EquipmentDto equipment:list) {
			List<EquipmentAccessorie> accessories = equipmentAccessorieRepository.findAllByEquipmentId(equipment.getId());
			
			List<EquipmentAccessorieDto> accessorieDtos = new ArrayList<EquipmentAccessorieDto>();
			
			accessories.stream().forEach(value -> accessorieDtos.add(CommonMapper.map(value, EquipmentAccessorieDto.class)));
			
			equipment.setEquipmentAccessories(accessorieDtos);
		}
		return list;
	}

}
