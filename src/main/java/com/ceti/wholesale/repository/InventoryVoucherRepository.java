package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.InventoryVoucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Repository
public interface InventoryVoucherRepository extends JpaRepository<InventoryVoucher, String> {

    @Query(value = "select * from inventory_voucher iv where " +
            "(:voucher_at_from is null or iv.voucher_at >=:voucher_at_from) " +
            "and (:voucher_at_to is null or iv.voucher_at <=:voucher_at_to) " +
            "and (:no is null or iv.no =:no) " +
            "and iv.factory_id =:factory_id", nativeQuery = true)
    Page<InventoryVoucher> getAllByConditions(@Param("voucher_at_from") Instant voucherAtFrom,
                                              @Param("voucher_at_to") Instant voucherAtTo,@Param("no") String no,
                                              @Param("factory_id") String factoryId,
                                              Pageable pageable);
}
