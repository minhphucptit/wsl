package com.ceti.wholesale.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.TruckMonthlyFinalDo;

@Repository
public class TruckMonthlyFinalDoDetailRepository {
	
    @PersistenceContext
    private EntityManager entityManager;
    
    public ResultPage<TruckMonthlyFinalDo> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();

        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Collections.singletonList("truck_license_plate_number"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("month", "year", "factory_id"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Collections.emptyList());
        queryTypes.put("dateType", dateType);

        return SqlProcessV2.getResultPage(session, "truck_monthly_final_do", TruckMonthlyFinalDo.class, pageable, where, queryTypes);
    }

}
