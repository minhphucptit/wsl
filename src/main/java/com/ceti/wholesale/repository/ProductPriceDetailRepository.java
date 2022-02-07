package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.Product;
import com.ceti.wholesale.model.ProductPrice;
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
public class ProductPriceDetailRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();

        String selectQuery = "select {product_price.*},{product.*}";
        String fromQuery = "from product_price left join product on product_price.product_id = product.id and product_price.factory_id = product.factory_id";

        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>();
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("is_active", "product_id", "factory_id"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Arrays.asList("update_at"));
        queryTypes.put("dateType", dateType);
        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("product_price", ProductPrice.class);
        allowEntities.put("product", Product.class);

        List<String> embedTables = new ArrayList<>(Collections.singletonList("product"));

        return SqlProcessV2.getResultPageWithEmbed(selectQuery, fromQuery, session, "product_price", allowEntities, pageable,
                embedTables, where, queryTypes);
    }
}
