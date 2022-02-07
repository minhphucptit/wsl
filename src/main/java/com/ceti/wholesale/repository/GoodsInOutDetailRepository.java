package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.GoodsInOut;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
public class GoodsInOutDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<GoodsInOut> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();

        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Collections.singletonList("voucher_no"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("product_id", "voucher_code", "type", "is_main_product"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Collections.emptyList());
        queryTypes.put("dateType", dateType);
        List<String> inRangeType = new ArrayList<>(
                Arrays.asList("in", "out"));
        queryTypes.put("inRangeType", inRangeType);

        return SqlProcessV2.getResultPage(session, "goods_in_out", GoodsInOut.class, pageable, where, queryTypes);
    }
}
