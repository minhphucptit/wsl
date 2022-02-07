package com.ceti.wholesale.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.EquipmentAccessorie;

@Repository
public interface EquipmentAccessorieRepository extends JpaRepository<EquipmentAccessorie, String> {
	@Modifying
    @Query(value = "DELETE equipment_accessorie WHERE equipment_id= :equipment_id", nativeQuery = true)
	void deleteByEquipmentId(@Param("equipment_id")String equipmentId);
	
	
	@Query(value = "select * from equipment_accessorie ea where ea.equipment_id = :equipment_id ", nativeQuery = true)
	List<EquipmentAccessorie> findAllByEquipmentId(@Param("equipment_id")String equipmentId);
}
