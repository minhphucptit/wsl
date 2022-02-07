package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.CustomerPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomerPriceRepository extends JpaRepository<CustomerPrice, String> {

  @Query(value = "SELECT * FROM customer_price cp WHERE "
      + " (:customer_name is null or cp.customer_name like %:customer_name%) "
      + " and [dbo].[v1/check_inlist](cp.customer_category,:customer_category)= 1 "
      + " and (:month is null or cp.month =:month) "
      + " and (:year is null or cp.year =:year) "
      + " and factory_id = :factory_id", nativeQuery = true)
  Page<CustomerPrice> getAllByCondition(@Param("customer_name") String customerName,
      @Param("customer_category") String customerCategory,
      @Param("month") Integer month, @Param("year") Integer year, @Param("factory_id") String factory_id,
      Pageable pageable);

  boolean existsByCustomerIdAndMonthAndYearAndFactoryId(String customerId, Integer month, Integer year, String factoryId);

  @Transactional
  @Query(value = "{call [v4/forward_customer_price](:month_from,:year_from,:month_to,:year_to,:factory_id)}", nativeQuery = true)
  Boolean setForwardCustomerPrice(@Param("month_from") Integer monthFrom,
      @Param("year_from") Integer yearFrom,
      @Param("month_to") Integer monthTo,
      @Param("year_to") Integer yearTo,
      @Param("factory_id") String factoryId);

  @Query(value = "{call [v4/update_good_in_out_from_customer_price](:year_to,:month_to,:factory_id)}", nativeQuery = true)
  Boolean applyCustomerPrice(
      @Param("month_to") Integer monthTo,
      @Param("year_to") Integer yearTo,
      @Param("factory_id") String factoryId);
}
