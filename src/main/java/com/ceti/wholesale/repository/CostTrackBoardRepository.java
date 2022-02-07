package com.ceti.wholesale.repository;


import com.ceti.wholesale.model.CostTrackBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostTrackBoardRepository extends JpaRepository<CostTrackBoard, String> {

}
