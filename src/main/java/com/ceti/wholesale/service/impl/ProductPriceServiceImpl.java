package com.ceti.wholesale.service.impl;


import java.util.ArrayList;
import java.util.List;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.dto.ProductDto;
import com.ceti.wholesale.model.Product;
import com.ceti.wholesale.repository.ProductPriceDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.dto.ProductPriceDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.ProductPrice;
import com.ceti.wholesale.service.ProductPriceService;
import org.springframework.util.MultiValueMap;

@Service
@Transactional
public class ProductPriceServiceImpl implements ProductPriceService {

    @Autowired
    private ProductPriceDetailRepository productPriceDetailRepository;

    @Override
    public Page<ProductPriceDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
        ResultPage<Object[]> page = productPriceDetailRepository.findAllWithFilter(pageable, where);
        List<ProductPriceDto> productPriceDtos = new ArrayList<>();
        for (Object[] object : page.getPageList()) {
            ProductPrice p = (ProductPrice) object[0];
            ProductPriceDto productPriceDto = CommonMapper.map(p, ProductPriceDto.class);

            if (object[1] != null) {
                Product product = (Product) object[1];
                ProductDto productDto = CommonMapper.map(product, ProductDto.class);
                productPriceDto.setProduct(productDto);
            }

            productPriceDtos.add(productPriceDto);
        }
        return new PageImpl<>(productPriceDtos, pageable, page.getTotalItems());
    }
}
