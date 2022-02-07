package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.CustomerCategory;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.Region;
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
public class CustomerDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Customer> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {
        Session session = (Session) entityManager.getDelegate();
        return SqlProcessV2.getResultPage(session, "customer", Customer.class, pageable, where, getQueryTypes());
    }
    public ResultPage<Object[]> findAllWithEmbed(Pageable pageable, MultiValueMap<String, String> where) {
        Session session = (Session) entityManager.getDelegate();
        String selectQuery= "select {customer.*}, {factory.*}, {region.*}, {customer_category.*}";
        String fromQuery = "from customer left join factory on customer.factory_id = factory.id"+
                " left join region on customer.region_id = region.id"+
                " left join customer_category on customer.category = customer_category.id";
        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("customer", Customer.class);
        allowEntities.put("factory", Factory.class);
        allowEntities.put("region", Region.class);
        allowEntities.put("customer_category", CustomerCategory.class);
        List<String> embedTables = new ArrayList<>(Arrays.asList("factory","region","customer_category"));
        return SqlProcessV2.getResultPageWithEmbed(selectQuery,fromQuery,session,"customer",allowEntities,pageable,embedTables,where,getQueryTypes());
    }

    public Map<String, Object> getQueryTypes(){
        Map<String, Object> queryTypes = new HashMap<>();
        queryTypes.put("likeType", new ArrayList<>(Arrays.asList("id", "name", "address", "phone_number", "tax_code", "code")));
        queryTypes.put("equalType", new ArrayList<>(Arrays.asList("category", "group_id", "company_id", "group_name", "factory_id", "is_active","region_id")));
        List<String> inType = new ArrayList<String>(Arrays.asList("category_in"));
        queryTypes.put("inType", inType);
        Map<String, List<String>> searchType = new HashMap<>();
        searchType.put("search", new ArrayList<>(Arrays.asList("code", "name")));
        queryTypes.put("searchType",searchType);
        return queryTypes;
    }


}
