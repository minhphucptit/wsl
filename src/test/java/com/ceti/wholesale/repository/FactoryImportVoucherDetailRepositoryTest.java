package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.FactoryImportVoucher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class FactoryImportVoucherDetailRepositoryTest {
    @TestConfiguration
    public static class FactoryImportVoucherDetailRepositoryTestConfiguration{
        @Bean
        FactoryImportVoucherDetailRepository factoryImportVoucherDetailRepository(){
            return new FactoryImportVoucherDetailRepository();
        }
    }
    @Autowired
    private FactoryImportVoucherDetailRepository factoryImportVoucherDetailRepository;
    @Autowired
    private FactoryImportVoucherRepository factoryImportVoucherRepository;

    @Test
    public void testFillAllWithFilter()throws Exception{
        Instant instant= Instant.now();
        Pageable pageable= PageRequest.of(0,20);
        FactoryImportVoucher factoryImportVoucher= new FactoryImportVoucher();
        factoryImportVoucher.setId("id");
        factoryImportVoucher.setCompanyId("comId");
        factoryImportVoucher.setCustomerId("customerId");
        factoryImportVoucher.setTruckLicensePlateNumber("30A9321");
        factoryImportVoucher.setTruckDriverId("TX02");
        factoryImportVoucher.setVoucherAt(instant);
        factoryImportVoucher.setCreatedAt(instant);
        factoryImportVoucher.setVoucherCode("vcode");
        factoryImportVoucher.setCreatedByFullName("bien");
        factoryImportVoucher.setUpdateByFullName("bien");
        factoryImportVoucher.setSalesmanId("NV01");
        factoryImportVoucher.setNo("no");

        factoryImportVoucherRepository.save(factoryImportVoucher);
        MultiValueMap<String,String> where = new LinkedMultiValueMap<>();
        where.add("company_id","comId");
        ResultPage<Object[]> rs = factoryImportVoucherDetailRepository.findAllWithFilter(pageable,where);
        Object[] returnObject= (Object[]) rs.getPageList().get(0);
        FactoryImportVoucher returnFactoryImportVoucher = (FactoryImportVoucher) returnObject[0];
        assertEquals(factoryImportVoucher.getCustomerId(),returnFactoryImportVoucher.getCustomerId());
    }
}
