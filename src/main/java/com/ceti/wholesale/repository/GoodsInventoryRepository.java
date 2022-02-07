package com.ceti.wholesale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.model.GoodsInventory;

@Repository
public interface GoodsInventoryRepository extends JpaRepository<GoodsInventory, String> {
	@Modifying
	@Transactional
	@Query(value = "DELETE goods_inventory WHERE voucher_id=?", nativeQuery = true)
	void deleteByVoucherId(String voucherId);

	@Query(value = "SELECT * FROM goods_inventory a WHERE voucher_id = :voucher_id "
			+ "AND (:product_type is null or a.product_type=:product_type) ", nativeQuery = true)
	List<GoodsInventory> findByVoucherId(@Param("voucher_id")String voucherId, @Param("product_type") String productType);
}
