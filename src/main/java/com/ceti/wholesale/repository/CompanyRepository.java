package com.ceti.wholesale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

    @Query(value =
            "select * from company co where (:search is null or (co.address like %:search% or co.name like %:search% or co.id like %:search%))"
                    + "and (:id is null or co.id like %:id%) "
                    + "and (:name is null or co.name like %:name%) "
                    + "and (:address is null or co.address like %:address%) "
                    + "and (:is_active is null or co.is_active=:is_active) "
                    + "and (:phone_number is null or co.phone_number like %:phone_number%)", nativeQuery = true)
    Page<Company> getAllByCondition(@Param("search") String search, @Param("id") String id,
                                    @Param("name") String name, @Param("address") String address, @Param(
            "phone_number") String phoneNumber, @Param("is_active") Boolean isActive, Pageable pageable);
}
