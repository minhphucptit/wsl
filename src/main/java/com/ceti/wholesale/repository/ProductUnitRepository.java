package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.ProductUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnit, String> {
    List<ProductUnit> findAllByOrderBySortAsc();
}
