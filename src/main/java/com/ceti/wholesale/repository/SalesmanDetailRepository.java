package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.Salesman;
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
public class SalesmanDetailRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithEmbed(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();


        String selectQuery = "select {salesman.*}, {factory.*} ";
        String fromQuery = "from salesman left join factory on salesman.factory_id = factory.id";


        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("abbreviated_name","id","address", "factory.name", "phone_number", "full_name"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("is_active", "factory_id"));
        queryTypes.put("equalType", equalType);

        List<String> searchFields = new ArrayList<>(Arrays.asList("abbreviated_name", "full_name"));
        Map<String, List<String>> searchType = new HashMap<>();
        searchType.put("search", searchFields);
        queryTypes.put("searchType", searchType);

        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("salesman", Salesman.class);
        allowEntities.put("factory", Factory.class);

        List<String> embedTables = new ArrayList<>(Collections.singletonList("factory"));
        return SqlProcessV2.getResultPageWithEmbed(selectQuery, fromQuery, session, "salesman", allowEntities, pageable,
                embedTables, where, queryTypes);
    }

    public ResultPage<Salesman> findAll(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();


        String selectQuery = "select {salesman.*} ";
        String fromQuery = "from salesman ";


        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("abbreviated_name","address","id", "phone_number", "full_name"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("is_active", "factory_id"));
        queryTypes.put("equalType", equalType);

        List<String> searchFields = new ArrayList<>(Arrays.asList("abbreviated_name", "full_name"));
        Map<String, List<String>> searchType = new HashMap<>();
        searchType.put("search", searchFields);
        queryTypes.put("searchType", searchType);


        return SqlProcessV2.getResultPage(selectQuery, fromQuery, session, "salesman", Salesman.class, pageable,
                where, queryTypes);

    }
}
