package com.ceti.wholesale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.ReceiptPaymentCategory;

@Repository
public interface ReceiptPaymentCategoryRepository extends JpaRepository<ReceiptPaymentCategory, String> {

    @Query(value =
            "select * from receipt_payment_category rc where (:search is null or (rc.name like %:search%) or (rc.id like %:search%)) "
                    + " and (:id is null or rc.id =:id) "
                    + " and (:type is null or rc.type =:type) "
                    + " and (:is_active is null or rc.is_active =:is_active)"
            , nativeQuery = true
    )
    public Page<ReceiptPaymentCategory> getAllByCondition(@Param("search") String search, @Param("id") String id, @Param("type") Boolean type,
                                                          @Param("is_active") Boolean isActive, Pageable pageable);
}
