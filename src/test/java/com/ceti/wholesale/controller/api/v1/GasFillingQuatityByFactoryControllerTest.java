package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.dto.GasFillingQuantityByFactoryDto;
import com.ceti.wholesale.mapper.GasFillingQuantityByFactoryMapper;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GasFillingQuantityByFactoryController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class GasFillingQuatityByFactoryControllerTest {
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
    private GasFillingQuantityByFactoryMapper gasFillingQuantityByFactoryMapper;


    @Test
    public void testGetAllSuccess() throws Exception {
        List<GasFillingQuantityByFactoryDto> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GasFillingQuantityByFactoryDto item = new GasFillingQuantityByFactoryDto().setFactoryId("nhat" + i).setTotalQuantity(BigDecimal.valueOf(1+ i));
            list.add(item);
        }
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("date_from" , "1619827200");
        where.add("date_to" , "1634273946");
        where.add("factory_id" , "nhat0");
        given(gasFillingQuantityByFactoryMapper.getList(Instant.ofEpochSecond(1619827200), Instant.ofEpochSecond(1634273946), "nhat")).willReturn(list);

        mockMvc.perform(get("/v1/gas-filling-quantity-by-factory").params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }
}
