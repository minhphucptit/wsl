package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.CustomerGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, String> {

    @Query(value = "select * from customer_group cg where (:search is null or cg.name like %:search% or cg.id like %:search% )" +
            " and (:id is null or cg.id like %:id%) " +
            " and (:name is null or cg.name like %:name%) " +
            " and (:is_active is null or cg.is_active=:is_active) " +
            " and (:factoryId is null or factory_id=:factoryId) ", nativeQuery = true)
    Page<CustomerGroup> getAllByCondition(@Param("search") String search, @Param("id") String id, @Param("name") String name, @Param("factoryId") String factoryId,
                                          @Param("is_active") Boolean isActive, Pageable pageable);
}
