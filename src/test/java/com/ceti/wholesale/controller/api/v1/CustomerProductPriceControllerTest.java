package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.mapper.CustomerProductPriceMapper;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.CustomerProductPrice;
import com.ceti.wholesale.repository.CustomerProductPriceRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.TruckDriverDetailRepository;
import com.ceti.wholesale.service.CustomerProductPriceService;
import com.ceti.wholesale.service.impl.CustomerProductPriceServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerProductPriceController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerProductPriceControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        CustomerProductPriceService customerProductPriceService(){return new CustomerProductPriceServiceImpl();
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;

    @MockBean
    private CustomerProductPriceRepository customerProductPriceRepository;

    @MockBean
    private CustomerProductPriceMapper customerProductPriceMapper;

    @MockBean
    private TruckDriverDetailRepository truckDriverDetailRepository;
    private String jsonCreateRequest= "{\n" +
            "    \"customer_id\": \"165XUANTHUY\",\n" +
            "    \"product_id\":\"12123\",\n" +
            "    \"price\":6000,\n" +
            "    \"factory_id\":\"FAC\"\n" +
            "}";
    private String jsonUpdateRequest="{\n" +
            "\n" +
            "    \"product_id\":\"12123\",\n" +
            "    \"price\":6000\n" +
            "}";
    @Test
    public void testCreateCustomerProductPrice()throws Exception{
        //tạo mới CustomerProductPrice thành công
        given(customerProductPriceRepository.save(isA(CustomerProductPrice.class))).willAnswer(i -> i.getArgument(0));
            mockMvc.perform(post("/v1/customer-product-prices").header("user_id","ma")
                    .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));

            //đã tồn tại ProductIdAndCustomerId
        given(customerProductPriceRepository.existsByProductIdAndCustomerId("12123","165XUANTHUY")).willReturn(true);
        mockMvc.perform(post("/v1/customer-product-prices").header("user_id","ma")
                        .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Giá cho sản phẩm: 12123 của khách hàng165XUANTHUY đã tồn tại"));
        }


        @Test
    public void testUpdateTruckDriverSuccess()throws Exception{
            CustomerProductPrice productPrice = new CustomerProductPrice().setId("1").setProductId("12123").setCustomerId("C01").setPrice(BigDecimal.valueOf(123));
            given(customerProductPriceRepository.existsById("1")).willReturn(true);
            given(customerProductPriceRepository.findById("1")).willReturn(Optional.of(productPrice));
            mockMvc.perform(patch("/v1/customer-product-prices/{id}", "1").contentType(APPLICATION_JSON_UTF8).header("user_id","ma")
                    .content(jsonUpdateRequest)).andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
        }
        @Test
    public void testUpdateTruckDriverFalseWithIdNotExists()throws Exception{
        given(customerProductPriceRepository.existsById("adqed")).willReturn(false);
            mockMvc.perform(patch("/v1/customer-product-prices/{id}", "adqed").contentType(APPLICATION_JSON_UTF8).header("user_id","ma")
                    .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Giá khách hàng không tồn tại"));
        }

    @Test
    public void testGetListSuccess() throws Exception {

        List<CustomerProductPrice> customerProductPrices = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            CustomerProductPrice customerProductPrice = new CustomerProductPrice().setFactoryId("fac").setId("A"+i).setCustomerId("customerId")
                    .setProductId("product").setPrice(BigDecimal.valueOf(120)).setUpdateAt(Instant.now()).setUpdateBy("Ma");
            customerProductPrices.add(customerProductPrice);
        }
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> where_ = new LinkedMultiValueMap<>();
        where.add("factory_id","fac");
        ResultPage<CustomerProductPrice> resultPage = new ResultPage<>();
        resultPage.setPageList(customerProductPrices);
        resultPage.setTotalItems(5);
        given(customerProductPriceRepository.findAll(isA(Pageable.class), Mockito.any())).willReturn(resultPage);
        mockMvc.perform(get("/v1/customer-product-prices").header("department_id","fac")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }
    @Test
    public void testGetListSuccessWithEmbed() throws Exception {

        List<Object[]> list = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            CustomerProductPrice customerProductPrice = new CustomerProductPrice().setFactoryId("fac").setId("A"+i).setCustomerId("customerId")
                    .setProductId("product").setPrice(BigDecimal.valueOf(120)).setUpdateAt(Instant.now()).setUpdateBy("Ma");
            Customer customer = new Customer().setId("id").setFactoryId("fac").setCompanyId("com");
            Object[] object = new Object[3];
            object[0]=customerProductPrice;
            object[1]=customer;
            list.add(object);
        }
        List<String> table = new ArrayList<>();
        table.add("customer");
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> where_ = new LinkedMultiValueMap<>();
        where_.add("embed","customer");
        where.add("factory_id","f1");
        ResultPage<Object[]> resultPage = new ResultPage<Object[]>();
        resultPage.setPageList(list);
        resultPage.setTotalItems(5);
        given(customerProductPriceRepository.findAllWithEmbed(isA(Pageable.class),Mockito.any(),Mockito.any())).willReturn(resultPage);
        mockMvc.perform(get("/v1/customer-product-prices").header("department_id","fac")
                .params(where_)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }
}

