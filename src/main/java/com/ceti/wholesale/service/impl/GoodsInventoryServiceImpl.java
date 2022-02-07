package com.ceti.wholesale.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceti.wholesale.dto.GoodsInventoryDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.GoodsInventory;
import com.ceti.wholesale.repository.GoodsInventoryRepository;
import com.ceti.wholesale.service.GoodsInventoryService;

@Service
public class GoodsInventoryServiceImpl implements GoodsInventoryService {

    @Autowired
    private GoodsInventoryRepository goodsInventoryRepository;

//	@Override
//	public List<GoodsInventoryDto> getAll(String inventoryVoucherId, String productType) {
//		List<GoodsInventory> goodsInventories=goodsInventoryRepository.findByVoucherId(inventoryVoucherId, productType);
//		return CommonMapper.toList(goodsInventories, GoodsInventoryDto.class);
//	}
}
