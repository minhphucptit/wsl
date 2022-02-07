package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.dto.CylinderInventoryDto;
import com.ceti.wholesale.mapper.CylinderInventoryMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CylinderInventoryController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CylinderInventoryControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
    }
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CylinderInventoryMapper cylinderInventoryMapper;
    @MockBean
    private FactoryRepository factoryRepository;
    @Test
    public void testGetAllSuccess() throws Exception {
        List<CylinderInventoryDto> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CylinderInventoryDto item = new CylinderInventoryDto().setInventory(BigDecimal.valueOf(1 + i)).setFactoryName("FactoryName" + i).
                    setProductStatusName("ProductStatusName" + i).setWeight(1+i);
            list.add(item);
        }
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("voucher_at_to" , "1619827200");
        given(cylinderInventoryMapper.getList(Instant.ofEpochSecond(1619827200))).willReturn(list);

        mockMvc.perform(get("/v1/cylinder-inventories").params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(jsonPath("$.items[0].factory_name").value("FactoryName0"));
    }
}
