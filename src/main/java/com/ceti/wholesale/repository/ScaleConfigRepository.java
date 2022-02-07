package com.ceti.wholesale.repository;
import com.ceti.wholesale.model.ScaleConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScaleConfigRepository extends JpaRepository<ScaleConfig, String> {
    ScaleConfig findByFactoryId(String factoryId);
}
