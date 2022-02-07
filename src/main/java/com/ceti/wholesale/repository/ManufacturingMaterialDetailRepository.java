package com.ceti.wholesale.repository;

import com.ceti.wholesale.dto.ManufacturingMaterialDto;
import com.ceti.wholesale.model.ManufacturingMaterial;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ManufacturingMaterialDetailRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ManufacturingMaterial> findAllWithFilter(String id) {

        Session session = (Session) entityManager.getDelegate();

        String queryString = "SELECT {manufacturing_material.*} "
                +"FROM manufacturing_material " +
                "where manufacturing_io_voucher_id=:manufacturing_io_voucher_id";

        NativeQuery<ManufacturingMaterial> query = session.createSQLQuery(queryString);
        query.setParameter("manufacturing_io_voucher_id", id);
        query.addEntity("manufacturing_material", ManufacturingMaterial.class);

        return query.getResultList();
    }

}
