package com.ceti.wholesale.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.Truck;

@Repository
public interface TruckRepository extends JpaRepository<Truck, String> {

    @Query(value = "select * from truck td "
            + "where (:license_plate_number is null or td.license_plate_number like %:license_plate_number%) "
            + "and factory_id=:factory_id "
            + "and (:is_active is null or td.is_active=:is_active)", nativeQuery = true)
    Page<Truck> getAllByCondition(@Param("license_plate_number") String license_plate_number, @Param("factory_id") String factory_id,
                                  @Param("is_active") Boolean isActive, Pageable pageable);

    boolean existsByLicensePlateNumberAndFactoryId(String licensePlateNumber, String factoryId);

    Truck getTruckByLicensePlateNumber(String id);

    @Query(value = "Select top 1 license_plate_number from truck t WHERE t.is_active = 1 AND REPLACE(t.license_plate_number,'.','')  in (:license_plate_number, :license_plate_number_replace_dots)", nativeQuery = true)
    String findAllByLicensePlateNumber(@Param("license_plate_number") String licensePlateNumber, @Param("license_plate_number_replace_dots") String licensePlateNumberReplaceDots);
}
