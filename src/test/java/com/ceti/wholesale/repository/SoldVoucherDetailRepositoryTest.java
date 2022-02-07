//package com.ceti.wholesale.repository;
//
//import com.ceti.wholesale.common.constant.ResultPage;
//import com.ceti.wholesale.model.SoldVoucher;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//
//import static org.junit.Assert.assertEquals;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("test")
//public class SoldVoucherDetailRepositoryTest {
//    @TestConfiguration
//    public static class SoldVoucherDetailRepositoryTestConfiguration{
//        @Bean
//        SoldVoucherDetailRepository soldVoucherDetailRepository(){
//            return new SoldVoucherDetailRepository();
//        }
//    }
//    @Autowired
//    private SoldVoucherDetailRepository soldVoucherDetailRepository;
//    @Autowired
//    private SoldVoucherRepository soldVoucherRepository;
//
//    @Test
//    public void testConfiguration() throws Exception{
//        Pageable pageable= PageRequest.of(0,20);
//        Instant instant=Instant.now();
//        SoldVoucher soldVoucher= new SoldVoucher();
//        soldVoucher.setId("id");
//        soldVoucher.setCompanyId("companyId");
//        soldVoucher.setSalesmanId("salesmanId");
//        soldVoucher.setCustomerId("customerId");
//        soldVoucher.setTruckDriverId("TX01");
//        soldVoucher.setVoucherAt(instant);
//        soldVoucher.setVoucherCode("vcode");
//        soldVoucher.setNo("no");
//        soldVoucher.setCreatedAt(instant);
//        soldVoucher.setCreatedByFullName("bien");
//        soldVoucher.setUpdateAt(instant);
//        soldVoucher.setUpdateByFullName("bien");
//        soldVoucher.setTruckLicensePlateNumber("30A2364");
//        soldVoucher.setTotalReceivable(BigDecimal.valueOf(10));
//        soldVoucher.setTotalGoods(BigDecimal.valueOf(10));
//        soldVoucher.setTotalGoodsReturn(BigDecimal.valueOf(10));
//        soldVoucher.setTotalPaymentReceived(BigDecimal.valueOf(10));
//
//        soldVoucherRepository.save(soldVoucher);
//        MultiValueMap<String,String> where = new LinkedMultiValueMap<>();
//        where.add("company_id","companyId");
//        ResultPage<Object[]> rs = soldVoucherDetailRepository.findAllWithFilter(pageable,where);
//        Object[] returnObject = (Object[]) rs.getPageList().get(0);
//        SoldVoucher returnSoldVoucher = (SoldVoucher) returnObject[0];
//        assertEquals(soldVoucher.getCustomerId(),returnSoldVoucher.getCustomerId());
//    }
//}
