package com.ceti.wholesale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.ProductPrice;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, String> {

    @Query(value = "select * from product_price pr where pr.product_id = :product_id and pr.factory_id = :factory_id"
            + "and  pr.is_active = 1 ", nativeQuery = true)
    ProductPrice findLatestProductPrice(@Param("product_id") String productId, @Param("factory_id") String factoryId);

}