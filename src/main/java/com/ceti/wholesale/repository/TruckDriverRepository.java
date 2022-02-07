package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.TruckDriver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckDriverRepository extends JpaRepository<TruckDriver, String> {

    @Query(value = "select * from truck_driver td where (:search is null or (td.abbreviated_name like %:search% or td.full_name like %:search% or td.id like %:search%)) " +
            "and (:id is null or td.id like %:id%) " +
            "and (:abbreviated_name is null or td.abbreviated_name like %:abbreviated_name%) " +
            "and (:full_name is null or td.full_name like %:full_name%) " +
            "and (:is_active is null or td.is_active = :is_active) " +
            "and (:phone_number is null or td.phone_number like %:phone_number%) and factory_id=:factory_id", nativeQuery = true)
    Page<TruckDriver> getAllByCondition(@Param("search") String search, @Param("id") String id, @Param("abbreviated_name") String abbreviatedName,
                                        @Param("full_name") String fullName, @Param("phone_number") String phoneNumber,
                                        @Param("factory_id") String factory_id, @Param("is_active") Boolean isActive, Pageable pageable);

    @Override
    boolean existsById(String id);

    TruckDriver getTruckDriverById(String id);
}
