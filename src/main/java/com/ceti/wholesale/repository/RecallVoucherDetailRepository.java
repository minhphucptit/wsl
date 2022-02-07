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
public class RecallVoucherDetailRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();


        String selectQuery = "select {recall_voucher.*}, {truck_driver.*}, {truck.*}, {company.*} ";
        String fromQuery = "from recall_voucher left join truck_driver on recall_voucher.truck_driver_id = truck_driver.id "
                + "left join truck on recall_voucher.truck_license_plate_number = truck.license_plate_number "
                + "left join company on recall_voucher.company_id=company.id";


        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("no", "voucher_code", "note", "created_by_full_name", "update_by_full_name", "delivery_voucher_no"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("truck_driver_id", "truck_license_plate_number", "company_id", "factory_id"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Arrays.asList("created_at", "voucher_at", "update_at"));
        queryTypes.put("dateType", dateType);
        List<String> inRangeType = new ArrayList<>(
                Arrays.asList("total_goods_return", "total_payment_received"));
        queryTypes.put("inRangeType", inRangeType);

        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("recall_voucher", RecallVoucher.class);
        allowEntities.put("truck_driver", TruckDriver.class);
        allowEntities.put("truck", Truck.class);
        allowEntities.put("company", Company.class);

        List<String> embedTables = new ArrayList<>(Arrays.asList("truck_driver", "truck", "company"));

        return SqlProcessV2.getResultPageWithEmbed(selectQuery, fromQuery, session, "recall_voucher", allowEntities, pageable,
                embedTables, where, queryTypes);
    }
}
