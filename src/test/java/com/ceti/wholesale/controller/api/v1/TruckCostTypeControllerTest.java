package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.model.TruckCostType;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.TruckCostTypeRepository;
import com.ceti.wholesale.service.TruckCostTypeService;
import com.ceti.wholesale.service.impl.TruckCostTypeServiceImpl;
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
@WebMvcTest(TruckCostTypeController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TruckCostTypeControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        TruckCostTypeService truckCostTypeService(){return new TruckCostTypeServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TruckCostTypeRepository truckCostTypeRepository;
    @MockBean
    private FactoryRepository factoryRepository;
    @Test
    public void testGetTruckCostTypeSuccess()throws Exception{
        List<TruckCostType> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            TruckCostType truckCostType = new TruckCostType().setId("A"+i).setName("name"+i);
            list.add(truckCostType);
        }
        Pageable pageable= PageRequest.of(0,20);
        Page<TruckCostType> truckCostTypes = new PageImpl<TruckCostType>(list,pageable, list.size());
        given(truckCostTypeRepository.findAll( pageable)).willReturn(truckCostTypes);
        mockMvc.perform(get("/v1/truck_cost_type"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }

}
