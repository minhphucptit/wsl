package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.ProductAccessory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAccessoryRepository extends JpaRepository<ProductAccessory, String> {

    List<ProductAccessory> findByMainProductIdAndFactoryId(String productId, String factoryId);

    void deleteByMainProductIdAndFactoryId(String mainProductId, String factoryId);
}
