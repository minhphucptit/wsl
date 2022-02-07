package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.FactoryExportVoucher;
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
public class FactoryExportVoucherDetailRepositoryTest {
    @TestConfiguration
    public static class FactoryExportVoucherDetailRepositoryTestConfiguration{
        @Bean
        FactoryExportVoucherDetailRepository factoryExportVoucherDetailRepository(){
            return new FactoryExportVoucherDetailRepository();
        }
    }
    @Autowired
    private FactoryExportVoucherDetailRepository factoryExportVoucherDetailRepository;
    @Autowired
    private FactoryExportVoucherRepository factoryExportVoucherRepository;
    @Test
    public void testFillAllWithFilter() throws Exception{
        Instant instant= Instant.now();
        Pageable pageable= PageRequest.of(0,20);
        FactoryExportVoucher factoryExportVoucher = new FactoryExportVoucher();
        factoryExportVoucher.setId("id");
        factoryExportVoucher.setCompanyId("comId");
        factoryExportVoucher.setCustomerId("customerId");
        factoryExportVoucher.setTruckLicensePlateNumber("30A9321");
        factoryExportVoucher.setTruckDriverId("TX02");
        factoryExportVoucher.setVoucherAt(instant);
        factoryExportVoucher.setCreatedAt(instant);
        factoryExportVoucher.setVoucherCode("vcode");
        factoryExportVoucher.setCreatedByFullName("bien");
        factoryExportVoucher.setUpdateByFullName("bien");
        factoryExportVoucher.setSalesmanId("NV01");
        factoryExportVoucher.setNo("no");

        factoryExportVoucherRepository.save(factoryExportVoucher);
        MultiValueMap<String,String> where = new LinkedMultiValueMap<>();
        where.add("customer_id","customerId");
        ResultPage<Object[]> resultPage= factoryExportVoucherDetailRepository.findAllWithFilter(pageable,where);
        Object[] returnObject = (Object[]) resultPage.getPageList().get(0);
        FactoryExportVoucher returnFactoryExportVoucher = (FactoryExportVoucher) returnObject[0];
        assertEquals(factoryExportVoucher.getCompanyId(),returnFactoryExportVoucher.getCompanyId());
    }
}
