package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.GoodsInStock;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
public class GoodsInStockDetailRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<GoodsInStock> findAllWithFilter(MultiValueMap<String, String> where, Pageable pageable) {

        Session session = (Session) entityManager.getDelegate();

        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("id", "year", "note"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("company_id", "product_id", "factory_id"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Arrays.asList("create_at", "update_at"));
        queryTypes.put("dateType", dateType);
        List<String> inRangeType = new ArrayList<>(
                Collections.singletonList("inventory"));
        queryTypes.put("inRangeType", inRangeType);

        return SqlProcessV2.getResultPage(session, "goods_in_stock", GoodsInStock.class, pageable, where, queryTypes);
    }
}
