package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.FactoryExportVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoryExportVoucherRepository extends JpaRepository<FactoryExportVoucher, String> {

    @Query(value = "SELECT ISNULL(CONVERT(int , SUBSTRING((select top 1 id from factory_export_voucher "
            + "where id like %:no_sample% order by id desc) ,14, 5)),0)", nativeQuery = true)
    Integer countFactoryExportVoucherInDay(@Param("no_sample") String noSample);

    @Modifying
    @Query(value = "DELETE factory_export_voucher where id=?", nativeQuery = true)
    void deleteById(String id);

}
