package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.CustomerPrice;
import com.ceti.wholesale.model.CustomerPriceByMonth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CustomerPriceByMonthRepository extends JpaRepository<CustomerPriceByMonth,String> {

    @Query(value = "SELECT * FROM customer_price_by_month cpbm WHERE "
            + " (:customer_name is null or cpbm.customer_name like %:customer_name%) "
            + " and [dbo].[v1/check_inlist](cpbm.customer_category,:customer_category)= 1 "
            + " and (:year is null or cpbm.year =:year) "
            + " and cpbm.factory_id = :factory_id", nativeQuery = true)
    Page<CustomerPriceByMonth> getAllByCondition(@Param("customer_name") String customerName,
                                                 @Param("customer_category") String customerCategory,
                                                 @Param("year") Integer year, @Param("factory_id") String factory_id,
                                                 Pageable pageable);

//    boolean existsByCustomerId(String customerId);
    boolean existsByCustomerIdAndYearAndFactoryId(String customerId, Integer year, String factoryId);

    @Transactional
    @Query(value = "{call [v4/forward_customer_price_by_month](:year_from,:year_to,:factory_id)}", nativeQuery = true)
    Boolean setForwardCustomerPriceByMonth(@Param("year_from") Integer yearFrom,
                                    @Param("year_to") Integer yearTo,
                                    @Param("factory_id") String factoryId);

    @Query(value = "{call [v4/update_good_in_out_from_customer_price_by_month](:year,:factory_id)}", nativeQuery = true)
    Boolean applyCustomerPriceByMonth(
            @Param("year") Integer year,
            @Param("factory_id") String factoryId);
}
