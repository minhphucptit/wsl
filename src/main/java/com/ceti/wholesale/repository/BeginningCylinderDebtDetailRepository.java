package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.*;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
public class BeginningCylinderDebtDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();


        String selectQuery = "select {beginning_cylinder_debt.*}, {customer.*}, {product.*} ";
        String fromQuery = "from beginning_cylinder_debt left join customer on beginning_cylinder_debt.customer_id = customer.id "
                + "left join product on beginning_cylinder_debt.product_id = product.id and beginning_cylinder_debt.factory_id = product.factory_id";


        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Collections.singletonList("note"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("customer_id", "product_id", "year", "factory_id"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Arrays.asList("create_at", "update_at"));
        queryTypes.put("dateType", dateType);
        List<String> inRangeType = new ArrayList<>(
                Collections.singletonList("inventory"));
        queryTypes.put("inRangeType", inRangeType);

        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("beginning_cylinder_debt", BeginningCylinderDebt.class);
        allowEntities.put("customer", Customer.class);
        allowEntities.put("product", Product.class);

        List<String> embedTables = new ArrayList<>(Arrays.asList("customer", "product"));

        return SqlProcessV2.getResultPageWithEmbed(selectQuery, fromQuery, session, "beginning_cylinder_debt", allowEntities, pageable,
                embedTables, where, queryTypes);
    }
}
