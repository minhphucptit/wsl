package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.ceti.wholesale.repository.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateTruckDistanceListRequest;
import com.ceti.wholesale.controller.api.request.CreateTruckDistanceRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckDistanceRequest;
import com.ceti.wholesale.dto.TruckDistanceDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.TruckDistance;
import com.ceti.wholesale.repository.TruckDistanceRepository;
import com.ceti.wholesale.service.TruckDistanceService;

@Service
public class TruckDistanceServiceImpl implements TruckDistanceService {

    @Autowired
	private TruckDistanceRepository truckDistanceRepository;

	@Autowired
	private TruckRepository truckRepository;

	@Override
	public List<TruckDistanceDto> createList(CreateTruckDistanceListRequest request, String factoryId) {
		List<String> error = new ArrayList<String>();

		List<CreateTruckDistanceListRequest.Create> truckDistanceList = request.getTruckDistanceList();
		List<TruckDistance> saveList = new ArrayList<TruckDistance>();

		String replaceLicensePlateNumber =null;

		//hàng chứa biển số xe
		String rowLicensePlateNumber =null;
		for(CreateTruckDistanceListRequest.Create item : truckDistanceList) {
			if (item.getTruckLicensePlateNumber() !=null && item.getDistance() == null && item.getDay() == null){
				rowLicensePlateNumber = item.getTruckLicensePlateNumber();
				replaceLicensePlateNumber = truckRepository.findAllByLicensePlateNumber(rowLicensePlateNumber,item.getTruckLicensePlateNumber().replaceAll("\\.","") );
				if (replaceLicensePlateNumber == null) {
					error.add(item.getRowLocation().toString());
				}
			}

			if(item.getDay() == null && item.getDistance() == null){
				continue;
			}

			if (replaceLicensePlateNumber != null && item.getTruckLicensePlateNumber() !=null &&
					item.getTruckLicensePlateNumber().equalsIgnoreCase(rowLicensePlateNumber)){
				item.setTruckLicensePlateNumber(replaceLicensePlateNumber);
			}else{
				continue;
			}

			TruckDistance truckDistance = CommonMapper.map(item, TruckDistance.class);
			saveList.add(truckDistance);

		}

		truckDistanceRepository.saveAll(saveList);

		if(error.size() != 0) {
			throw new BadRequestException("Lỗi ở dòng " + String.join(",", error));
		}

		return CommonMapper.toList(saveList, TruckDistanceDto.class);
	}

	@Override
	public TruckDistanceDto create(CreateTruckDistanceRequest request) {

		if(truckDistanceRepository.existsByTruckLicensePlateNumberAndDay(request.getTruckLicensePlateNumber(), request.getDay())) {
			throw new BadRequestException("Quyết toán dầu đã tồn tại");
		}

		TruckDistance truckDistance = new TruckDistance();
		CommonMapper.copyPropertiesIgnoreNull(request, truckDistance);

		truckDistanceRepository.save(truckDistance);

		return CommonMapper.map(truckDistance, TruckDistanceDto.class);
	}

	@Override
	public TruckDistanceDto update(UpdateTruckDistanceRequest request, String truckLicensePlateNumber, Instant day) {

		TruckDistance truckDistance = truckDistanceRepository.getByTruckLicensePlateNumberAndDay(truckLicensePlateNumber, day);

		if(truckDistance == null) {
			throw new NotFoundException("Quyết toán dầu không tồn tại");
		}

		CommonMapper.copyPropertiesIgnoreNull(request, truckDistance);

		truckDistanceRepository.save(truckDistance);

		return CommonMapper.map(truckDistance, TruckDistanceDto.class);
	}

	@Override
	public void delete(String truckLicensePlateNumber, Instant day) {

		if(!truckDistanceRepository.existsByTruckLicensePlateNumberAndDay(truckLicensePlateNumber, day)) {
			throw new NotFoundException("Quyết toán dầu không tồn tại");
		}

		truckDistanceRepository.deleteByTruckLicensePlateNumberAndDay(truckLicensePlateNumber, day);

	}

}
