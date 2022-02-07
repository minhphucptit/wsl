package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.OtherDiscount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface OtherDiscountRepository extends JpaRepository<OtherDiscount,String> {

    @Modifying
    void deleteAllByCustomerCodeAndMonthAndYear(String customerCode, Integer month, Integer year);

    Page<OtherDiscount> getAllByCustomerCodeAndMonthAndYear(String customerCode, Integer month, Integer year, Pageable page);
    
}
