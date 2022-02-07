package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.FactoryRotationVoucher;
import com.ceti.wholesale.model.Truck;
import com.ceti.wholesale.model.TruckDriver;
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
public class FactoryRotationVoucherDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where) {

        Session session = (Session) entityManager.getDelegate();

        String selectQuery = "select {factory_rotation_voucher.*}, {truck_driver.*}, {truck.*} ";
        String fromQuery = "from factory_rotation_voucher" +
                " left join truck_driver on factory_rotation_voucher.truck_driver_id = truck_driver.id" +
                " left join truck on factory_rotation_voucher.truck_license_plate_number = truck.license_plate_number";
        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("no", "voucher_code", "note"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("company_import_id", "company_export_id", "truck_driver_id",
                "truck_license_plate_number", "factory_id"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Arrays.asList("created_at", "update_at", "voucher_at"));
        queryTypes.put("dateType", dateType);
        List<String> inRangeType = new ArrayList<>(
                Arrays.asList("total_goods", "total_payment"));
        queryTypes.put("inRangeType", inRangeType);

        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("factory_rotation_voucher", FactoryRotationVoucher.class);
        allowEntities.put("truck_driver", TruckDriver.class);
        allowEntities.put("truck", Truck.class);

        List<String> embedTables = new ArrayList<>(Arrays.asList("truck_driver", "truck"));

        return SqlProcessV2.getResultPageWithEmbed(selectQuery, fromQuery, session, "factory_rotation_voucher", allowEntities, pageable,
                embedTables, where, queryTypes);
    }
}
