package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.RecallVoucher;
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
public class RecallVoucherDetailRepositoryTest {
    @TestConfiguration
    public static class RecallVoucherDetailRepositoryTestConfiguration{
        @Bean
        RecallVoucherDetailRepository recallVoucherDetailRepository(){
            return new RecallVoucherDetailRepository();
        }
    }
    @Autowired
    private RecallVoucherDetailRepository recallVoucherDetailRepository;
    @Autowired
    private RecallVoucherRepository recallVoucherRepository;

    @Test
    public void testFillAllWithFilter()throws Exception{
        Pageable pageable= PageRequest.of(0,20);
        Instant instant= Instant.now();
        RecallVoucher recallVoucher= new RecallVoucher();
        recallVoucher.setId("id");
        recallVoucher.setCompanyId("companyId");
        recallVoucher.setNo("no");
        recallVoucher.setDeliveryVoucherId("dvId");
        recallVoucher.setTruckLicensePlateNumber("30A52121");
        recallVoucher.setTruckDriverId("TX01");
        recallVoucher.setTotalGoodsReturn(BigDecimal.valueOf(10));
        recallVoucher.setUpdateByFullName("bien");
        recallVoucher.setDeliveryVoucherNo("deliNo");
        recallVoucher.setVoucherCode("vcode");
        recallVoucher.setCreatedAt(instant);
        recallVoucher.setVoucherAt(instant);
        recallVoucher.setUpdateAt(instant);
        recallVoucher.setCreatedByFullName("bien");

        recallVoucherRepository.save(recallVoucher);
        MultiValueMap<String,String> where = new LinkedMultiValueMap<>();
        where.add("company_id","companyId");
        ResultPage<Object[]> rs= recallVoucherDetailRepository.findAllWithFilter(pageable,where);
        Object[] returnObject = (Object[]) rs.getPageList().get(0);
        RecallVoucher returnRecallVoucher= (RecallVoucher) returnObject[0];
        assertEquals(recallVoucher.getTruckDriverId(),returnRecallVoucher.getTruckDriverId());
    }
}
