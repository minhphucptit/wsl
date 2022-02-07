package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.Factory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoryRepository extends JpaRepository<Factory, String> {

    Factory getById(String id);

    boolean existsById(String id);
}
