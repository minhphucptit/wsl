package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.dto.GoodsInventoryDto;
import com.ceti.wholesale.mapper.GoodsInventoryMapper;
import com.ceti.wholesale.repository.FactoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GoodsInventoryController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class GoodsInventoryControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;

    @MockBean
    private GoodsInventoryMapper goodsInventoryMapper;

    @Test
    public void testGetAllSuccess() throws Exception {
        List<GoodsInventoryDto> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GoodsInventoryDto item = new GoodsInventoryDto().setId("Id" + i).setInventory(BigDecimal.valueOf(1+ i)).setFactoryId("FactoryId" + i).setProductId("ProductId" + i)
                    .setProductName("ProductName" + i).setProductType("ProductType" + i).setUnit("Unit" + i)
                    .setVoucherCode("VoucherCode" + i).setVoucherId("VoucherId" + i).setVoucherNo("VoucherNo" + i).setWeight(BigDecimal.valueOf(1 + i));
            list.add(item);
        }
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("product_type" , "ProductType0");
        given(goodsInventoryMapper.getList("123","ProductType0")).willReturn(list);
        given(goodsInventoryMapper.countList("123","ProductType0")).willReturn(5l);

        mockMvc.perform(patch("/v1/inventory-vouchers/{inventory_voucher_id}/goods-inventories","123").params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(jsonPath("$.items[0].id").value("Id0"));
    }
}
