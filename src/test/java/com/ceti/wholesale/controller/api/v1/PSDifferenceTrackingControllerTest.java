package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.dto.PSDifferenceTrackingDto;
import com.ceti.wholesale.mapper.PSDifferenceTrackingMapper;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PSDifferenceTrackingController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PSDifferenceTrackingControllerTest {
	
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
    private PSDifferenceTrackingMapper psDifferenceTrackingMapper;
    

    @Test
    public void testGetPSDifferenceTrackingSuccess() throws Exception {

        List<List<Object>> lists = new ArrayList<>();
        List<PSDifferenceTrackingDto> PSDifferenceTrackingDtos = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            PSDifferenceTrackingDto PSDifferenceTrackingDto = new PSDifferenceTrackingDto();
            PSDifferenceTrackingDto.setPsDifference(BigDecimal.valueOf(1+i));
            PSDifferenceTrackingDto.setDistance(BigDecimal.valueOf(2+i));
            PSDifferenceTrackingDto.setDoBonus(BigDecimal.valueOf(3+i));
            PSDifferenceTrackingDto.setTruckLicensePlateNumber("TruckLicensePlateNumber" + i);
            PSDifferenceTrackingDto.setFactoryName("FactoryName" + i);
            PSDifferenceTrackingDto.setGasFollowGps(BigDecimal.valueOf(4+i));
            PSDifferenceTrackingDto.setQuota(BigDecimal.valueOf(5+i));
            PSDifferenceTrackingDto.setTotalQuantity(BigDecimal.valueOf(6+i));
            PSDifferenceTrackingDtos.add(PSDifferenceTrackingDto);
        }
        Pageable pageable= PageRequest.of(0,20);
        long totalItems = 5;
        lists.add(Collections.singletonList(PSDifferenceTrackingDtos));
        lists.add(Collections.singletonList(totalItems));
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("month","11");
        where.add("year","2021");
        where.add("factory_id","nhat");
        where.add("truck_license_plate_number","truckLicensePlateNumber");
        where.add("has_ps_difference","false");

        given(psDifferenceTrackingMapper.getAll("nhat",11,2021,"truckLicensePlateNumber",false,pageable.getPageNumber(), pageable.getPageSize())).willReturn(lists);
        mockMvc.perform(get("/v1/ps-difference-trackings").params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"));
    }

}
