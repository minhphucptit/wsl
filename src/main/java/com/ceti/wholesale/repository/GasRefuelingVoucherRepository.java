package com.ceti.wholesale.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.model.GasRefuelingVoucher;
import com.ceti.wholesale.model.GoodsInOut;

@Repository
public interface GasRefuelingVoucherRepository extends JpaRepository<GasRefuelingVoucher, String> {
    boolean existsByVoucherAtAndFactoryId(Instant voucherAt, String factoryId);
    
    //hàm gọi sau khi tạo phiếu kiểm kê thì tạo chiết nạp gas, gợi ý tạo chiết nạp gas, tạo chiết nạp gas
    @Transactional
    @Modifying
    @Query(value="exec [dbo].[v4/post_inventory_voucher_tran] :factory_id,:voucher_at, :gas_refueling_voucher_id, :is_created", nativeQuery = true)
    void genarateGasRefuleingVoucher(@Param("voucher_at") Instant voucherAt, @Param("factory_id") String factoryId,
    		@Param("gas_refueling_voucher_id") String gasRefuelingVoucherId, @Param("is_created") boolean isCreated);
}
