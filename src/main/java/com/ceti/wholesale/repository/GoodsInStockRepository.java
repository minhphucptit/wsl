package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.GoodsInStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GoodsInStockRepository extends JpaRepository<GoodsInStock, String> {

//    @Transactional
//    @Modifying
//    @Query(value = "{call [v5/forward_goods_in_stock](:year_from,:year_to,:factory_id,:user_full_name)}",nativeQuery = true)
//    Integer setForwardToNextYear(@Param("year_from") Integer yearFrom,
//                                 @Param("year_to") Integer yearTo,
//                                 @Param("factory_id") String factoryId,
//                                 @Param("user_full_name") String userFullName);

}
