package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateTruckDriverRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckDriverRequest;
import com.ceti.wholesale.dto.FactoryDto;
import com.ceti.wholesale.dto.ProductCategoryDto;
import com.ceti.wholesale.dto.ProductDto;
import com.ceti.wholesale.dto.TruckDriverDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.Product;
import com.ceti.wholesale.model.ProductCategory;
import com.ceti.wholesale.model.Truck;
import com.ceti.wholesale.model.TruckDriver;
import com.ceti.wholesale.repository.TruckDriverDetailRepository;
import com.ceti.wholesale.repository.TruckDriverRepository;
import com.ceti.wholesale.service.TruckDriverService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TruckDriverServiceImpl implements TruckDriverService {

    @Autowired
    private TruckDriverRepository truckDriverRepository;

    @Autowired
    private TruckDriverDetailRepository truckDriverDetailRepository;

    @Override
    public Page<TruckDriverDto> getAllByCondition(MultiValueMap<String, String> where, String embed, Pageable pageable) {
        List<TruckDriverDto> truckDriverDtos = new ArrayList<>();
        Long totalItems;
        if(embed != null && embed.equals("factory")){
                ResultPage<Object[]> page = truckDriverDetailRepository.findAllWithEmbed(pageable, where);
                totalItems = page.getTotalItems();
                for (Object[] object : page.getPageList()) {
                    TruckDriver truckDriver = (TruckDriver) object[0];
                    TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
                    if (object[1] != null) {
                        Factory factory = (Factory) object[1];
                        FactoryDto factoryDto = CommonMapper.map(factory, FactoryDto.class);
                        truckDriverDto.setFactory(factoryDto);
                    }
                    truckDriverDtos.add(truckDriverDto);
                }
        }else{
            ResultPage<TruckDriver> page = truckDriverDetailRepository.findAll(pageable, where);
            totalItems = page.getTotalItems();
            for(TruckDriver truckDriver : page.getPageList()){
                truckDriverDtos.add(CommonMapper.map(truckDriver, TruckDriverDto.class));
            }
        }
        return new PageImpl<>(truckDriverDtos, pageable, totalItems);

    }

    //create truck driver
    @Override
    public TruckDriverDto createTruckDriver(CreateTruckDriverRequest request, String factory_id) {
        if (truckDriverRepository.existsById(request.getId())) {
            throw new BadRequestException("Mã tài xế đã tồn tại");
        }
        TruckDriver truckDriver = new TruckDriver();
        CommonMapper.copyPropertiesIgnoreNull(request, truckDriver);
        truckDriver.setFactoryId(factory_id);
        TruckDriver newTruckDriver = truckDriverRepository.save(truckDriver);
        return CommonMapper.map(newTruckDriver, TruckDriverDto.class);
    }

    //update Truck driver
    @Override
    public TruckDriverDto updateTruckDriver(String truckDriverId, UpdateTruckDriverRequest request) {
        if(!truckDriverRepository.existsById(truckDriverId)){
            throw new NotFoundException("Mã tài xế không tồn tại");
        }
        TruckDriver truckDriver = truckDriverRepository.getTruckDriverById(truckDriverId);
        BeanUtils.copyProperties(request, truckDriver);
        truckDriverRepository.save(truckDriver);
        return CommonMapper.map(truckDriver, TruckDriverDto.class);
    }

}
