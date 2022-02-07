package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.CylinderDebt;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CylinderDebtRepository extends JpaRepository<CylinderDebt, String> {

    @Modifying
    @Query(value = "DELETE cylinder_debt WHERE voucher_id=?", nativeQuery = true)
    void deleteByVoucherId(String voucherId);

    void deleteByVoucherIdIn(List<String> ids);

    @Transactional
    @Modifying
    @Query(value = "update cylinder_debt set out_quantity=:out_quantity where product_id=(select sub_product_id "
    		+ "from product_accessory where sub_product_type='VO' and main_product_id=:product_id and factory_id=:factory_id) "
    		+ "and voucher_id=:voucher_id and goods_in_out_type='XBSP'", nativeQuery = true)
    void updateCylinderDebtOfSoldVoucher1Product(@Param("voucher_id") String soldVoucherId, @Param("out_quantity") BigDecimal outQuantity,
    		@Param("product_id") String productId, @Param("factory_id") String factoryId);
}
