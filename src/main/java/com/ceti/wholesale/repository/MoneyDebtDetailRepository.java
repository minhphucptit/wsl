package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.BeginningMoneyDebt;
import com.ceti.wholesale.model.Customer;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
public class MoneyDebtDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();

        String selectQuery = "select {beginning_money_debt.*}, {customer.*} ";
        String fromQuery = "from beginning_money_debt left join customer on beginning_money_debt.customer_id = customer.id";

        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("note", "update_by", "create_by"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("id", "customer_id", "year", "factory_id"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Arrays.asList("created_at", "update_at"));
        queryTypes.put("dateType", dateType);
        List<String> inRangeType = new ArrayList<>(
                Collections.emptyList());
        queryTypes.put("inRangeType", inRangeType);

        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("beginning_money_debt", BeginningMoneyDebt.class);
        allowEntities.put("customer", Customer.class);

        List<String> embedTables = new ArrayList<>(Collections.singletonList("customer"));

        return SqlProcessV2.getResultPageWithEmbed(selectQuery, fromQuery, session, "beginning_money_debt", allowEntities, pageable,
                embedTables, where, queryTypes);
    }
}
