package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.model.CustomerGroup;
import com.ceti.wholesale.repository.CustomerGroupRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.service.CustomerGroupService;
import com.ceti.wholesale.service.CustomerService;
import com.ceti.wholesale.service.impl.CustomerGroupServiceImple;
import com.ceti.wholesale.service.impl.CustomerServiceImpl;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerGroupController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerGroupControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        CustomerGroupService customerGroupService(){return new CustomerGroupServiceImple();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerGroupRepository customerGroupRepository;
    @MockBean
    private FactoryRepository factoryRepository;
    private String jsonCreateRequest ="{\n" +
            "            \"id\": \"1\",\n" +
            "            \"name\": \"nhann\",\n" +
            "            \"note\": \"Mới\"\n" +
            "        }";
    private String jsonUpdateRequest = "{\n" +
            "            \"name\": \"nhann\",\n" +
            "            \"note\": \"Mới\"\n" +
            "        }";
    @Test
    public void testGetCustomerGroupSuccess()throws Exception{
        List<CustomerGroup> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            CustomerGroup customerGroup = new CustomerGroup().setId("A"+i).setName("name"+i).setNote("note"+i);
            list.add(customerGroup);
        }
        MultiValueMap<String,String> params= new LinkedMultiValueMap<>();
        params.add("factory_id","FAC1");
        Pageable pageable= PageRequest.of(0,20);
        Page<CustomerGroup> customerGroups = new PageImpl<CustomerGroup>(list,pageable, list.size());
        given(customerGroupRepository.getAllByCondition(null, null,null,"FAC1",null, pageable)).willReturn(customerGroups);
        mockMvc.perform(get("/v1/customer-groups").params(params))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").value("A0"));
    }
    @Test
    public void testCreateCustomerGroupSuccess()throws Exception{
        given(customerGroupRepository.save(isA(CustomerGroup.class))).willAnswer(i->i.getArgument(0));
        given(customerGroupRepository.existsById("1")).willReturn(false);
        mockMvc.perform(post("/v1/customer-groups")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));

    }
    @Test
    public void testCreateCustomerGroupFalseWithIDExists()throws Exception{
        given(customerGroupRepository.existsById("1")).willReturn(true);
        mockMvc.perform(post("/v1/customer-groups")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã tuyến đã tồn tại"));

    }
    @Test
    public void testUpdateCustomerGroupSuccess()throws Exception{
    CustomerGroup customerGroup = new CustomerGroup().setId("1").setName("bien").setNote("MOI");
    given(customerGroupRepository.findById("1")).willReturn(Optional.of(customerGroup));
    given(customerGroupRepository.save(isA(CustomerGroup.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(patch("/v1/customer-groups/{id}", "1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }
    @Test
    public void testUpdateCustomerGroupFalseWithIdNotExists() throws Exception {
        given(customerGroupRepository.findById("kjadha")).willReturn(Optional.empty());
        mockMvc.perform(patch("/v1/customer-groups/{id}", "kjadha").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã tuyến không tồn tại"));


    }
}
