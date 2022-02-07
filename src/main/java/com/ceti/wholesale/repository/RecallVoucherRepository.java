package com.ceti.wholesale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ceti.wholesale.model.RecallVoucher;

public interface RecallVoucherRepository extends JpaRepository<RecallVoucher, String> {

    @Query(value = "SELECT ISNULL(CONVERT(int , SUBSTRING((select top 1 id from recall_voucher "
            + "where id like %:no_sample% order by id desc) ,14, 5)),0)", nativeQuery = true)
    Integer countRecallVoucherInDay(@Param("no_sample") String noSample);

    @Query(value = "select id from recall_voucher where delivery_voucher_id=?", nativeQuery = true)
    List<String> findIdByDeliveryVoucherId(String id);

    void deleteByIdIn(List<String> recallVoucherIds);
    
    List<RecallVoucher> findAllByDeliveryVoucherId(String deliveryVoucherId);

}
