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
public class FactoryImportVoucherDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();

        String selectQuery = "select distinct {factory_import_voucher.*},{company.*},{customer.*}, {truck_driver.*}, {truck.*}, {salesman"
                + ".*} ";
        String fromQuery = "from factory_import_voucher left join company on factory_import_voucher.company_id = company.id left join "
                + "customer on factory_import_voucher.customer_id = customer.id left join truck_driver on factory_import_voucher"
                + ".truck_driver_id = truck_driver.id left join truck on factory_import_voucher.truck_license_plate_number = truck"
                + ".license_plate_number left join salesman on factory_import_voucher.salesman_id = salesman.id"
                + " left join goods_in_out on goods_in_out.voucher_id = factory_import_voucher.id";
        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<String>(Arrays.asList("no", "voucher_code", "note"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<String>(Arrays.asList("company_id", "customer_id", "truck_driver_id", "truck_license_plate_number", "salesman_id", "factory_id", "goods_in_out.type"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<String>(Arrays.asList("created_at", "update_at", "voucher_at"));
        queryTypes.put("dateType", dateType);
        List<String> inRangeType = new ArrayList<String>(
                Arrays.asList("total_goods", "total_payment"));
        queryTypes.put("inRangeType", inRangeType);

        HashMap<String, Class<?>> allowEntities = new HashMap<String, Class<?>>();
        allowEntities.put("factory_import_voucher", FactoryImportVoucher.class);
        allowEntities.put("company", Company.class);
        allowEntities.put("customer", Customer.class);
        allowEntities.put("truck_driver", TruckDriver.class);
        allowEntities.put("truck", Truck.class);
        allowEntities.put("salesman", Salesman.class);

        List<String> embedTables = new ArrayList<String>(Arrays.asList("company", "customer", "truck_driver", "truck"
                , "salesman"));

        return SqlProcessV2.getResultPageColumnWithEmbed(selectQuery, fromQuery, session, "factory_import_voucher", allowEntities, pageable,
                embedTables, where, queryTypes, "id");
    }

}
