package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.ExportVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportVoucherRepository extends JpaRepository<ExportVoucher, String> {
    @Query(value = "SELECT ISNULL(CONVERT(int , SUBSTRING((select top 1 no from export_voucher "
            + "where no like %:no_sample% order by no desc) ,10, 5)),0)", nativeQuery = true)
    Integer countExportVoucherInDay(@Param("no_sample") String noSample);

    @Modifying
    @Query(value = "DELETE export_voucher where id=:export_voucher_id", nativeQuery = true)
    void deleteById(String export_voucher_id);
}
