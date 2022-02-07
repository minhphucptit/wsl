package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.TruckDriver;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TruckDriverDetailRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithEmbed(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();


        String selectQuery = "select {truck_driver.*}, {factory.*} ";
        String fromQuery = "from truck_driver left join factory on truck_driver.factory_id = factory.id";


        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("abbreviated_name", "factory.name", "phone_number", "full_name","truck_license_plate_number"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("is_active", "factory_id"));
        queryTypes.put("equalType", equalType);

        List<String> searchFields = new ArrayList<>(Arrays.asList("abbreviated_name", "full_name"));
        Map<String, List<String>> searchType = new HashMap<>();
        searchType.put("search", searchFields);
        queryTypes.put("searchType", searchType);

        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("truck_driver", TruckDriver.class);
        allowEntities.put("factory", Factory.class);

        List<String> embedTables = new ArrayList<>(Collections.singletonList("factory"));
        return SqlProcessV2.getResultPageWithEmbed(selectQuery, fromQuery, session, "truck_driver", allowEntities, pageable,
                embedTables, where, queryTypes);
    }

    public ResultPage<TruckDriver> findAll(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();


        String selectQuery = "select {truck_driver.*} ";
        String fromQuery = "from truck_driver ";


        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("abbreviated_name", "phone_number", "full_name","truck_license_plate_number"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("is_active", "factory_id"));
        queryTypes.put("equalType", equalType);

        List<String> searchFields = new ArrayList<>(Arrays.asList("abbreviated_name", "full_name"));
        Map<String, List<String>> searchType = new HashMap<>();
        searchType.put("search", searchFields);
        queryTypes.put("searchType", searchType);


        return SqlProcessV2.getResultPage(selectQuery, fromQuery, session, "truck_driver", TruckDriver.class, pageable,
                where, queryTypes);

    }
}
