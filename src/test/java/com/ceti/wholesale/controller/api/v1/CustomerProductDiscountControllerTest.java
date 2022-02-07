package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.*;
import com.ceti.wholesale.repository.CustomerProductDiscountRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.TruckDriverDetailRepository;
import com.ceti.wholesale.service.CustomerProductDiscountService;
import com.ceti.wholesale.service.impl.CustomerProductDiscountServiceImpl;
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
@WebMvcTest(CustomerProductDiscountController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerProductDiscountControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        CustomerProductDiscountService customerProductDiscountService(){return new CustomerProductDiscountServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerProductDiscountRepository customerProductDiscountRepository;
    @MockBean
    private FactoryRepository factoryRepository;
    @MockBean
    private TruckDriverDetailRepository truckDriverDetailRepository;
    private String jsonCreateRequest= "{\n" +
            "    \"customer_id\": \"165XUANTHUY\",\n" +
            "    \"product_id\":\"12123\",\n" +
            "    \"factory_id\": \"FAC1\",\n" +
            "    \"discount\":6000\n" +
            "}";
    private String jsonUpdateRequest="{\n" +
            "\n" +
            "    \"product_id\":\"12123\",\n" +
            "    \"discount\":6000\n" +
            "}";
        @Test
        public void testCreateCustomerProductDiscountSuccess()throws Exception{
            //tạo mới CustomerProductDiscount thành công
        given(customerProductDiscountRepository.existsByProductIdAndCustomerId("165XUANTHUY","12123")).willReturn(false);
        given(customerProductDiscountRepository.save(isA(CustomerProductDiscount.class))).willAnswer(i -> i.getArgument(0));
            mockMvc.perform(post("/v1/customer-product-discounts").header("department_id","FAC1").header("user_id","ma")
                    .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));

            //đã tồn tại ProductIdAndCustomerId
            given(customerProductDiscountRepository.existsByProductIdAndCustomerId("12123","165XUANTHUY")).willReturn(true);
            mockMvc.perform(post("/v1/customer-product-discounts").header("department_id","FAC1").header("user_id","ma")
                            .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                    .andExpect(status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Chiết khấu cho sản phẩm: 12123 của khách hàng165XUANTHUY đã tồn tại"));

        }

        @Test
    public void testUpdateCustomerProductDiscountSuccess()throws Exception{
            CustomerProductDiscount productPrice = new CustomerProductDiscount().setId("1").setProductId("12123").setCustomerId("C01").setDiscount(BigDecimal.valueOf(123));
            given(customerProductDiscountRepository.existsById("1")).willReturn(true);
            given(customerProductDiscountRepository.findById("1")).willReturn(Optional.of(productPrice));
            mockMvc.perform(patch("/v1/customer-product-discounts/{id}", "1").contentType(APPLICATION_JSON_UTF8).header("user_id","ma")
                    .content(jsonUpdateRequest)).andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
        }
        @Test
    public void testUpdateCustomerProductDiscountFalseWithIdNotExists()throws Exception{
        given(customerProductDiscountRepository.existsById("adqed")).willReturn(false);
            mockMvc.perform(patch("/v1/customer-product-discounts/{id}", "adqed").contentType(APPLICATION_JSON_UTF8).header("user_id","ma")
                    .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Chiết khấu khách hàng không tồn tại"));
        }

   @Test
    public void testGetListSuccess() throws Exception {

        List<CustomerProductDiscount> customerProductDiscounts = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            CustomerProductDiscount customerProductDiscount = new CustomerProductDiscount().setFactoryId("fac").setId("A"+i).setCustomerId("customerId")
                    .setProductId("product").setDiscount(BigDecimal.valueOf(120)).setUpdateAt(Instant.now()).setUpdateBy("Ma");
            customerProductDiscounts.add(customerProductDiscount);
        }
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id","fac");
        ResultPage<CustomerProductDiscount> resultPage = new ResultPage<>();
        resultPage.setPageList(customerProductDiscounts);
        resultPage.setTotalItems(5);
        given(customerProductDiscountRepository.findAll(isA(Pageable.class), Mockito.any())).willReturn(resultPage);
        mockMvc.perform(get("/v1/customer-product-discounts").header("department_id","fac")
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
            CustomerProductDiscount customerProductDiscount = new CustomerProductDiscount().setFactoryId("fac").setId("A"+i).setCustomerId("customerId")
                    .setProductId("product").setDiscount(BigDecimal.valueOf(120)).setUpdateAt(Instant.now()).setUpdateBy("Ma");
            Customer customer = new Customer().setId("id").setFactoryId("fac").setCompanyId("com");
            Object[] object = new Object[3];
            object[0]=customerProductDiscount;
            object[1]=customer;
            list.add(object);
        }
        List<String> table = new ArrayList<>();
        table.add("customer");
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> where_ = new LinkedMultiValueMap<>();
        where_.add("embed","customer");
        where.add("factory_id","fac");
        ResultPage<Object[]> resultPage = new ResultPage<Object[]>();
        resultPage.setPageList(list);
        resultPage.setTotalItems(5);
        given(customerProductDiscountRepository.findAllWithEmbed(isA(Pageable.class),Mockito.any(),Mockito.any())).willReturn(resultPage);
        mockMvc.perform(get("/v1/customer-product-discounts").header("department_id","fac")
                .params(where_)
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }

    @Test
    public void testGetListSuccessWithEmbedProduct() throws Exception {

        List<Object[]> list = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            CustomerProductDiscount customerProductDiscount = new CustomerProductDiscount().setFactoryId("fac").setId("A"+i).setCustomerId("customerId")
                    .setProductId("product").setDiscount(BigDecimal.valueOf(120)).setUpdateAt(Instant.now()).setUpdateBy("Ma");
            Product product = new Product().setId("id").setFactoryId("fac").setCylinderCategory(BigDecimal.valueOf(1+i)).setCategoryId("CategoryId")
                    .setPurpose("Purpose" + i).setReferenceProductId("ReferenceProductId").setUnit("Unit" + i).setBuyPrice(BigDecimal.valueOf(1+i)).setName("Name");
            Object[] object = new Object[3];
            object[0]=customerProductDiscount;
            object[1]=product;
            list.add(object);
        }
        List<String> table = new ArrayList<>();
        table.add("product");
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> where_ = new LinkedMultiValueMap<>();
        where_.add("embed","product");
        where.add("factory_id","fac");
        ResultPage<Object[]> resultPage = new ResultPage<Object[]>();
        resultPage.setPageList(list);
        resultPage.setTotalItems(5);
        given(customerProductDiscountRepository.findAllWithEmbed(isA(Pageable.class),Mockito.any(),Mockito.any())).willReturn(resultPage);
        mockMvc.perform(get("/v1/customer-product-discounts").header("department_id","fac")
                        .params(where_)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }

    @Test
    public void testGetListSuccessWithEmbedFactory() throws Exception {

        List<Object[]> list = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            CustomerProductDiscount customerProductDiscount = new CustomerProductDiscount().setFactoryId("fac").setId("A"+i).setCustomerId("customerId")
                    .setProductId("product").setDiscount(BigDecimal.valueOf(120)).setUpdateAt(Instant.now()).setUpdateBy("Ma");
            Factory factory = new Factory().setId("id").setName("Name" + i).setDirectorFullName("DirectorFullName" + i)
                    .setAddressOnVoucher("AddressOnVoucher" + i).setNameOnVoucher("NameOnVoucher").setAccountantFullName("AccountantFullName");
            Object[] object = new Object[3];
            object[0]=customerProductDiscount;
            object[1]=factory;
            list.add(object);
        }
        List<String> table = new ArrayList<>();
        table.add("factory");
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> where_ = new LinkedMultiValueMap<>();
        where_.add("embed","factory");
        where.add("factory_id","fac");
        ResultPage<Object[]> resultPage = new ResultPage<Object[]>();
        resultPage.setPageList(list);
        resultPage.setTotalItems(5);
        given(customerProductDiscountRepository.findAllWithEmbed(isA(Pageable.class),Mockito.any(),Mockito.any())).willReturn(resultPage);
        mockMvc.perform(get("/v1/customer-product-discounts").header("department_id","fac")
                        .params(where_)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }
}

