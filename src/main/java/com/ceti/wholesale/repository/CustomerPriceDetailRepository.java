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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.CustomerPrice;

@Repository
public class CustomerPriceDetailRepository {

    @Value(value = "${ceti.service.zoneId}")
    private String zoneId;

    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<CustomerPrice> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();

        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Collections.singletonList("customer_name"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("customer_id", "group_id", "update_date", "factory_id"
            ,"customer_category"));
        queryTypes.put("equalType", equalType);

        return SqlProcessV2.getResultPage(session, "customer_price", CustomerPrice.class, pageable, where, queryTypes);
    }

}
