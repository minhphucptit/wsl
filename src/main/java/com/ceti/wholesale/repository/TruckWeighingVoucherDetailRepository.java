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
public class TruckWeighingVoucherDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();

        String selectQuery = "select {truck_weighing_voucher.*},{company.*},{customer.*}, {truck_driver.*}, {truck.*}, "
        		+ "{product.*}, {account_voucher_code.*} ";
        String fromQuery = "from truck_weighing_voucher left join company on truck_weighing_voucher.company_id = company.id left join "
                + "customer on truck_weighing_voucher.customer_id = customer.id left join truck_driver on truck_weighing_voucher"
                + ".truck_driver_id = truck_driver.id left join truck on truck_weighing_voucher.truck_license_plate_number = truck"
                + ".license_plate_number left join product on truck_weighing_voucher.product_id = product.id and truck_weighing_voucher.factory_id = product.factory_id "
                + " left join account_voucher_code ON truck_weighing_voucher.id = account_voucher_code.voucher_id";
        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("no", "voucher_code", "note", "account_voucher_code.acc_no"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("company_id", "factory_id", "customer_id", "truck_driver_id", "truck_license_plate_number", "product_id", "status"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Arrays.asList("created_at", "update_at", "voucher_at"));
        queryTypes.put("dateType", dateType);
        List<String> inRangeType = new ArrayList<>(
                Arrays.asList("weighing_result_1", "weighing_result_2", "weighing_result_final"));
        queryTypes.put("inRangeType", inRangeType);

        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("truck_weighing_voucher", TruckWeighingVoucher.class);
        allowEntities.put("company", Company.class);
        allowEntities.put("customer", Customer.class);
        allowEntities.put("truck_driver", TruckDriver.class);
        allowEntities.put("truck", Truck.class);
        allowEntities.put("product", Product.class);
        allowEntities.put("account_voucher_code", AccountVoucherCode.class);

        List<String> embedTables = new ArrayList<>(Arrays.asList("company", "customer", "truck_driver", "truck"
                , "product", "account_voucher_code"));

        return SqlProcessV2.getResultPageWithEmbed(selectQuery, fromQuery, session, "truck_weighing_voucher", allowEntities, pageable,
                embedTables, where, queryTypes);
    }
}
