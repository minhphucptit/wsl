package com.ceti.wholesale.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.SoldDeliveryVoucher;

@Repository
public interface SoldDeliveryVoucherRepository extends JpaRepository<SoldDeliveryVoucher, String> {

    @Query(value = "SELECT ISNULL(CONVERT(int , SUBSTRING((select top 1 id from sold_delivery_voucher "
            + "where id like %:no_sample% order by id desc) ,14, 5)),0) ", nativeQuery = true)
    Integer countSoldDeliveryVoucherInDay(@Param("no_sample") String noSample);

    @Modifying
    @Transactional
    @Query(value = "update sold_delivery_voucher set total_payment_received=:total_payment_received, total_goods_return=:total_goods_return, payment_voucher_id=:payment_voucher_id where id=:id", nativeQuery = true)
    void updateTotalPaymentReceivedAndTotalGoodsReturnAndPaymentVoucherId(@Param("total_payment_received") BigDecimal totalPaymentReceived,
                                                                          @Param("total_goods_return") BigDecimal totalGoodsReturn, @Param("id") String id, @Param("payment_voucher_id") String paymentVoucherId);

    @Query(value = "select id from sold_delivery_voucher where delivery_voucher_id=?", nativeQuery = true)
    List<String> findIdByDeliveryVoucherId(String id);

    void deleteByIdIn(List<String> ids);
    
    List<SoldDeliveryVoucher> findAllByDeliveryVoucherId(String deliveryVoucherId);


}
