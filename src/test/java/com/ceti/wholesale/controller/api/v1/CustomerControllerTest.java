package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.*;
import com.ceti.wholesale.repository.CustomerDetailRepository;
import com.ceti.wholesale.repository.CustomerRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.service.CustomerService;
import com.ceti.wholesale.service.impl.CustomerServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;
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
@WebMvcTest(CustomerController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        CustomerService customerService(){return new CustomerServiceImpl();
        }
    }
    @Autowired
   private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;
    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private CustomerDetailRepository customerDetailRepository;
    private String jsonCreateRequest= "{\n" +
            "            \"code\": \"BM212\",\n" +
            "            \"company_id\": \"Công ty a\",\n" +
            "            \"name\": \"HA Biên\",\n" +
            "            \"address\": \"DFSDSv\",\n" +
            "            \"phone_number\": \"23432\",\n" +
            "            \"tax_code\": \"23D\",\n" +
            "            \"category\": \"CONGNGHIEP\",\n" +
            "            \"group_id\": \"3\",\n" +
            "            \"group_name\": \"Nhóm 3\"\n" +
            "        }";
    private String jsonUpdateRequest= "{\n" +
    		"            \"code\": \"NG123\",\n" +
            "            \"company_id\": \"Công ty a\",\n" +
            "            \"name\": \"Đổi tên\",\n" +
            "            \"address\": \"DFSDSv\",\n" +
            "            \"phone_number\": \"23432\",\n" +
            "            \"tax_code\": \"23D\",\n" +
            "            \"category\": \"CONGNGHIEP\",\n" +
            "            \"group_id\": \"3\",\n" +
            "            \"group_name\": \"Nhóm 3\"\n" +
            "        }";
    String id="1";
    @Test
    public void testGetCustomerListSuccess()throws Exception{
        List<Object[]> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            Customer customer = new Customer().setId("A"+i).setName("name"+i).setCompanyId("conpany"+i)
                    .setAddress("address"+i).setGroupId("group"+i).setPhoneNumber("09231"+i).setTaxCode("tx"+i)
                    .setCategory("category"+i);
            Factory factory = new Factory().setId("FAC1").setName("FAC");
            Region region = new Region().setId("RE"+i).setName("RE"+i);
            CustomerCategory customerCategory = new CustomerCategory().setId("DAILY"+i).setName("Dai ly"+i);
            Object[] objects = new Object[4];
            objects[0]=customer;
            objects[1]=factory;
            objects[2]=region;
            objects[3]=customerCategory;
            list.add(objects);
        }
        ResultPage<Object[]>resultPage = new ResultPage<>();
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        resultPage.setPageList(list);
        resultPage.setTotalItems(5);

        given(customerDetailRepository.findAllWithEmbed(isA(Pageable.class), Mockito.any())).willReturn(resultPage);
        mockMvc.perform(get("/v1/customers")
                .header("department_id","FAC1")
                .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }
    @Test
    public void testCreateCustomerSuccess() throws Exception {
        given(customerRepository.save(isA(Customer.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(post("/v1/customers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.code").value("BM212"));

    }
    @Test
    public void testUpdateCustomerSuccess() throws Exception {
    Customer customer = new Customer().setId(id).setName("name").setCompanyId("conpany")
            .setAddress("address").setGroupId("group").setPhoneNumber("09231").setTaxCode("tx")
            .setCategory("category");
    given(customerRepository.findById(id)).willReturn(Optional.of(customer));
        given(customerRepository.save(isA(Customer.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(patch("/v1/customers/{customer-id}",id).contentType(APPLICATION_JSON_UTF8).content(jsonUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("Đổi tên"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.code").value("NG123"));

    }
    @Test
    public void testUpdateCustomerFalseWithCustomerIdNotExists() throws Exception {
    given(customerRepository.findById("10001")).willReturn(Optional.empty());
        mockMvc.perform(patch("/v1/customers/{customer-id}","10001").contentType(APPLICATION_JSON_UTF8).content(jsonUpdateRequest))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã khách hàng không tồn tại"));

    }

    @Test
    public void testGetListCustomerSuccess()throws Exception{
        List<Object[]> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            Customer customer = new Customer().setId("A"+i).setName("name"+i).setCompanyId("conpany"+i)
                    .setAddress("address"+i).setGroupId("group"+i).setPhoneNumber("09231"+i).setTaxCode("tx"+i)
                    .setCategory("category"+i);
            Factory factory = new Factory().setId("FAC1").setName("FAC");
            Region region = new Region().setId("RE"+i).setName("RE"+i);
            CustomerCategory customerCategory = new CustomerCategory().setId("DAILY"+i).setName("Dai ly"+i);
            Object[] objects = new Object[4];
            objects[0]=customer;
            objects[1]=factory;
            objects[2]=region;
            objects[3]=customerCategory;
            list.add(objects);
        }
        ResultPage<Object[]>resultPage = new ResultPage<>();
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        resultPage.setPageList(list);
        resultPage.setTotalItems(5);

        given(customerDetailRepository.findAllWithEmbed(isA(Pageable.class), Mockito.any())).willReturn(resultPage);
        mockMvc.perform(get("/v1/customers")
                        .header("department_id","FAC1")
                        .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }


    @Test
    public void testGetListAll() throws Exception {

        // Get listCustomer success
        List<Customer> listCustomer =new ArrayList<>();
        List<Object[]> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            Customer customer = new Customer().setId("A"+i).setName("name"+i).setCompanyId("conpany"+i)
                    .setAddress("address"+i).setGroupId("group"+i).setPhoneNumber("09231"+i).setTaxCode("tx"+i)
                    .setCategory("category"+i);
            listCustomer.add(customer);
            Factory factory = new Factory().setId("FAC1").setName("FAC");
            Region region = new Region().setId("RE"+i).setName("RE"+i);
            CustomerCategory customerCategory = new CustomerCategory().setId("DAILY"+i).setName("Dai ly"+i);
            Object[] objects = new Object[4];
            objects[0]=customer;
            objects[1]=factory;
            objects[2]=region;
            objects[3]=customerCategory;
            list.add(objects);
        }
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        Pageable pageable = PageRequest.of(0,20);
        ResultPage<Object[]> result = new ResultPage<>();
        result.setPageList(list);
        result.setTotalItems(5);

        given(customerDetailRepository.findAllWithEmbed(pageable, where)).willReturn(result);

        mockMvc.perform(get("/v1/customers/all").params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }


}
