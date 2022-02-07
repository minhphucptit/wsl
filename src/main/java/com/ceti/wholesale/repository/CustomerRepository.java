package com.ceti.wholesale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.Customer;

@Repository
public interface  CustomerRepository extends JpaRepository<Customer, String> {
	boolean existsByIdAndFactoryId(String id, String factoryId);
	
	boolean existsByCode(String code);
	
    @Query(value = "SELECT top 1 id from customer c WHERE c.code = :customer_code ", nativeQuery = true)
	String getFirstCustomerIdByCustomerCode(@Param("customer_code") String customerCode);

	@Query(value = "SELECT top 1 name from customer c WHERE c.code = :customer_code ", nativeQuery = true)
	String findByName(@Param("customer_code") String customerCode);

}
