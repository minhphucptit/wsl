package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.DeliveryVoucher;
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
public class DeliveryVoucherDetailRepositoryTest {
    @TestConfiguration
    public static class DeliveryVoucherDetailRepositoryTestConfiguration{
        @Bean
        DeliveryVoucherDetailRepository deliveryVoucherDetailRepository(){
            return new DeliveryVoucherDetailRepository();
        }
    }
    @Autowired
    private DeliveryVoucherDetailRepository deliveryVoucherDetailRepository;
    @Autowired
    private DeliveryVoucherRepository deliveryVoucherRepository;
    @Test
    public void testFillAllWithFilter()throws Exception{
        Instant instant= Instant.now();
        Pageable pageable= PageRequest.of(0,20);
        DeliveryVoucher deliveryVoucher= new DeliveryVoucher();
        deliveryVoucher.setId("id");
        deliveryVoucher.setVoucherAt(instant);
        deliveryVoucher.setCompanyId("comId");
        deliveryVoucher.setCreatedByFullName("bien");
        deliveryVoucher.setTruckDriverId("TX01");
        deliveryVoucher.setTruckLicensePlateNumber("29A08723");
        deliveryVoucher.setUpdateByFullName("bien");
        deliveryVoucher.setNo("no");
        deliveryVoucher.setVoucherCode("vcode");
        deliveryVoucher.setCreatedAt(instant);
        deliveryVoucher.setUpdateAt(instant);

        deliveryVoucherRepository.save(deliveryVoucher);
        MultiValueMap<String,String> where = new LinkedMultiValueMap<>();
        where.add("company_id","comId");
        ResultPage<Object[]> rs =deliveryVoucherDetailRepository.findAllWithFilter(pageable,where);
        Object[] returnObject = (Object[]) rs.getPageList().get(0);
        DeliveryVoucher returnDeliveryVoucher = (DeliveryVoucher) returnObject[0];
        assertEquals(deliveryVoucher.getCompanyId(),returnDeliveryVoucher.getCompanyId());

    }
}
