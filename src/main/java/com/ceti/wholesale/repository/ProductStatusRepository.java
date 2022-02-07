package com.ceti.wholesale.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.ProductStatus;

@Repository
public interface ProductStatusRepository extends JpaRepository<ProductStatus, String> {

}
