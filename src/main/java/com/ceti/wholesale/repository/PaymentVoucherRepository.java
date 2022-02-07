package com.ceti.wholesale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.PaymentVoucher;

@Repository
public interface PaymentVoucherRepository extends JpaRepository<PaymentVoucher, String> {

    @Query(value = "SELECT ISNULL(CONVERT(int , SUBSTRING((select top 1 id from payment_voucher "
            + "where id like %:no_sample% order by id desc) ,14, 5)),0)", nativeQuery = true)
    Integer countPaymentVoucherInDay(@Param("no_sample") String noSample);

    @Query(value = "select id from payment_voucher where voucher_id in :ids", nativeQuery = true)
    List<String> findIdByVoucherIdIn(List<String> ids);

    @Query(value = "select id from payment_voucher where voucher_id = ?", nativeQuery = true)
    List<String> findIdByVoucherId(String id);

    void deleteByIdIn(List<String> paymentVoucherIds);

    boolean existsByVoucherId(String voucherId);
    
    List<PaymentVoucher> findAllByVoucherId(String voucherId);

    PaymentVoucher findByVoucherId(String voucherId);

    @Query(value = "select * from payment_voucher where voucher_id in :ids", nativeQuery = true)
    List<PaymentVoucher> findByVoucherIdIn(List<String> ids);
}
