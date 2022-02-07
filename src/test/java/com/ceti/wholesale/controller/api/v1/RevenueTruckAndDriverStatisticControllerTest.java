package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.dto.RevenueAndCycleStatisticCustomerDto;
import com.ceti.wholesale.dto.RevenueAndCycleStatisticDto;
import com.ceti.wholesale.dto.RevenueTruckAndDriverStatisticDto;
import com.ceti.wholesale.mapper.RevenueAndCycleStatisticMapper;
import com.ceti.wholesale.mapper.RevenueTruckAndDriverStatisticMapper;
import com.ceti.wholesale.repository.FactoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
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
@WebMvcTest(RevenueTruckAndDriverStatisticController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RevenueTruckAndDriverStatisticControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RevenueTruckAndDriverStatisticMapper revenueTruckAndDriverStatisticMapper;

    @Test
    public void testGetAllSuccess() throws Exception {
        List<List<Object>> objectsList = new ArrayList<>();
        List<RevenueTruckAndDriverStatisticDto> revenueTruckAndDriverStatisticList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RevenueTruckAndDriverStatisticDto item = new RevenueTruckAndDriverStatisticDto();
            revenueTruckAndDriverStatisticList.add(item);
        }
        List<Object> object =Collections.singletonList(revenueTruckAndDriverStatisticList);
        objectsList.add(object);

        Pageable pageable = PageRequest.of(0, 20);
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id","factoryId");
        where.add("type","type");
        where.add("date_from","1633053853");
        where.add("date_to","1634273946");
        where.add("weight", "123");

        given(revenueTruckAndDriverStatisticMapper.getAll("factoryId",Instant.ofEpochSecond(1633053853), Instant.ofEpochSecond(1634273946),"type","123",pageable.getPageNumber(), pageable.getPageSize())).willReturn(objectsList);

        mockMvc.perform(get("/v1/revenue-truck-and-driver-statistics").params(where)).andExpect(status().isNotFound());
    }
}