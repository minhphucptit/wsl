package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.SoldDeliveryVoucher;
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
public class SoldDeliveryVoucherDetailRepositoryTest {
    @TestConfiguration
    public static class SoldDeliveryVoucherDetailRepositoryTestConfiguration{
        @Bean
        SoldDeliveryVoucherDetailRepository soldDeliveryVoucherDetailRepository(){
            return new SoldDeliveryVoucherDetailRepository();
        }
    }
    @Autowired
    private SoldDeliveryVoucherDetailRepository soldDeliveryVoucherDetailRepository;
    @Autowired
    private SoldDeliveryVoucherRepository soldDeliveryVoucherRepository;

    @Test
    public void testFillAllWithFilter()throws Exception{
        Pageable pageable= PageRequest.of(0,20);
        Instant instant= Instant.now();
        SoldDeliveryVoucher soldDeliveryVoucher= new SoldDeliveryVoucher();
        soldDeliveryVoucher.setId("id");
        soldDeliveryVoucher.setDeliveryVoucherId("deliVoucherId");
        soldDeliveryVoucher.setCustomerId("customerId");
        soldDeliveryVoucher.setCompanyId("companyId");
        soldDeliveryVoucher.setTruckDriverId("TX01");
        soldDeliveryVoucher.setTruckLicensePlateNumber("30A8212");
        soldDeliveryVoucher.setCreatedByFullName("bien");
        soldDeliveryVoucher.setUpdateByFullName("bien");
        soldDeliveryVoucher.setCreatedAt(instant);
        soldDeliveryVoucher.setVoucherAt(instant);
        soldDeliveryVoucher.setUpdateAt(instant);
        soldDeliveryVoucher.setVoucherCode("vcode");
        soldDeliveryVoucher.setNo("no");
        soldDeliveryVoucher.setTotalReceivable(BigDecimal.valueOf(10));
        soldDeliveryVoucher.setTotalGoods(BigDecimal.valueOf(1));
        soldDeliveryVoucher.setTotalGoodsReturn(BigDecimal.valueOf(5));
        soldDeliveryVoucher.setTotalPaymentReceived(BigDecimal.valueOf(10));
        soldDeliveryVoucher.setDeliveryVoucherNo("sad");

        soldDeliveryVoucherRepository.save(soldDeliveryVoucher);
        MultiValueMap<String,String>where= new LinkedMultiValueMap<>();
        where.add("no","no");
        ResultPage<Object[]> rs= soldDeliveryVoucherDetailRepository.findAllWithFilter(pageable,where);
        Object[] returnObject = (Object[]) rs.getPageList().get(0);
        SoldDeliveryVoucher returnSoldDeliveryVoucher = (SoldDeliveryVoucher) returnObject[0];
        assertEquals(soldDeliveryVoucher.getCustomerId(),returnSoldDeliveryVoucher.getCustomerId());
    }
}
