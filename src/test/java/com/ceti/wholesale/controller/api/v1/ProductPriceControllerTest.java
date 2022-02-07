package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.Brand;
import com.ceti.wholesale.model.Product;
import com.ceti.wholesale.model.ProductPrice;
import com.ceti.wholesale.repository.BrandRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.ProductPriceDetailRepository;
import com.ceti.wholesale.repository.ProductPriceRepository;
import com.ceti.wholesale.service.BrandService;
import com.ceti.wholesale.service.ProductPriceService;
import com.ceti.wholesale.service.impl.BrandServiceImpl;
import com.ceti.wholesale.service.impl.ProductPriceServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductPriceController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ProductPriceControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        ProductPriceService productPriceService(){return new ProductPriceServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductPriceDetailRepository productPriceDetailRepository;
    @MockBean
    private FactoryRepository factoryRepository;

    @Test
    public void testGetListProductPriceSuccess()throws Exception{
        List<Object[]> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            ProductPrice productPrice = new ProductPrice().setId("A"+i).setProductId("product"+i);
            Product product = new Product().setId("product"+i).setFactoryId("factoryId");
            Object[] objects = new Object[3];
            objects[0] = productPrice;
            objects[1] =product;
            list.add(objects);
        }
        ResultPage<Object[]> result = new ResultPage<>();
        result.setPageList(list);
        given(productPriceDetailRepository.findAllWithFilter(isA(Pageable.class), Mockito.any())).willReturn(result);
        mockMvc.perform(get("/v1/product-prices")
        .header("department_id","factoryId"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"));
    }

}
