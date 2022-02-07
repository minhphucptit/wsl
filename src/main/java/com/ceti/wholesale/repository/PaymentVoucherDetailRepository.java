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
public class PaymentVoucherDetailRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();

        String selectQuery = "select {payment_voucher.*},{customer.*}, {truck_driver.*}, {company.*}";
        String fromQuery = "from payment_voucher left join customer on payment_voucher.customer_id = customer.id " +
                "left join truck_driver on payment_voucher.truck_driver_id = truck_driver.id " +
                "left join company on payment_voucher.company_id = company.id";

        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("no", "voucher_code", "note"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("customer_id", "sold_voucher_no", "sold_delivery_voucher_no", "factory_id"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Arrays.asList("created_at", "update_at", "voucher_at"));
        queryTypes.put("dateType", dateType);
        List<String> inRangeType = new ArrayList<>(
                Arrays.asList("total_receivable", "total_goods_return"));
            queryTypes.put("inRangeType", inRangeType);
        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("payment_voucher", PaymentVoucher.class);
        allowEntities.put("customer", Customer.class);
        allowEntities.put("truck_driver", TruckDriver.class);
        allowEntities.put("company", Company.class);

        List<String> embedTables = new ArrayList<>(Arrays.asList("customer", "truck_driver", "company"));

        return SqlProcessV2.getResultPageWithEmbed(selectQuery, fromQuery, session, "payment_voucher", allowEntities, pageable,
                embedTables, where, queryTypes);
    }
}
