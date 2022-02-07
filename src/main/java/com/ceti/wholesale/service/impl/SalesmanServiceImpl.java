package com.ceti.wholesale.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.dto.FactoryDto;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.repository.SalesmanDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateSalemanRequest;
import com.ceti.wholesale.controller.api.request.UpdateSalemanRequest;
import com.ceti.wholesale.dto.SalesmanDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.Salesman;
import com.ceti.wholesale.repository.SalesmanRepository;
import com.ceti.wholesale.service.SalesmanService;
import org.springframework.util.MultiValueMap;

@Service
public class SalesmanServiceImpl implements SalesmanService {

    @Autowired
    SalesmanRepository salesmanRepository;

    @Autowired
    SalesmanDetailRepository salesmanDetailRepository;

    @Override
    public Page<SalesmanDto> getAllByCondition(MultiValueMap<String, String> where, String embed, String factoryId, Pageable pageable) {
        List<SalesmanDto> salesmanDtos = new ArrayList<>();
        Long totalItems;
        if(embed != null && embed.equals("factory")){
            ResultPage<Object[]> page = salesmanDetailRepository.findAllWithEmbed(pageable, where);
            totalItems = page.getTotalItems();
            for (Object[] object : page.getPageList()) {
                Salesman salesman = (Salesman) object[0];
                SalesmanDto salesmanDto = CommonMapper.map(salesman, SalesmanDto.class);
                if (object[1] != null) {
                    Factory factory = (Factory) object[1];
                    FactoryDto factoryDto = CommonMapper.map(factory, FactoryDto.class);
                    salesmanDto.setFactory(factoryDto);
                }
                salesmanDtos.add(salesmanDto);
            }
        }else{
            where.add("factory_id", factoryId);
            ResultPage<Salesman> page = salesmanDetailRepository.findAll(pageable, where);
            totalItems = page.getTotalItems();
            for(Salesman truckDriver : page.getPageList()){
                salesmanDtos.add(CommonMapper.map(truckDriver, SalesmanDto.class));
            }
        }
        return new PageImpl<>(salesmanDtos, pageable, totalItems);

    }

    //create new saleman
    @Override
    public SalesmanDto createSaleman(CreateSalemanRequest request, String factory_id) {
        if (salesmanRepository.existsById(request.getId())) {
            throw new BadRequestException("Mã nhân viên đã tồn tại");
        }

        Salesman salesman = new Salesman();
        CommonMapper.copyPropertiesIgnoreNull(request, salesman);
        salesman.setFactoryId(factory_id);
        salesman = salesmanRepository.save(salesman);
        return CommonMapper.map(salesman, SalesmanDto.class);

    }

    //update saleman
    @Override
    public SalesmanDto updateSaleman(String id, UpdateSalemanRequest request) {
        Optional<Salesman> optional = salesmanRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Mã nhân viên không tồn tại");
        }
        Salesman salesman = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(request, salesman);
        salesman = salesmanRepository.save(salesman);
        return CommonMapper.map(salesman, SalesmanDto.class);
    }
}
