package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.ManufacturingMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ManufacturingMaterialRepository extends JpaRepository<ManufacturingMaterial, String> {

    @Modifying
    @Transactional
    @Query(value ="DELETE manufacturing_material WHERE manufacturing_io_voucher_id= :id", nativeQuery = true)
    void deleteAllByManufacturingIoVoucherId(@Param("id") String id);

}
