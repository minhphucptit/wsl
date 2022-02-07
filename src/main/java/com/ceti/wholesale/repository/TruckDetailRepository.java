package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.Truck;
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
public class TruckDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithEmbed(Pageable pageable, MultiValueMap<String, String> where) {
        Session session = (Session) entityManager.getDelegate();
        String selectQuery= "select {truck.*}, {factory.*}";
        String fromQuery = "from truck left join factory on truck.factory_id = factory.id";
        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("truck", Truck.class);
        allowEntities.put("factory", Factory.class);
        List<String> embedTables = new ArrayList<>(Arrays.asList("factory"));
        return SqlProcessV2.getResultPageWithEmbed(selectQuery,fromQuery,session,"truck",allowEntities,pageable,embedTables,where,getQueryTypes());
    }

    public Map<String, Object> getQueryTypes(){
        Map<String, Object> queryTypes = new HashMap<>();
        queryTypes.put("equalType", new ArrayList<>(Arrays.asList("factory_id", "is_active")));
        queryTypes.put("likeType", new ArrayList<>(Arrays.asList("license_plate_number")));
        return queryTypes;
    }
}
