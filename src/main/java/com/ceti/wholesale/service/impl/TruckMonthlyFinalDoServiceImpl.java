package com.ceti.wholesale.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateTruckMonthlyFinalDoRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckMonthlyFinalDoRequest;
import com.ceti.wholesale.dto.PSDifferenceTrackingDto;
import com.ceti.wholesale.dto.TruckMonthlyFinalDoDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.mapper.PSDifferenceTrackingMapper;
import com.ceti.wholesale.model.TruckMonthlyFinalDo;
import com.ceti.wholesale.repository.TruckMonthlyFinalDoDetailRepository;
import com.ceti.wholesale.repository.TruckMonthlyFinalDoRepository;
import com.ceti.wholesale.service.TruckMonthlyFinalDoService;


@Service
@Transactional
public class TruckMonthlyFinalDoServiceImpl implements TruckMonthlyFinalDoService {
	
	@Autowired
	private TruckMonthlyFinalDoRepository truckMonthlyFinalDoRepository;
	
	@Autowired
	private TruckMonthlyFinalDoDetailRepository truckMonthlỳinalDoDetailRepository;
	
	@Autowired
	private PSDifferenceTrackingMapper psDifferenceTrackingMapper;

	@Override
	public TruckMonthlyFinalDoDto create(CreateTruckMonthlyFinalDoRequest request) {
		TruckMonthlyFinalDo truckMonthlyFinalDo = truckMonthlyFinalDoRepository.getByMonthAndYearAndTruckLicensePlateNumberAndFactoryId(
				request.getMonth(), request.getYear(), request.getTruckLicensePlateNumber(), request.getFactoryId());
		if(truckMonthlyFinalDo != null) {
			throw new BadRequestException("Tổng chênh lệnh gas của xe đã tồn tại");
		}else {
			truckMonthlyFinalDo = new TruckMonthlyFinalDo();
		}
		CommonMapper.copyPropertiesIgnoreNull(request, truckMonthlyFinalDo);
		truckMonthlyFinalDoRepository.save(truckMonthlyFinalDo);
		return CommonMapper.map(truckMonthlyFinalDo, TruckMonthlyFinalDoDto.class);
	}

	@Override
	public TruckMonthlyFinalDoDto update(UpdateTruckMonthlyFinalDoRequest request, String id) {
		Optional<TruckMonthlyFinalDo> option = truckMonthlyFinalDoRepository.findById(id);
		if(!option.isPresent()) {
			throw new NotFoundException("Tổng chênh lệnh gas của xe không tồn tại");
		}
		
		TruckMonthlyFinalDo truckMonthlyFinalDo = option.get();
		
		CommonMapper.copyPropertiesIgnoreNull(request, truckMonthlyFinalDo);
		truckMonthlyFinalDoRepository.save(truckMonthlyFinalDo);
		
		return CommonMapper.map(truckMonthlyFinalDo, TruckMonthlyFinalDoDto.class);
	}

	@Override
	public void delete(String id) {
		if(!truckMonthlyFinalDoRepository.existsById(id)) {
			throw new NotFoundException("Tổng chênh lệnh gas của xe không tồn tại");
		}
		
		truckMonthlyFinalDoRepository.deleteById(id);
		
	}

	@Override
	public Page<TruckMonthlyFinalDoDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
        ResultPage<TruckMonthlyFinalDo> rs = truckMonthlỳinalDoDetailRepository.findAllWithFilter(pageable,where);
        long total = rs.getTotalItems();
        List<TruckMonthlyFinalDo> result = rs.getPageList();
        return new PageImpl<TruckMonthlyFinalDoDto>(CommonMapper.toList(result,TruckMonthlyFinalDoDto.class),pageable,total);
	}

	@Override
	public List<TruckMonthlyFinalDoDto> createAll(String factoryId, Integer month, Integer year, String truckLicencePlateNumber, Boolean hasFinalDo) {
		Pageable pageable = PageRequest.of(0,Integer.MAX_VALUE);
		
    	List<List<Object>> result = psDifferenceTrackingMapper.getAll(factoryId, month, year,truckLicencePlateNumber,hasFinalDo, pageable.getOffset(), pageable.getPageSize());

    	List<PSDifferenceTrackingDto> psDifferenceTrackingDtos = (List<PSDifferenceTrackingDto>) (Object)result.get(0);
    	
    	List<TruckMonthlyFinalDo> truckMonthlyFinalDos = new ArrayList<TruckMonthlyFinalDo>();
    	
    	for(PSDifferenceTrackingDto item : psDifferenceTrackingDtos) {
    		TruckMonthlyFinalDo truckMonthlyFinalDo = truckMonthlyFinalDoRepository.getByMonthAndYearAndTruckLicensePlateNumberAndFactoryId(month, year, item.getTruckLicensePlateNumber(), item.getFactoryId());
    		
    		if(truckMonthlyFinalDo == null) {
    			truckMonthlyFinalDo = new TruckMonthlyFinalDo();
    		}
    		
    		truckMonthlyFinalDo.setMonth(month);
    		truckMonthlyFinalDo.setYear(year);
    		truckMonthlyFinalDo.setFactoryId(item.getFactoryId());
    		truckMonthlyFinalDo.setFinalDo(item.getTotalDifference());
    		truckMonthlyFinalDo.setTruckLicensePlateNumber(item.getTruckLicensePlateNumber());
    		truckMonthlyFinalDos.add(truckMonthlyFinalDo);
    	}
    	
    	truckMonthlyFinalDoRepository.saveAll(truckMonthlyFinalDos);
    	
		return CommonMapper.toList(truckMonthlyFinalDos, TruckMonthlyFinalDoDto.class);
	}

}
