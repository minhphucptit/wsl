package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.Brand;
import com.ceti.wholesale.model.Color;
import com.ceti.wholesale.model.CustomerProductDiscount;
import com.ceti.wholesale.model.CustomerProductPrice;
import com.ceti.wholesale.model.Product;
import com.ceti.wholesale.model.ProductCategory;
import com.ceti.wholesale.model.ProductType;
import com.ceti.wholesale.model.ProductUnit;
import com.ceti.wholesale.model.Factory;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.LongType;
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
public class ProductDetailRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Product> findAllProduct(Pageable pageable, MultiValueMap<String, String> where) {
        Session session = (Session) entityManager.getDelegate();

        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("id", "name"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("unit", "purpose", "type",  "is_active", "factory_id","brand_id","weight","cylinder_category"));
        queryTypes.put("equalType", equalType);
        List<String> searchFields = new ArrayList<>(
                Arrays.asList("id", "name"));
        Map<String, List<String>> searchType = new HashMap<>();
        searchType.put("search", searchFields);
        queryTypes.put("searchType", searchType);

        return SqlProcessV2.getResultPage(session, "product", Product.class, pageable, where, queryTypes);
    }


    public ResultPage<Object[]> findAll(Pageable pageable, MultiValueMap<String, String> where) {
        Session session = (Session) entityManager.getDelegate();

        String selectQuery = "select {product.*}, {product_category.*} , {brand.*}, {product_type.*}, {color.*},{factory.*},{product_unit.*}";
        String fromQuery = "from product left join product_category on product.category_id = product_category.id"+
                " left join brand on product.brand_id = brand.id"+
                " left join product_type on product.type = product_type.id"+
                " left join color on product.color_id = color.id" +
                " left join factory on product.factory_id = factory.id" +
                " left join product_unit on product.unit = product_unit.id";

        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("id", "name"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("unit", "category_id", "purpose", "type",  "is_active", "factory_id","customer_product_price.customer_id","brand_id","cylinder_category","weight"));
        queryTypes.put("equalType", equalType);

        List<String> searchFields = new ArrayList<>(
                Arrays.asList("id", "name"));
        Map<String, List<String>> searchType = new HashMap<>();
        searchType.put("search", searchFields);
        queryTypes.put("searchType", searchType);

        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("product", Product.class);
        allowEntities.put("product_category", ProductCategory.class);
        allowEntities.put("brand", Brand.class);
        allowEntities.put("product_type", ProductType.class);
        allowEntities.put("color", Color.class);
        allowEntities.put("factory", Factory.class);
        allowEntities.put("product_unit", ProductUnit.class);
        List<String> embedTables = new ArrayList<>(Arrays.asList("product_category","brand","product_type","color","factory","product_unit"));

        return SqlProcessV2.getResultPageWithEmbed(selectQuery, fromQuery, session, "product", allowEntities, pageable,
                embedTables, where, queryTypes);

    }

    public ResultPage<Object[]> findAllWithEmbed(Pageable pageable, MultiValueMap<String, String> where, List<String> listEmbedTable) {

        Session session = (Session) entityManager.getDelegate();


        String selectQuery = "select {product.*},{product_category.*}";
        String fromQuery = " from product left join product_category on product.category_id = product_category.id ";

        if(listEmbedTable.contains("customer_product_price")){
            selectQuery+=", {customer_product_price.*}";
            fromQuery+=" left join (select * from customer_product_price where customer_product_price.customer_id = :customer_id ) " +
                    "customer_product_price on product.id = customer_product_price.product_id and product.factory_id = customer_product_price.factory_id";
        }
        if(listEmbedTable.contains("customer_product_discount")){
            selectQuery+=", {customer_product_discount.*}";
            fromQuery+=" left join (select * from customer_product_discount where customer_product_discount.customer_id =:customer_id) " +
                    "customer_product_discount on product.id = customer_product_discount.product_id and product.factory_id = customer_product_discount.factory_id";
        }

        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("id", "name"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("unit", "category_id", "purpose", "type",  "is_active", "factory_id"));
        queryTypes.put("equalType", equalType);
         List<String> searchFields = new ArrayList<>(
                Arrays.asList("id", "name"));
        Map<String, List<String>> searchType = new HashMap<>();
        searchType.put("search", searchFields);
        queryTypes.put("searchType", searchType);

        ResultPage<Object[]> rs = new ResultPage<Object[]>();

        // Get select, from, page sql
        String page_ = PageableProcess.PageToSqlQuery(pageable, "product");
        String where_ = SqlProcessV2.getWhereSql("product", where, queryTypes);

        NativeQuery<Object[]> query = session.createSQLQuery(selectQuery + " " + fromQuery + where_ + page_);
        SqlProcessV2.setParams(query, where, queryTypes);
        if(listEmbedTable.contains("customer_product_price") || listEmbedTable.contains("customer_product_discount")){
            query.setParameter("customer_id",where.getFirst("customer_id"));
        }

        query.addEntity("product", Product.class);
        query.addEntity("product_category", ProductCategory.class);
        if(listEmbedTable.contains("customer_product_price")){
            query.addEntity("customer_product_price", CustomerProductPrice.class);
        }
        if(listEmbedTable.contains("customer_product_discount")){
            query.addEntity("customer_product_discount", CustomerProductDiscount.class);
        }
        rs.setPageList(query.getResultList());

        NativeQuery<Long> queryCount = session.createSQLQuery("select count (*) as total_items " + fromQuery + where_);
        SqlProcessV2.setParams(queryCount, where, queryTypes);
        if(listEmbedTable.contains("customer_product_price") || listEmbedTable.contains("customer_product_discount")){
        	queryCount.setParameter("customer_id",where.getFirst("customer_id"));
        }

        queryCount.addScalar("total_items", LongType.INSTANCE);
        rs.setTotalItems(queryCount.getResultList().get(0));
        return rs;
    }


}
