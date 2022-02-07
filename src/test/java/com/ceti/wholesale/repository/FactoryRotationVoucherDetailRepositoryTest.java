package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.FactoryRotationVoucher;
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

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class FactoryRotationVoucherDetailRepositoryTest {
    @TestConfiguration
    public static class FactoryRotationVoucherDetailRepositoryTestConfiguration{
        @Bean
        FactoryRotationVoucherDetailRepository factoryRotationVoucherDetailRepository(){
            return new FactoryRotationVoucherDetailRepository();
        }
    }
    @Autowired
    private FactoryRotationVoucherDetailRepository factoryRotationVoucherDetailRepository;
    @Autowired
    private FactoryRotationVoucherRepository factoryRotationVoucherRepository;

    @Test
    public void testFillAllWithFilter()throws Exception{
        Instant instant= Instant.now();
        Pageable pageable= PageRequest.of(0,20);
        FactoryRotationVoucher factoryRotationVoucher= new FactoryRotationVoucher();
        factoryRotationVoucher.setId("id");
        factoryRotationVoucher.setCompanyExportId("CEId");
        factoryRotationVoucher.setCompanyImportId("CIId");
        factoryRotationVoucher.setTruckLicensePlateNumber("30A0897");
        factoryRotationVoucher.setTruckDriverId("TX01");
        factoryRotationVoucher.setNo("no");
        factoryRotationVoucher.setUpdateByFullName("bien");
        factoryRotationVoucher.setCreatedByFullName("nhan");
        factoryRotationVoucher.setVoucherCode("vcode");
        factoryRotationVoucher.setVoucherAt(instant);
        factoryRotationVoucher.setUpdateAt(instant);
        factoryRotationVoucher.setTotalGoods(BigDecimal.valueOf(10));
        factoryRotationVoucher.setCompanyExportName("ceti");
        factoryRotationVoucher.setCompanyImportName("anpha");

        factoryRotationVoucherRepository.save(factoryRotationVoucher);
        MultiValueMap<String,String> where = new LinkedMultiValueMap<>();
        where.add("company_import_id","CIId");
        ResultPage<Object[]> rs= factoryRotationVoucherDetailRepository.findAllWithFilter(pageable,where);
        Object[] returnObject =(Object[]) rs.getPageList().get(0);
        FactoryRotationVoucher returnFactoryRotationVoucher= (FactoryRotationVoucher) returnObject[0];
        assertEquals(factoryRotationVoucher.getTruckDriverId(),returnFactoryRotationVoucher.getTruckDriverId());
    }
}
