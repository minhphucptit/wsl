package com.ceti.wholesale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.FactoryImportVoucher;

@Repository
public interface FactoryImportVoucherRepository extends JpaRepository<FactoryImportVoucher, String> {

    @Query(value = "SELECT ISNULL(CONVERT(int , SUBSTRING((select top 1 id from factory_import_voucher "
            + "where id like %:no_sample% order by id desc) ,14, 5)),0)", nativeQuery = true)
    Integer countFactoryImportVoucherInDay(@Param("no_sample") String noSample);

    @Modifying
    @Query(value = "DELETE factory_import_voucher where id=?", nativeQuery = true)
    void deleteById(String id);

}
