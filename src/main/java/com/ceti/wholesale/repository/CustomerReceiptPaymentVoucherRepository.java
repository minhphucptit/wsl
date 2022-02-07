package com.ceti.wholesale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.CustomerReceiptPaymentVoucher;

@Repository
public interface CustomerReceiptPaymentVoucherRepository extends JpaRepository<CustomerReceiptPaymentVoucher, String> {

    @Query(value =
            "select * from customer_receipt_payment_voucher rp where (:customer_id is null or (rp.customer_id =:customer_id)) "
                    + "and (:customer_full_name is null or (rp.customer_full_name like %:customer_full_name%)) "
                    + "and (:payer is null or (rp.payer like %:payer%)) and (:payer_method is null or (rp.payer_method =:payer_method)) "
                    + "and (:category is null or (rp.category =:category)) "
                    + "and (:type is null or (rp.type =:type))"
                    + "and (:factory_id is null or (rp.factory_id =:factory_id))"
            , nativeQuery = true
    )
    public Page<CustomerReceiptPaymentVoucher> getAllByCondition(
            @Param("customer_id") String customerId, @Param("customer_full_name") String customerFullName,
            @Param("payer") String payer, @Param("payer_method") Boolean payerMethod,
            @Param("category") String category, @Param("type") Boolean type, @Param("factory_id") String factoryId, Pageable pageable);

    @Query(value = "SELECT ISNULL(CONVERT(int , SUBSTRING((select top 1 no from customer_receipt_payment_voucher "
            + "where no like %:no_sample% order by no desc) ,13, 5)),0)", nativeQuery = true)
    Integer countReceiptPaymentInday(@Param("no_sample") String noSample);
}
