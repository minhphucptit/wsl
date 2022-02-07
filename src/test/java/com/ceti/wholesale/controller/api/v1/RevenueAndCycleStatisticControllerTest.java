package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.dto.*;
import com.ceti.wholesale.mapper.RevenueAndCycleStatisticMapper;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RevenueAndCycleStatisticController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RevenueAndCycleStatisticControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RevenueAndCycleStatisticMapper revenueAndCycleStatisticMapper;
    @MockBean
    private FactoryRepository factoryRepository;

    @Test
    public void testGetAllSuccess() throws Exception {
        List<List<Object>> objectsList = new ArrayList<>();
        List<RevenueAndCycleStatisticDto> listRevenueAndCycleStatistic = new ArrayList<>();
        RevenueAndCycleStatisticCustomerDto revenueAndCycleStatisticCustomer = new RevenueAndCycleStatisticCustomerDto();
        revenueAndCycleStatisticCustomer.setRevenueAndCycleStatistic(listRevenueAndCycleStatistic);
        for (int i = 0; i < 5; i++) {
            RevenueAndCycleStatisticDto item = new RevenueAndCycleStatisticDto().setCycle6(BigDecimal.valueOf(6+i)).setCycle12(BigDecimal.valueOf(12+i)).setCycle45(BigDecimal.valueOf(45+i))
                    .setRevenueDebt6(BigDecimal.valueOf(6+i)).setRevenueDebt12(BigDecimal.valueOf(12+i)).setRevenueDebt45(BigDecimal.valueOf(45+i))
                    .setBeginningDebt6(BigDecimal.valueOf(6+i)).setBeginningDebt12(BigDecimal.valueOf(12+i)).setBeginningDebt45(BigDecimal.valueOf(45+i))
                    .setEndingDebt6(BigDecimal.valueOf(6+i)).setEndingDebt12(BigDecimal.valueOf(12+i)).setEndingDebt45(BigDecimal.valueOf(45+i))
                    .setIntermDebt6(BigDecimal.valueOf(6+i)).setIntermDebt12(BigDecimal.valueOf(12+i)).setIntermDebt45(BigDecimal.valueOf(45+i));
            listRevenueAndCycleStatistic.add(item);
        }
        objectsList.add(Collections.singletonList(listRevenueAndCycleStatistic));
        objectsList.add(Collections.singletonList(revenueAndCycleStatisticCustomer));
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("customer_code","customerCode");
        where.add("date_from","1633053853");
        where.add("date_to","1634273946");

        given(revenueAndCycleStatisticMapper.getList("customerCode",Instant.ofEpochSecond(1633053853), Instant.ofEpochSecond(1634273946))).willReturn(objectsList);

        mockMvc.perform(get("/v1/revenue-and-cycles").params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }

}