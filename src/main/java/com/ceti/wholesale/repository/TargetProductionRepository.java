package com.ceti.wholesale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.TargetProduction;
import com.ceti.wholesale.model.TargetProductionId;

@Repository
public interface TargetProductionRepository extends JpaRepository<TargetProduction, TargetProductionId> {

    boolean existsByCustomerCodeAndMonthAndYear(String customerCode, Integer month, Integer year);
    
    TargetProduction findByCustomerCodeAndMonthAndYear(String customerCode, Integer month, Integer year);
}
