package com.ceti.wholesale.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.ProductType;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, String> {
    @Query(value = "select * from product_type pt where (:search is null or pt.name like %:search% or pt.id like %:search%) "
            + "and (:name is null or pt.name like %:name%) "
            + "and (:is_active is null or pt.is_active =:is_active) "
            + "and (:id is null or pt.id like %:id%)", nativeQuery = true)
    Page<ProductType> getAllByCondition(@Param("search") String search, @Param("name") String name,
                                        @Param("id") String id, @Param("is_active") Boolean isActive, Pageable pageable);
}
