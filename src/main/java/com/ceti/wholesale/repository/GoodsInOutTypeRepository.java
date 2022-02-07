package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.GoodsInOutType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsInOutTypeRepository extends JpaRepository<GoodsInOutType, String> {
    @Query(value = "SELECT * from goods_in_out_type g WHERE g.code like %:code% ", nativeQuery = true)
    List<GoodsInOutType> findAllByCode(String code);

}
