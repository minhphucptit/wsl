package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.CustomerProductDiscount;
import com.ceti.wholesale.repository.custom.CustomerProductDiscountRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerProductDiscountRepository extends JpaRepository<CustomerProductDiscount,String>, CustomerProductDiscountRepositoryCustom {
    Boolean existsByProductIdAndCustomerId(String productId,String customerId);
}
