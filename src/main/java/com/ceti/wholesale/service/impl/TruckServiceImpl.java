package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateTruckRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckRequest;
import com.ceti.wholesale.dto.FactoryDto;
import com.ceti.wholesale.dto.TruckDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.Truck;
import com.ceti.wholesale.repository.TruckDetailRepository;
import com.ceti.wholesale.repository.TruckRepository;
import com.ceti.wholesale.service.TruckService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TruckServiceImpl implements TruckService {
    @Autowired
    private TruckRepository truckRepository;

    @Autowired
    private TruckDetailRepository truckDetailRepository;

//    @Override
//    public Page<TruckDto> getAllByCondition(String licensePlateNumber, String factoryId, Boolean isActive, Pageable pageable) {
//        Page<Truck> page = truckRepository.getAllByCondition(licensePlateNumber, factoryId, isActive, pageable);
//        return CommonMapper.toPage(page, TruckDto.class, pageable);
//    }
//    // lấy danh sách tất cả các xe ở tất cả các kho
//    @Override
//    public Page<TruckDto> getAllByConditionWithEmbed(MultiValueMap<String, String> where, Pageable pageable) {
//        ResultPage<Object[]> page=truckDetailRepository.findAllWithEmbed(pageable,where);
//        List<TruckDto> truckList = new ArrayList<>();
//        for(Object[] objects:page.getPageList()){
//            Truck truck = (Truck) objects[0];
//            TruckDto truckDto = CommonMapper.map(truck,TruckDto.class);
//            if(objects[1]!=null){
//                Factory factory = (Factory) objects[1];
//                FactoryDto factoryDto = CommonMapper.map(factory,FactoryDto.class);
//                truckDto.setFactory(factoryDto);
//            }
//            truckList.add(truckDto);
//        }
//        return new PageImpl<>(truckList, pageable, page.getTotalItems());
//    }

    // create new Truck
    @Override
    public TruckDto createTruck(CreateTruckRequest request, String factory_id) {
        if (truckRepository.existsById(request.getLicensePlateNumber())) {
            throw new BadRequestException("Biển số xe đã tồn tại");
        }
        Truck truck = new Truck();
        CommonMapper.copyPropertiesIgnoreNull(request, truck);
        truck.setFactoryId(factory_id);
        truck = truckRepository.save(truck);
        return CommonMapper.map(truck, TruckDto.class);
    }

    // update truck
    @Override
    public TruckDto updateTruck(String licensePlateNumber, UpdateTruckRequest request) {
        if(!truckRepository.existsById(licensePlateNumber)){
            throw new NotFoundException("Biển số xe không tồn tại");
        }
        Truck truck = truckRepository.getTruckByLicensePlateNumber(licensePlateNumber);
        BeanUtils.copyProperties(request, truck);
        truckRepository.save(truck);
        return CommonMapper.map(truck, TruckDto.class);
    }
}
