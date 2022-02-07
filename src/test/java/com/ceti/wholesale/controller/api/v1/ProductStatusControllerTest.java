package com.ceti.wholesale.controller.api.v1;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.ceti.wholesale.repository.FactoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
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

import com.ceti.wholesale.model.ProductStatus;
import com.ceti.wholesale.repository.ProductStatusRepository;
import com.ceti.wholesale.service.ProductStatusService;
import com.ceti.wholesale.service.impl.ProductStatusServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductStatusController.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class ProductStatusControllerTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf-8"));
	
	@TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        ProductStatusService productStatusService() {
            return new ProductStatusServiceImpl();
        }
    }
	
	@Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;

    @MockBean
    private ProductStatusRepository productStatusRepository;
    
    @Test
    public void testGetListProductStatusSuccess() throws Exception {
    	List<ProductStatus> listStatus = new ArrayList<>();
    	
    	for(int i=0;i<5;i++) {
    		ProductStatus pStatus = new ProductStatus().setId("" + i).setName("s "+i).setCode(i+" c");
    		listStatus.add(pStatus);
    	}
    	
    	Pageable pageable= PageRequest.of(0,20);
    	
    	Page<ProductStatus> page = new PageImpl<ProductStatus>(listStatus, pageable, listStatus.size());
    	
    	given(productStatusRepository.findAll(pageable)).willReturn(page);
    	
    	mockMvc.perform(get("/v1/product-status")
                .header("department_id","FAC1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("0"));
    }
}
