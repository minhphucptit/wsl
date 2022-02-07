package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.model.ProductType;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.ProductTypeRepository;
import com.ceti.wholesale.service.ProductTypeService;
import com.ceti.wholesale.service.impl.ProductTypeServiceImpl;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductTypeController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ProductTypeControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        ProductTypeService productTypeService(){return new ProductTypeServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;
    @MockBean
    private ProductTypeRepository productTypeRepository;
    private String jsonCreateRequest= "{\n" +
            "            \"id\": \"001\",\n" +
            "            \"name\": \"Ha1\"\n" +
            "        }";
    private String jsonUpdateRequest= "{\n" +
            "            \"name\": \"Ha1\"\n" +
            "        }";
    @Test
    public void testGetProductTypeSuccess()throws Exception{
        List<ProductType> list= new ArrayList<>();
        for(int i=0;i<5;i++){
            ProductType productType = new ProductType().setId("A"+i).setName("name"+i);
            list.add(productType);
        }
        Pageable pageable= PageRequest.of(0,20);
        Page<ProductType> productTypes = new PageImpl<ProductType>(list,pageable, list.size());
        given(productTypeRepository.getAllByCondition(null,null,null,null, pageable)).willReturn(productTypes);
        mockMvc.perform(get("/v1/product-types"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").value("A0"));
    }
    @Test
    public void testCreateProductTypeSuccess()throws Exception{
        given(productTypeRepository.save(isA(ProductType.class))).willAnswer(i-> i.getArgument(0));
        given(productTypeRepository.existsById("001")).willReturn(false);
        mockMvc.perform(post("/v1/product-types").contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));
    }
    @Test
    public void testCreateProductTypeFalseWithIdExists()throws Exception{
        given(productTypeRepository.existsById("001")).willReturn(true);
        mockMvc.perform(post("/v1/product-types").contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã loại hàng đã tồn tại"));
    }
    @Test
    public void testUpdateProductTypeSuccess()throws Exception{
        ProductType productType = new ProductType().setId("001").setName("Bienhd");
        given(productTypeRepository.findById("001")).willReturn(Optional.of(productType));
        given(productTypeRepository.save(isA(ProductType.class))).willAnswer(i-> i.getArgument(0));
        mockMvc.perform(patch("/v1/product-types/{id}", "001").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }
    @Test
    public void testUpdateProductTypeFalseWithIdNotExists()throws Exception{
        given(productTypeRepository.findById("bi12")).willReturn(Optional.empty());
        mockMvc.perform(patch("/v1/product-types/{id}", "bi12").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã loại hàng không tồn tại"));
    }
}
