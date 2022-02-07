package com.ceti.wholesale.repository;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.TruckDistance;

@Repository
public interface TruckDistanceRepository extends JpaRepository<TruckDistance, String> {
	
	boolean existsByTruckLicensePlateNumberAndDay(String truckLicensePlateNumber, Instant day);
	
	TruckDistance getByTruckLicensePlateNumberAndDay(String truckLicensePlateNumber, Instant day);

	void deleteByTruckLicensePlateNumberAndDay(String truckLicensePlateNumber, Instant day);

}
