package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.TruckWeighingVoucher;
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
public class TruckWeighingVoucherDetailRepositoryTest {
    @TestConfiguration
    public static class TruckWeighingVoucherDetailRepositoryTestConfiguration{
        @Bean
        TruckWeighingVoucherDetailRepository truckWeighingVoucherDetailRepository(){
            return new TruckWeighingVoucherDetailRepository();
        }
    }
    @Autowired
    private TruckWeighingVoucherDetailRepository truckWeighingVoucherDetailRepository;
    @Autowired
    private TruckWeighingVoucherRepository truckWeighingVoucherRepository;

    @Test
    public void testFillAllWithFilter()throws Exception{
        Pageable pageable= PageRequest.of(0,20);
        Instant instant= Instant.now();
        TruckWeighingVoucher truckWeighingVoucher= new TruckWeighingVoucher();
        truckWeighingVoucher.setId("id");
        truckWeighingVoucher.setProductId("P01");
        truckWeighingVoucher.setCompanyId("CT01");
        truckWeighingVoucher.setCustomerId("KH01");
        truckWeighingVoucher.setTruckDriverId("TX01");
        truckWeighingVoucher.setNo("no");
        truckWeighingVoucher.setVoucherCode("vcode");
        truckWeighingVoucher.setVoucherAt(instant);
        truckWeighingVoucher.setWeighingTime1(instant);
        truckWeighingVoucher.setWeighingTime2(instant);
        truckWeighingVoucher.setCreatedAt(instant);
        truckWeighingVoucher.setCreatedByFullName("bien");
        truckWeighingVoucher.setUpdateAt(instant);
        truckWeighingVoucher.setUpdateByFullName("bien");
        truckWeighingVoucher.setWeighingResultFinal(BigDecimal.valueOf(10));
        truckWeighingVoucher.setWeighingResult1(BigDecimal.valueOf(100));
        truckWeighingVoucher.setWeighingResult2(BigDecimal.valueOf(101));
        truckWeighingVoucher.setTruckLicensePlateNumber("30A21621");

        truckWeighingVoucherRepository.save(truckWeighingVoucher);
        MultiValueMap<String,String> where = new LinkedMultiValueMap<>();
        where.add("no","no");
        ResultPage<Object[]> rs= truckWeighingVoucherDetailRepository.findAllWithFilter(pageable,where);
        Object[] returnObject = rs.getPageList().get(0);
        TruckWeighingVoucher returnTruckWeightVoucher = (TruckWeighingVoucher) returnObject[0];
        assertEquals(truckWeighingVoucher.getCustomerId(),returnTruckWeightVoucher.getCustomerId());
    }
}
