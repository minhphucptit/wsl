
package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.ProductCategory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {
    @Query(value = "select * from product_category pc where (:search is null or pc.name like %:search% or pc.id like %:search%) "
            + "and (:name is null or pc.name like %:name%) "
            + "and (:is_active is null or pc.is_active=:is_active) "
            + "and (:product_type_id is null or pc.product_type_id like %:product_type_id%)", nativeQuery = true)
    Page<ProductCategory> getAllByCondition(@Param("search") String search, @Param("name") String name,
                                            @Param("product_type_id") String productTypeId, @Param("is_active") Boolean isActive, Pageable pageable);
}

