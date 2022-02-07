package com.ceti.wholesale.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.ProductionMonitoring;
import com.ceti.wholesale.model.ProductionMonitoringId;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductionMonitoringRepository extends JpaRepository<ProductionMonitoring, ProductionMonitoringId> {
	
    @Query(value = "SELECT * from production_monitoring pm WHERE pm.customer_id = :customer_id "
    		+ "AND pm.voucher_at =:voucher_at", nativeQuery = true)
	ProductionMonitoring getByCustomerIdAndVoucherAt(@Param("customer_id") String customerId, @Param("voucher_at") Instant voucherAt);

	@Transactional
	@Modifying
	@Query(value = "DELETE production_monitoring WHERE customer_id = :customer_id "
			+ "AND voucher_at =:voucher_at", nativeQuery = true)
	void deleteByCustomerIdAndVoucherAt(@Param("customer_id") String customerId, @Param("voucher_at") Instant voucherAt);
//
//	boolean existsByCustomerId(String customerId);
//
//	boolean existsByVoucherAt(Instant voucherAt);
	
	boolean existsByCustomerIdAndVoucherAt(String customerId, Instant voucherAt);

}
