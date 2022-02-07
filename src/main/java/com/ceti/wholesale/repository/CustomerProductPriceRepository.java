package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.CustomerProductPrice;
import com.ceti.wholesale.repository.custom.CustomerProductPriceRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerProductPriceRepository extends JpaRepository<CustomerProductPrice,String>, CustomerProductPriceRepositoryCustom {

    Boolean existsByProductIdAndCustomerId(String productId,String customerId);

}
