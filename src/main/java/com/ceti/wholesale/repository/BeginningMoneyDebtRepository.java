package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.BeginningMoneyDebt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BeginningMoneyDebtRepository extends JpaRepository<BeginningMoneyDebt, String> {

    @Query(value = "select * from beginning_money_debt md where (:id is null or (md.id =:id)) " +
            "and (:customer_id is null or (md.customer_id like %:customer_id%))", nativeQuery = true)
    Page<BeginningMoneyDebt> getAllByCondition(@Param("id") String id, @Param("customer_id") String customerId, Pageable pageable);

//    @Transactional
//    @Modifying
//    @Query(value = "{call [v4/forward_beginning_money_dept](:year_from,:year_to,:factory_id,:user_full_name)}",nativeQuery = true)
//    public Integer setForwardToNextYear(@Param("year_from") Integer yearFrom,
//                                     @Param("year_to") Integer yearTo,
//                                     @Param("factory_id") String factoryId,
//                                     @Param("user_full_name")String userFullName);
}
