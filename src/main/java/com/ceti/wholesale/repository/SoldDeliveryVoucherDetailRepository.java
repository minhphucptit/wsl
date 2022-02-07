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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SoldDeliveryVoucherDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();

        String selectQuery = "select distinct {sold_delivery_voucher.*}, {company.*}, {customer.*}, {truck_driver.*}, {truck.*}, {salesman.*}";
        String fromQuery = "from sold_delivery_voucher left join company on sold_delivery_voucher.company_id = company.id left join "
                + "customer on sold_delivery_voucher.customer_id = customer.id left join truck_driver on sold_delivery_voucher"
                + ".truck_driver_id = truck_driver.id left join truck on sold_delivery_voucher.truck_license_plate_number = truck.license_plate_number"
                + " left join salesman on sold_delivery_voucher.salesman_id = salesman.id"
                + " left join goods_in_out on goods_in_out.voucher_id = sold_delivery_voucher.id";

        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("no", "voucher_code", "note"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("company_id", "customer_id", "truck_driver_id", "truck_license_plate_number", "delivery_voucher_no", "salesman_id", "factory_id", "goods_in_out.type"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Arrays.asList("created_at", "update_at", "voucher_at"));
        queryTypes.put("dateType", dateType);
        List<String> inRangeType = new ArrayList<>(
                Arrays.asList("total_goods", "total_receivable", "total_payment_received", "total_goods_return"));
        queryTypes.put("inRangeType", inRangeType);

        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("sold_delivery_voucher", SoldDeliveryVoucher.class);
        allowEntities.put("company", Company.class);
        allowEntities.put("customer", Customer.class);
        allowEntities.put("truck_driver", TruckDriver.class);
        allowEntities.put("truck", Truck.class);
        allowEntities.put("salesman", Salesman.class);

        List<String> embedTables = new ArrayList<>(Arrays.asList("company", "customer", "truck_driver", "truck", "salesman"));

        return SqlProcessV2.getResultPageColumnWithEmbed(selectQuery, fromQuery, session, "sold_delivery_voucher", allowEntities, pageable,
                embedTables, where, queryTypes, "id");
    }
}
