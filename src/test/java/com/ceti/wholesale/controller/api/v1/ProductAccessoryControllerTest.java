package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.model.MoneyInStock;
import com.ceti.wholesale.model.ProductAccessory;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.ProductAccessoryRepository;
import com.ceti.wholesale.service.ProductAccessoryService;
import com.ceti.wholesale.service.impl.ProductAccessoryServiceImpl;
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

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductAccessoryController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ProductAccessoryControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        ProductAccessoryService productAccessoryService() {
            return new ProductAccessoryServiceImpl();
        }

    }

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;

    @MockBean
    private ProductAccessoryRepository productAccessoryRepository;

    private ProductAccessory productAccessory;

    String jsonCreateRequest = "{\n" +
            "            \"customer_id\": \"ct_001\",\n" +
            "            \"customer_full_name\": \"do van hung\",\n" +
            "            \"payer\": \"payer01\", \n" +
            "            \"payer_method\": \"false\", \n" +
            "            \"address\": \"ha noi\", \n" +
            "            \"reason\": \"vay cong lai\", \n" +
            "            \"amount_of_money\": \"12\", \n" +
            "            \"note\": \"tien vay cong lai\", \n"+
            "            \"category\": \"rpc_001\", \n"+
            "            \"create_by\": \"hung\", \n"+
            "            \"update_by\": \"hung\", \n"+
            "            \"voucher_at\": \"123\", \n"+
            "            \"type\": \"true\" \n"+
            "}";

    String jsonUpdateRequest = "{" +
            "    \"product_accessory\":" +
            "[{ \"sub_product_id\":\"H01\", " +
            "\"sub_product_name\":\"Hàng 2\"," +
            "\"sub_product_quantity\":\"6\"" +
            "}]}";


    @Test
    public void testUpdateProductAccessorySuccess() throws Exception{
        productAccessoryRepository.deleteByMainProductIdAndFactoryId("H01","1");
        given(productAccessoryRepository.save(isA(ProductAccessory.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(patch("/v1/factory-products/{product_id}/product-accessories","H01").header("department_id", "1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].sub_product_id").value("H01"));
    }

    @Test
    public void testGetListCustomerReceiptPaymentSuccess() throws Exception {
        List<ProductAccessory> list = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            ProductAccessory productAccessory = new ProductAccessory();
            productAccessory.setMainProductId("i");
            productAccessory.setFactoryId("1");
            productAccessory.setId("i");
            productAccessory.setSubProductName("name"+i);
            productAccessory.setSubProductType("type"+i);
            list.add(productAccessory);
        }
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("i","1")).willReturn(list);

        mockMvc.perform(get("/v1/factory-products/{product_id}/product-accessories", "i").header("department_id", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").value("i"));

    }
}
