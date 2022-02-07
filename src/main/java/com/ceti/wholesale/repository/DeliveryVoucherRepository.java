package com.ceti.wholesale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.DeliveryVoucher;

@Repository
public interface DeliveryVoucherRepository extends JpaRepository<DeliveryVoucher, String> {

    @Query(value = "SELECT ISNULL(CONVERT(int , SUBSTRING((select top 1 id from delivery_voucher "
            + "where id like %:no_sample% order by id desc) ,14, 5)),0)", nativeQuery = true)
    Integer countDeliveryVoucherInDay(@Param("no_sample") String noSample);

    boolean existsByNo(String no);
}
