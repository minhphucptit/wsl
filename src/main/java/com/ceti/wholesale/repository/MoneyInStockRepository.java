package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.MoneyInStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MoneyInStockRepository extends JpaRepository<MoneyInStock, String> {
    @Query(value = "select * from money_in_stock WHERE factory_id =:factory_id", nativeQuery = true)
    Page<MoneyInStock> getAllByCondition(@Param("factory_id") String factoryId, Pageable pageable);

//    @Transactional
//    @Query(value = "{call [v4/forward_money_in_stock](:year_from,:year_to,:factory_id,:user_full_name)}",nativeQuery = true)
//    public Integer setForwardToNextYear(@Param("year_from") Integer yearFrom,
//                                        @Param("year_to") Integer yearTo,
//                                        @Param("factory_id") String factoryId,
//                                        @Param("user_full_name")String userFullName);
}
