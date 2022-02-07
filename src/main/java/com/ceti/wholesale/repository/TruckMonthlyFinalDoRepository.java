package com.ceti.wholesale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.TruckMonthlyFinalDo;

@Repository
public interface TruckMonthlyFinalDoRepository extends JpaRepository<TruckMonthlyFinalDo, String> {
		
	TruckMonthlyFinalDo getByMonthAndYearAndTruckLicensePlateNumberAndFactoryId(Integer month, Integer year, String trucklicensePlateNumber, String factoryId);

}
