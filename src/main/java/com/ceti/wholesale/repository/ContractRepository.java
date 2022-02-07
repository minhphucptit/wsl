package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.Contract;
import com.ceti.wholesale.model.ContractCategory;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {
    @Query(value = "select * from contract c where (:search is null or c.contract_number like %:search%)"
            + "and (:sign_date_from is null or c.sign_date >=:sign_date_from)"
            + "and (:sign_date_to is null or c.sign_date <=:sign_date_to)"
            + "and (:expire_date_from is null or c.expire_date >=:expire_date_from)"
            + "and (:expire_date_to is null or c.expire_date <=:expire_date_to)"
            + "and (:status is null or c.status =:status)"
            + "and (:delivery_method is null or c.delivery_method =:delivery_method)"
            + "and (:contract_category_id is null or c.contract_category_id like %:contract_category_id%) "
            + "and c.factory_id=:factory_id", nativeQuery = true)
    public Page<Contract> getAllByConditions(
            @Param("search") String contractNumber,
            @Param("sign_date_from") Instant signDateFrom,
            @Param("sign_date_to") Instant signDateTo,
            @Param("expire_date_from") Instant expireDateFrom,
            @Param("expire_date_to") Instant expireDateTo,
            @Param("status") String status,
            @Param("delivery_method") String deliveryMethod,
            @Param("contract_category_id") String ContractCategoryId,
            @Param("factory_id") String factoryId
            , Pageable pageable);

    Optional<Contract> findByContractNumberAndFactoryId(String contractNumber, String factoryId);

    Boolean existsByContractNumberAndFactoryId(String contractNumber, String factoryId);
}
