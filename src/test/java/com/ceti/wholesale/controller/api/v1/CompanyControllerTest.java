package com.ceti.wholesale.controller.api.v1;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ceti.wholesale.repository.FactoryRepository;
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

import com.ceti.wholesale.model.Company;
import com.ceti.wholesale.repository.CompanyRepository;
import com.ceti.wholesale.service.CompanyService;
import com.ceti.wholesale.service.impl.CompanyServiceImpl;


@RunWith(SpringRunner.class)
@WebMvcTest(CompanyController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CompanyControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        CompanyService companyService() {
            return new CompanyServiceImpl();
        }

    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;

    //repository
    @MockBean
    private CompanyRepository companyRepository;

//common value
private String jsonCreateRequest= "{\n" +
        "            \"id\": \"1\",\n" +
        "            \"name\": \"Công ty a\",\n" +
        "            \"address\": \"Hà Nội\",\n" +
        "            \"phone_number\": \"1523658788\",\n" +
        "            \"tax_code\": \"2s5\"\n" +
        "        }";
private String jsonUpdateRequest="{\n" +
        "            \"name\": \"Công ty a\",\n" +
        "            \"address\": \"Hà Nội\",\n" +
        "            \"phone_number\": \"1082732331\",\n" +
        "            \"tax_code\": \"2s325\"\n" +
        "        }";
 String id="1";
    @Test
    public void testGetListCompanySuccess() throws Exception {
        List<Company> companies = new ArrayList<>();
        for(int i=0;i<5;i++){
        Company company = new Company().setId("A"+i).setName("Công ty "+i).setAddress("address "+i).setPhoneNumber("093231112"+i).setTaxCode("tx0"+i);
        companies.add(company);
        }
        Pageable pageable= PageRequest.of(0,20);
        Page<Company> result =new PageImpl<Company>(companies,pageable,companies.size());

        given(companyRepository.getAllByCondition(null,null,null,null,null,null, pageable)).willReturn( result);
        mockMvc.perform(get("/v1/companies").header("department_id","FAC1"))
                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").value("A0"));
    }
    @Test
    public void testCreateCompanySuccess()throws Exception{
        given(companyRepository.save(isA(Company.class))).willAnswer(i -> i.getArgument(0));
        given(companyRepository.existsById(id)).willReturn(false);
        mockMvc.perform(post("/v1/companies")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));

    }
    @Test
    public void testCreateCompanyFalseWithCompanyIdExitsts() throws Exception {
        given(companyRepository.existsById(id)).willReturn(true);
    mockMvc.perform(post("/v1/companies").header("department_id","FAC1").contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest)).andExpect(status().is4xxClientError())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã công ty đã tồn tại"));

    }
    @Test
    public void testUpdateCompanyFalseWithCompanyNotExists() throws Exception {

        given(companyRepository.findById(id)).willReturn(Optional.empty());
        mockMvc.perform(patch("/v1/companies/{company_id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã công ty không tồn tại"));
    }
    @Test
    public void testUpdateCompanySuccess() throws Exception{
        Company company1= new Company().setId(id).setName("name").setAddress("A0").setPhoneNumber("98326").setTaxCode("udsjas");
          given(companyRepository.findById(id)).willReturn(Optional.of(company1));
        given(companyRepository.save(isA(Company.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(patch("/v1/companies/{company_id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }


}
