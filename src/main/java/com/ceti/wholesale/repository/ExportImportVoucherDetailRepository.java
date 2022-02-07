package com.ceti.wholesale.repository;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.LongType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.common.util.SqlProcessV2;
import com.ceti.wholesale.model.Company;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.ExportVoucher;
import com.ceti.wholesale.model.ImportVoucher;
import com.ceti.wholesale.model.Salesman;
import com.ceti.wholesale.model.Truck;
import com.ceti.wholesale.model.TruckDriver;

@Repository
public class ExportImportVoucherDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public ResultPage<Object[]> findAllWithFilter(Pageable pageable, MultiValueMap<String, String> where, String factoryId){
        Session session = (Session) entityManager.getDelegate();

        String selectQuery = "select {import_voucher.*},{export_voucher.*}, {customer.*} ";
        String fromQuery = "from (select * from import_voucher where factory_id=:factory_id) import_voucher"
                + " full outer join (select * from export_voucher where factory_id=:factory_id) export_voucher on import_voucher.export_voucher_id = export_voucher.id" +
				" left join customer on export_voucher.customer_id = customer.id";

        Map<String, Object> queryTypes = new HashMap<>();
        List<String> likeType = new ArrayList<>(Arrays.asList("import_voucher.no", "export_voucher.no"));
        queryTypes.put("likeType", likeType);
        List<String> equalType = new ArrayList<>(Arrays.asList("export_voucher.customer_id"));
        queryTypes.put("equalType", equalType);
        List<String> dateType = new ArrayList<>(Arrays.asList("import_voucher.voucher_at", "export_voucher.voucher_at"));
        queryTypes.put("dateType", dateType);
        HashMap<String, Class<?>> allowEntities = new HashMap<>();
        allowEntities.put("import_voucher", ImportVoucher.class);
        allowEntities.put("export_voucher", ExportVoucher.class);
		allowEntities.put("customer", Customer.class);

		List<String> embedTables = new ArrayList<>(Arrays.asList("export_voucher", "customer"));
        
		ResultPage<Object[]> rs = new ResultPage<Object[]>();

		// Get select, from, page sql
		String page_ = PageableProcess.PageToSqlQuery(pageable, "import_voucher");
		String where_ = SqlProcessV2.getWhereSql("import_voucher", where, queryTypes);

		NativeQuery<Object[]> query = session.createSQLQuery(selectQuery + " " + fromQuery + where_ + page_);
		SqlProcessV2.setParams(query, where, queryTypes);
		query.setParameter("factory_id", factoryId);
		query.addEntity("import_voucher", allowEntities.get("import_voucher"));
		for (String embedTable : embedTables) {
			if (allowEntities.containsKey(embedTable)) {
				query.addEntity(embedTable, allowEntities.get(embedTable));
			}
		}

		rs.setPageList(query.getResultList());

		NativeQuery<Long> queryCount = session.createSQLQuery("select count (*) as total_items " + fromQuery + where_);
		SqlProcessV2.setParams(queryCount, where, queryTypes);
		queryCount.setParameter("factory_id", factoryId);

		queryCount.addScalar("total_items", LongType.INSTANCE);
		rs.setTotalItems(queryCount.getResultList().get(0));

		return rs;
    }
	
	public Object[] getDetailImportVoucher(String importVoucherId) {
        Session session = (Session) entityManager.getDelegate();

        String sql = "select {import_voucher.*},{company.*},{customer.*},{truck_driver.*},{truck.*},{salesman.*}"
        + " from import_voucher "
        + " left join company on import_voucher.company_id = company.id"
        + " left join customer on import_voucher.customer_id = customer.id"
        + " left join truck_driver on import_voucher.truck_driver_id = truck_driver.id"
        + " left join truck on import_voucher.truck_license_plate_number = truck.license_plate_number "
        + " left join salesman on import_voucher.salesman_id = salesman.id"
        + " where import_voucher.id=:import_voucher";

		NativeQuery<Object[]> query = session.createSQLQuery(sql);
		query.addEntity("import_voucher", ImportVoucher.class);
		query.addEntity("company", Company.class);
		query.addEntity("customer", Customer.class);
		query.addEntity("truck_driver", TruckDriver.class);
		query.addEntity("truck", Truck.class);
		query.addEntity("salesman", Salesman.class);
		query.setParameter("import_voucher", importVoucherId);

		return query.getSingleResult();
	}
	
	public Object[] getDetailExportVoucher(String exportVoucherId) {
        Session session = (Session) entityManager.getDelegate();

        String sql = "select {export_voucher.*},{company.*},{customer.*},{truck_driver.*},{truck.*},{salesman.*}"
        + " from export_voucher "
        + " left join company on export_voucher.company_id = company.id"
        + " left join customer on export_voucher.customer_id = customer.id"
        + " left join truck_driver on export_voucher.truck_driver_id = truck_driver.id"
        + " left join truck on export_voucher.truck_license_plate_number = truck.license_plate_number "
        + " left join salesman on export_voucher.salesman_id = salesman.id"
        + " where export_voucher.id=:export_voucher";

		NativeQuery<Object[]> query = session.createSQLQuery(sql);
		query.addEntity("export_voucher", ExportVoucher.class);
		query.addEntity("company", Company.class);
		query.addEntity("customer", Customer.class);
		query.addEntity("truck_driver", TruckDriver.class);
		query.addEntity("truck", Truck.class);
		query.addEntity("salesman", Salesman.class);
		query.setParameter("export_voucher", exportVoucherId);

		return query.getSingleResult();
	}
}
