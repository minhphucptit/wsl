package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.ReturnVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnVoucherRepository extends JpaRepository<ReturnVoucher, String> {

    @Query(value = "SELECT ISNULL(CONVERT(int , SUBSTRING((select top 1 no from return_voucher "
            + "where no like %:no_sample% order by no desc) ,13, 5)),0)", nativeQuery = true)
    Integer countReturnVoucherInDay(@Param("no_sample") String noSample);

    void deleteByIdIn(List<String> ids);
    
    void deleteByDeliveryVoucherNo(String deliveryVoucherNo);
    
    ReturnVoucher findByDeliveryVoucherNo(String deliveryVoucherNo);

    List<ReturnVoucher> findAllByDeliveryVoucherId(String deliveryVoucherId);

}
