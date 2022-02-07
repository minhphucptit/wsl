package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.PaymentVoucher;
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
public class PaymentVoucherDetailRepositoryTest {
    @TestConfiguration
    public static class PaymentVoucherDetailRepositoryTestConfiguration{
        @Bean
        PaymentVoucherDetailRepository paymentVoucherDetailRepository(){
            return new PaymentVoucherDetailRepository();
        }
    }
    @Autowired
    private PaymentVoucherDetailRepository paymentVoucherDetailRepository;
    @Autowired
    private PaymentVoucherRepository paymentVoucherRepository;

    @Test
    public void testFillAllWithFilter()throws Exception{
        Instant instant= Instant.now();
        Pageable pageable= PageRequest.of(0,20);
        PaymentVoucher paymentVoucher= new PaymentVoucher();
        paymentVoucher.setId("id");
        paymentVoucher.setVoucherId("voucherId");
        paymentVoucher.setCustomerId("customerId");
        paymentVoucher.setSoldVoucherNo("cvNo");
        paymentVoucher.setPayer("bien");
        paymentVoucher.setVoucherAt(instant);
        paymentVoucher.setSoldDeliveryVoucherNo("soulNo");
        paymentVoucher.setCreatedAt(instant);
        paymentVoucher.setUpdateByFullName("nhan");
        paymentVoucher.setCreatedByFullName("bien");
        paymentVoucher.setNo("no");
        paymentVoucher.setTotalPaymentReceived(BigDecimal.valueOf(10));
        paymentVoucher.setTotalGoodsReturn(BigDecimal.valueOf(12));
        paymentVoucher.setVoucherCode("vcode");

        paymentVoucherRepository.save(paymentVoucher);
        MultiValueMap<String,String> where = new LinkedMultiValueMap<>();
        ResultPage<Object[]> rs= paymentVoucherDetailRepository.findAllWithFilter(pageable,where);
        Object[] returnObject = (Object[]) rs.getPageList().get(0);
        PaymentVoucher returnPaymentVoucher =(PaymentVoucher) returnObject[0];
        assertEquals(paymentVoucher.getSoldVoucherNo(),returnPaymentVoucher.getSoldVoucherNo());
    }
}
