package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.model.ProductUnit;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.ProductUnitRepository;
import com.ceti.wholesale.service.ProductUnitService;
import com.ceti.wholesale.service.impl.ProductUnitServiceImpl;
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
@WebMvcTest(ProductUnitController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ProductUnitControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        ProductUnitService productUnitService(){return new ProductUnitServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductUnitRepository productUnitRepository;
    @MockBean
    private FactoryRepository factoryRepository;

    @Test
    public void testGetProductUnitSuccess()throws Exception{
        List<ProductUnit> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            ProductUnit productUnit = new ProductUnit().setId("A"+i).setName("name"+i);
            list.add(productUnit);
        }
        Pageable pageable= PageRequest.of(0,20);
        Page<ProductUnit> productUnits = new PageImpl<ProductUnit>(list,pageable, list.size());
        given(productUnitRepository.findAll( pageable)).willReturn(productUnits);
        mockMvc.perform(get("/v1/product_unit"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"));
    }

}
