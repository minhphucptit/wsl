package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.FactoryRotationVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FactoryRotationVoucherRepository extends JpaRepository<FactoryRotationVoucher, String> {
    @Query(value = "select count(*) from factory_rotation_voucher where no like %:no_sample%", nativeQuery = true)
    Integer countFactoryRotationVoucherInDay(@Param("no_sample") String noSample);

}
