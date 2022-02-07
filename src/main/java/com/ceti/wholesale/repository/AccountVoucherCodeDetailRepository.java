package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.AccountVoucherCode;
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
public class AccountVoucherCodeDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<AccountVoucherCode> findAll(Pageable pageable, MultiValueMap<String, String> where) {
        Session session = (Session) entityManager.getDelegate();
        Map<String, Object> queryTypes = new HashMap<>();
        List<String> equalType = new ArrayList<>(Arrays.asList("old_voucher_id","active", "factory_id"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Arrays.asList("update_at","voucher_at"));
        queryTypes.put("dateType", dateType);
        List<String> likeType = new ArrayList<>(Arrays.asList("acc_no"));
        queryTypes.put("likeType", likeType);
        List<String> inType = new ArrayList<>(Arrays.asList("group_code"));
        queryTypes.put("inType", inType);

        return SqlProcessV2.getResultPage(session, "account_voucher_code", AccountVoucherCode.class, pageable, where, queryTypes);
    }
}
