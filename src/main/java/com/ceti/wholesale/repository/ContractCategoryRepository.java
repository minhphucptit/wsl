package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.ContractCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractCategoryRepository extends JpaRepository<ContractCategory,String> {

      @Query(value = "select * from contract_category cc where (:name is null or cc.name like %:name%)"
      + "and (:is_active is null or cc.is_active =:is_active)" ,nativeQuery = true)
      public Page<ContractCategory> getAllByConditions(@Param("name") String name, @Param("is_active")Boolean isActive
      , Pageable pageable);

}
