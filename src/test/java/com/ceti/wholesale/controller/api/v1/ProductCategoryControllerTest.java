package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.model.ProductCategory;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.ProductCategoryRepository;
import com.ceti.wholesale.service.ProductCategoryService;
import com.ceti.wholesale.service.impl.ProductCategoryServiceImpl;
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
@WebMvcTest(ProductCategoryController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ProductCategoryControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        ProductCategoryService productCategoryService(){return new ProductCategoryServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;
    @MockBean
    private ProductCategoryRepository productCategoryRepository;
    private String jsonCreateRequest="{\n" +
            "            \"id\": \"1\",\n" +
            "            \"name\": \"nhans\",\n" +
            "            \"product_type_id\": \"13\"\n" +
            "        }";
    private String jsonUpdateRequest="{\n" +
            "            \"name\": \"nhans\",\n" +
            "            \"product_type_id\": \"13\"\n" +
            "        }";
    @Test
    public void testGetProductCategorySuccess()throws Exception{
        List<ProductCategory> list= new ArrayList<>();
        for(int i=0;i<5;i++){
            ProductCategory productCategory = new ProductCategory().setId("A"+i).setName("name"+i).setProductTypeId("productId"+i);
            list.add(productCategory);
        }
        Pageable pageable = PageRequest.of(0,20);
        Page<ProductCategory> productCategories= new PageImpl<ProductCategory>(list,pageable, list.size());
        given(productCategoryRepository.getAllByCondition(null,null,null,null, pageable)).willReturn(productCategories);
        mockMvc.perform(get("/v1/product-categories"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").value("A0"));
    }
    @Test
    public void testCreateProductCategorySuccess()throws Exception{
        given(productCategoryRepository.save(isA(ProductCategory.class))).willAnswer(i->i.getArgument(0));
        given(productCategoryRepository.existsById("1")).willReturn(false);
        mockMvc.perform(post("/v1/product-categories").contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));
    }
    @Test
    public void testCreateProductCategoryFalseWithIdExists()throws Exception{
        given(productCategoryRepository.existsById("1")).willReturn(true);
        mockMvc.perform(post("/v1/product-categories").contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã nhóm hàng đã tồn tại"));
    }
    @Test
    public void testUpdateProductCategorySuccess()throws Exception{
        ProductCategory productCategory= new ProductCategory().setId("1").setName("Bien").setProductTypeId("1");
        given(productCategoryRepository.findById("1")).willReturn(Optional.of(productCategory));
        given(productCategoryRepository.save(isA(ProductCategory.class))).willAnswer(i ->i.getArgument(0));
        mockMvc.perform(patch("/v1/product-categories/{id}", "1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }
    @Test
    public void testUpdateProductCategoryFalseWithIdNotExists()throws Exception{
        given(productCategoryRepository.findById("25501")).willReturn(Optional.empty());
        mockMvc.perform(patch("/v1/product-categories/{id}", "25501").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã nhóm hàng không tồn tại"));
    }
}
