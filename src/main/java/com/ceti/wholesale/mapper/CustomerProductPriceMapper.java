package com.ceti.wholesale.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface CustomerProductPriceMapper {

    Integer updateCustomerProductPrice(@Param("price") BigDecimal price, @Param("factory_id") String factoryId, @Param("list_customer_id") String listCustomerId,
                        @Param("list_product_id") String listProductId);
}
