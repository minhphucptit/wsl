package com.ceti.wholesale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.Salesman;

@Repository
public interface SalesmanRepository extends JpaRepository<Salesman, String> {

//    @Query(value = "select * from salesman s where (:search is null or (s.abbreviated_name like %:search% or s.full_name like %:search% or s.id like %:search%)) " +
//            "and (:abbreviated_name is null or s.abbreviated_name like %:abbreviated_name%) " +
//            "and (:id is null or s.id like %:id%) " +
//            "and (:full_name is null or s.full_name like %:full_name%) " +
//            "and (:address is null or s.address like %:address%) " +
//            "and (:is_active is null or s.is_active = :is_active) " +
//            "and (:phone_number is null or s.phone_number like %:phone_number%) and factory_id=:factoryId", nativeQuery = true)
//    Page<Salesman> getAllByCondition(@Param("search") String search,@Param("id") String id, @Param("abbreviated_name") String abbreviatedName,
//                                     @Param("full_name") String fullName, @Param("address") String address,
//                                     @Param("phone_number") String phoneNumber, @Param("factoryId") String factoryId,
//                                     @Param("is_active") Boolean isActive, Pageable pageable);
}
