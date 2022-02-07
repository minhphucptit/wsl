package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.MaintainVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintainVoucherRepository extends JpaRepository<MaintainVoucher, String> {

    @Query(value = "SELECT ISNULL(CONVERT(int , SUBSTRING((select top 1 id from maintain_voucher "
            + "where id like %:no_sample% order by id desc) ,14, 5)),0)", nativeQuery = true)
    Integer countMaintainVoucherInDay(@Param("no_sample") String noSample);

    @Modifying
    @Query(value = "DELETE maintain_voucher where id=?", nativeQuery = true)
    void deleteById(String id);

}
