package com.ceti.wholesale.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ceti.wholesale.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query(value = "select case when 'TDAILY'=(SELECT category from customer where id=:customer_id) then a.wholesale_price else a.sale_price end "
            + " from product a join product b on a.id= b.reference_product_id and a.factory_id = b.factory_id where b.id=:product_id and a.factory_id = :factory_id", nativeQuery = true)
    BigDecimal getBylinderPriceByCustomerAndProduct(@Param("customer_id") String customerId, @Param("product_id") String productId, @Param("factory_id") String factoryId);

    boolean existsByIdAndFactoryId(String id, String factoryId);

    Optional<Product> findByIdAndFactoryId(String id, String factoryId);

    @Modifying
    @Query( value = "update product set sale_price=:sale_price where weight=:weight and factory_id=:factory_id " +
            "insert into product_price select GETDATE(), REPLACE(NEWID(),'-',''),:update_by_full_name, product.buy_price, :sale_price, product.factory_id, " +
            "product.id from product  where weight=:weight and factory_id =:factory_id", nativeQuery = true)
    void updateProductPriceByWeight(@Param("weight") BigDecimal weight,
                                    @Param("factory_id") String factoryId,
                                    @Param("update_by_full_name") String updateByFullName,
                                    @Param("sale_price") BigDecimal salePrice);
    @Modifying
    @Query(value = "select DISTINCT p.weight from product p where p.type ='GAS' and p.weight>0 and p.factory_id=:factory_id", nativeQuery = true)
    List<String> getListWeight(@Param("factory_id") String factoryId);

    @Query(value = "select DISTINCT id from product where (:product_id is null or id like %:product_id%) and (:factory_id is null or factory_id =:factory_id) and is_active = 1",nativeQuery = true)
    List<String>getAllProductId(@Param("product_id")String id,@Param("factory_id")String factoryId);

	boolean existsByReferenceProductIdAndFactoryId(String referenceProductId, String factoryId);

}
