package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.GoodsInOut;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class GoodsInOutDetailRepositoryTest {
    @TestConfiguration
    public static class GoodsInoutDetailRepositoryTestConfiguration{
        @Bean
        GoodsInOutDetailRepository goodsInOutDetailRepository(){
            return new GoodsInOutDetailRepository();
        }
    }
    @Autowired
    private GoodsInOutDetailRepository goodsInOutDetailRepositories;
    @Autowired
    private GoodsInOutRepository goodsInOutRepository;
    @Test
    public void testFillAllWithFilter() throws Exception{
        Pageable pageable = PageRequest.of(0,20);
        GoodsInOut goodsInOut = new GoodsInOut();
        goodsInOut.setId("2c9c8086783a491101783de075730005");
        goodsInOut.setPrice(BigDecimal.valueOf(12));
        goodsInOut.setProductId("XH210317-001-XXE");
        goodsInOut.setVoucherCode("das");
        goodsInOut.setVoucherNo("123");
        goodsInOut.setType("type");
        goodsInOut.setOutQuantity(BigDecimal.valueOf(1));
        goodsInOut.setInQuantity(BigDecimal.valueOf(2));
        goodsInOut.setProductName("ca");
        goodsInOut.setUnit("unit");
        goodsInOut.setNxeInQuantity(BigDecimal.valueOf(3));
        goodsInOut.setXbxOutQuantity(BigDecimal.valueOf(12));
        goodsInOut.setProductType("ptype");
        goodsInOut.setVoucherId("XH210317-001-XXE");

        goodsInOutRepository.save(goodsInOut);
        MultiValueMap<String,String> where = new LinkedMultiValueMap<>();
        where.add("voucher_id","XH210317-001-XXE");
        ResultPage<GoodsInOut> rs= goodsInOutDetailRepositories.findAllWithFilter(pageable,where);
        GoodsInOut returnGoodInOut = rs.getPageList().get(0);
        assertEquals(goodsInOut.getProductId(),returnGoodInOut.getProductId());

    }
}
