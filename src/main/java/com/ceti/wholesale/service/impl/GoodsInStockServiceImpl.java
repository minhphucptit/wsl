package com.ceti.wholesale.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateGoodsInStockRequest;
import com.ceti.wholesale.dto.GoodsInStockDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.GoodsInStock;
import com.ceti.wholesale.repository.GoodsInStockDetailRepository;
import com.ceti.wholesale.repository.GoodsInStockRepository;
import com.ceti.wholesale.service.GoodsInStockService;

@Service
@Transactional
public class GoodsInStockServiceImpl implements GoodsInStockService {
    @Autowired
    GoodsInStockRepository goodsInStockRepository;
    @Autowired
    GoodsInStockDetailRepository goodsInStockDetailRepository;

    @Override
    public Page<GoodsInStockDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
        ResultPage<GoodsInStock> page = goodsInStockDetailRepository.findAllWithFilter(where, pageable);
        List<GoodsInStockDto> goodsInStockDtos = new ArrayList<>();
        for (GoodsInStock goodsInStock : page.getPageList()) {
            goodsInStockDtos
                    .add(CommonMapper.map(goodsInStock, GoodsInStockDto.class));
        }
        return new PageImpl<>(goodsInStockDtos, pageable, page.getTotalItems());
    }

    @Override
    public GoodsInStockDto createGoodsInStock(CreateGoodsInStockRequest request, String factoryId) {
        if (goodsInStockRepository.existsById(request.getYear() + request.getCompanyId() + request.getProductId() + factoryId)) {
            throw new BadRequestException("Hàng hóa " + request.getProductId() + " đã có bản ghi đầu kỳ lượng hàng trong năm " + request.getYear());
        }
        Instant instant = Instant.now();
        GoodsInStock goodsInStock = new GoodsInStock();
        CommonMapper.copyPropertiesIgnoreNull(request, goodsInStock);
        goodsInStock.setCreateAt(instant);
        goodsInStock.setUpdateAt(instant);
        goodsInStock.setId(request.getYear() + request.getCompanyId() + request.getProductId() + factoryId);
        goodsInStock.setFactoryId(factoryId);

        goodsInStock = goodsInStockRepository.save(goodsInStock);
        return CommonMapper.map(goodsInStock, GoodsInStockDto.class);

    }

    @Override
    public GoodsInStockDto updateGoodsInStock(String id, CreateGoodsInStockRequest request) {
        Optional<GoodsInStock> optional = goodsInStockRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Mã không tồn tại");
        }
        GoodsInStock goodsInStock = optional.get();
        GoodsInStock newGoodsInStock = CommonMapper.map(request, GoodsInStock.class);
        newGoodsInStock.setId(request.getYear() + request.getCompanyId() + request.getProductId() + goodsInStock.getFactoryId());
        if (!newGoodsInStock.getId().equals(goodsInStock.getId()) && goodsInStockRepository.existsById(newGoodsInStock.getId())) {
            throw new BadRequestException("Hàng hóa " + request.getProductId() + " đã có bản ghi đầu kỳ lượng hàng trong năm " + request.getYear());
        }
        newGoodsInStock.setUpdateAt(Instant.now());
        newGoodsInStock.setCreateAt(goodsInStock.getCreateAt());
        newGoodsInStock.setCreateBy(goodsInStock.getCreateBy());
        newGoodsInStock.setFactoryId(goodsInStock.getFactoryId());
        goodsInStockRepository.delete(goodsInStock);
        goodsInStockRepository.save(newGoodsInStock);
        return CommonMapper.map(newGoodsInStock, GoodsInStockDto.class);
    }

    @Override
    public void deleteGoodsInStock(String id) {
        if (!goodsInStockRepository.existsById(id)) {
            throw new NotFoundException("đầu kì hàng hóa không tồn tại");
        }
        goodsInStockRepository.deleteById(id);
    }

//    @Override
//    public Integer forwardToNextYear(Integer yearFrom, Integer yearTo, String factoryId, String userFullName) {
//        return goodsInStockRepository.setForwardToNextYear(yearFrom,yearTo,factoryId,userFullName);
//    }
}
