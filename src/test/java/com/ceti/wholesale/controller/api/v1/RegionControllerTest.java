package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.model.Region;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.RegionRepository;
import com.ceti.wholesale.service.RegionService;
import com.ceti.wholesale.service.impl.RegionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RegionController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RegionControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        RegionService regionService(){return new RegionServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RegionRepository regionRepository;
    @MockBean
    private FactoryRepository factoryRepository;
    @Test
    public void testGetCustomerCategorySuccess()throws Exception{
        List<Region> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            Region region = new Region().setId("A"+i).setName("name"+i);
            list.add(region);
        }
        Pageable pageable= PageRequest.of(0,20);
        Page<Region> customerGroups = new PageImpl<Region>(list,pageable, list.size());
        given(regionRepository.findAll( pageable)).willReturn(customerGroups);
        mockMvc.perform(get("/v1/regions"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").value("A0"));
    }

}
