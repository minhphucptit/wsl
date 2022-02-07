package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.mapper.GoodInOutMapper;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.GoodsInOutDetailRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.impl.GoodsInOutServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GoodsInOutController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class GoodsInOutControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        GoodsInOutService goodsInOutService(){return new GoodsInOutServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;

    @MockBean
    private GoodsInOutDetailRepository goodsInOutDetailRepository;

    @MockBean
    private GoodInOutMapper goodInOutMapper;
    @MockBean
    private FactoryRepository factoryRepository;
    @Test
    public void testGetListGoodsInOutSuccess() throws Exception {

        List<GoodsInOutDto> listGoodsInOut = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            GoodsInOutDto goodsInOutDto = new GoodsInOutDto();
            goodsInOutDto.setVoucherId("voucher"+i);
            goodsInOutDto.setVoucherNo("voucher_no"+i);
            goodsInOutDto.setVoucherCode("voucher_code"+i);
            goodsInOutDto.setType("type"+i);
            goodsInOutDto.setId("id"+i);
            goodsInOutDto.setOutQuantity(BigDecimal.valueOf(100));
            goodsInOutDto.setInQuantity(BigDecimal.valueOf(10));
            goodsInOutDto.setProductId("product"+i);
            goodsInOutDto.setPrice(BigDecimal.valueOf(1000));
            goodsInOutDto.setUnit("VND");
            goodsInOutDto.setProductName("romano"+i);

            listGoodsInOut.add(goodsInOutDto);
        }
        String[] embed = new String[]{""};
        given(goodInOutMapper.getList("1", true, embed)).willReturn(listGoodsInOut);
        given(goodInOutMapper.countList("1", true, embed)).willReturn((long)listGoodsInOut.size());
        mockMvc.perform(get("/v1/goods-in-out").param("voucher_id", "1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").value("id0"));
    }
}
