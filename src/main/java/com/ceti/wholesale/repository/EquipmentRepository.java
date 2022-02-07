package com.ceti.wholesale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ceti.wholesale.model.Equipment;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment,String> {

}
