package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.ImportVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportVoucherRepository extends JpaRepository<ImportVoucher, String> {

    @Query(value = "SELECT ISNULL(CONVERT(int , SUBSTRING((select top 1 no from import_voucher "
            + "where no like %:no_sample% order by no desc) ,10, 5)),0)", nativeQuery = true)
    Integer countImportVoucherInDay(@Param("no_sample") String noSample);

    @Modifying
    @Query(value = "DELETE import_voucher where id=:import_voucher_id", nativeQuery = true)
    void deleteById(String import_voucher_id);
}
