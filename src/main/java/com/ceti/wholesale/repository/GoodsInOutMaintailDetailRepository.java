package com.ceti.wholesale.repository;

import com.ceti.wholesale.model.GoodsInOutMaintainDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsInOutMaintailDetailRepository extends JpaRepository<GoodsInOutMaintainDetail, String> {

}
