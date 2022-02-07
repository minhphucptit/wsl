package com.ceti.wholesale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.TruckWeighingVoucher;

import java.util.Optional;

@Repository
public interface TruckWeighingVoucherRepository extends JpaRepository<TruckWeighingVoucher, String> {

    @Query(value = "SELECT ISNULL(CONVERT(int , SUBSTRING((select top 1 id from truck_weighing_voucher "
            + "where id like %:date% and factory_id=:factory_id order by id desc) ,14, 5)),0) ", nativeQuery = true)
    Integer countTruckWeighingVoucherInDay(@Param("date") String date, @Param("factory_id") String factoryId);

    @Modifying
    @Query(value = "DELETE truck_weighing_voucher where id=?", nativeQuery = true)
    void deleteById(String id);

    @Modifying
    @Query(value = "DELETE truck_weighing_voucher where command_reference_id=?", nativeQuery = true)
    void deleteByCommandReferenceId(String commandReferenceId);

    Optional<TruckWeighingVoucher> findByCommandReferenceId(String commandReferenceId);
}
