package com.ceti.wholesale.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.AccountVoucherCode;

@Repository
public interface AccountVoucherCodeRepository extends JpaRepository<AccountVoucherCode, String> {
	
    @Query(value = "SELECT COUNT(*) FROM account_voucher_code WHERE acc_no like %:date%" +
            " and group_code=:group_code and factory_id = :factory_id ", nativeQuery = true)
    Integer countNumberOfVouchers(@Param("date") String date, @Param("group_code") String groupCode, @Param("factory_id") String factoryId);
    
    @Transactional
    @Modifying
    @Query(value = "update account_voucher_code set active = 0 where voucher_id=:voucher_id ", nativeQuery = true)
    void deleteByVoucherId(@Param("voucher_id") String voucherId);

}
