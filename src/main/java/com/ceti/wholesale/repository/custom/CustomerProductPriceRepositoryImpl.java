package com.ceti.wholesale.repository.custom;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.CustomerProductPrice;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.Product;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerProductPriceRepositoryImpl implements CustomerProductPriceRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ResultPage<CustomerProductPrice> findAll(Pageable pageable, MultiValueMap<String, String> where) {
        Session session = (Session) entityManager.getDelegate();
        Map<String, Object> queryTypes = new HashMap<>();
        List<String> equalType = new ArrayList<String>(Arrays.asList("customer_id", "product_id", "factory_id"));
        queryTypes.put("equalType", equalType);
        return SqlProcessV2.getResultPage(session,"customer_product_price",CustomerProductPrice.class,pageable,where,queryTypes);
    }

    @Override
    public ResultPage<Object[]> findAllWithEmbed(Pageable pageable, List<String> listEmbedTable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();
        String selectQuery = "select {customer_product_price.*}";
        String fromQuery = " from customer_product_price";
        if (listEmbedTable.contains("customer")) {
            selectQuery += ", {customer.*}";
            fromQuery += " left join customer on customer_product_price.customer_id = customer.id and customer_product_price.factory_id=customer.factory_id";
        }
        if (listEmbedTable.contains("product")) {
            selectQuery += ", {product.*}";
            fromQuery += " left join product on customer_product_price.product_id = product.id and customer_product_price.factory_id=product.factory_id";
        }
        if (listEmbedTable.contains("factory")) {
            selectQuery += ", {factory.*}";
            fromQuery += " left join factory on customer_product_price.factory_id = factory.id";
        }
        Map<String, Object> queryTypes = new HashMap<>();
        List<String> equalType = new ArrayList<String>(Arrays.asList("customer_id", "product_id", "factory_id"));
        queryTypes.put("equalType", equalType);
        HashMap<String, Class<?>> allowEntitys = new HashMap<String, Class<?>>();
        allowEntitys.put("customer_product_price",CustomerProductPrice.class);
        allowEntitys.put("customer", Customer.class);
        allowEntitys.put("product", Product.class);
        allowEntitys.put("factory", Factory.class);

        return SqlProcessV2.getResultPageWithEmbed(selectQuery,fromQuery,session,"customer_product_price",allowEntitys,pageable,listEmbedTable,where,queryTypes);
    }
}
