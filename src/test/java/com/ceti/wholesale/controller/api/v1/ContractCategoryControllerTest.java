package com.ceti.wholesale.controller.api.v1;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ceti.wholesale.controller.api.request.CreateContractCategoryRequest;
import com.ceti.wholesale.controller.api.request.UpdateContractCategoryRequest;
import com.ceti.wholesale.model.ContractCategory;
import com.ceti.wholesale.repository.ContractCategoryRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.service.ContractCategoryService;
import com.ceti.wholesale.service.impl.ContractCategoryServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(ContractCategoryController.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class ContractCategoryControllerTest {
      public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf-8"));

      @TestConfiguration
      public static class ServiceTestConfiguration{
            @Bean
            ContractCategoryService contractCategoryService(){return new ContractCategoryServiceImpl();
            }
      }

      @Autowired
      private MockMvc mockMvc;
      @MockBean
      private FactoryRepository factoryRepository;

      @MockBean
      private ContractCategoryRepository contractCategoryRepository;

      //common value
      private String getJsonCreateRequest() throws JsonProcessingException {
            CreateContractCategoryRequest request=
            new CreateContractCategoryRequest().setName("phuc");

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            JavaTimeModule module = new JavaTimeModule();
            mapper.registerModule(module);
            String requestJson = mapper.writeValueAsString(request);

            return requestJson;
      }

      private String getJsonUpdateRequest() throws JsonProcessingException {
            UpdateContractCategoryRequest request=
            new UpdateContractCategoryRequest().setName("phuc").setIsActive(false);

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            JavaTimeModule module = new JavaTimeModule();
            mapper.registerModule(module);
            String requestJson = mapper.writeValueAsString(request);

            return requestJson;
      }
      String id="HD001";

      @Test
      public void testGetListContractCategorySuccess() throws Exception {
            List<ContractCategory> list = new ArrayList<>();
            for(int i=0 ;i<5;i++){
                  ContractCategory contractCategory = new ContractCategory().setId("HD00"+i).setName("hd"+i);
                  list.add(contractCategory);
            }
            Pageable pageable = PageRequest.of(0,20);
            Page<ContractCategory> result = new PageImpl<>(list,pageable,list.size());

            given(contractCategoryRepository.getAllByConditions(null,null, pageable)).willReturn( result);

            mockMvc.perform(get("/v1/contract-categories").contentType(APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").value("HD000"));
      }

      @Test
      public void testCreateContractCategorySuccess() throws Exception {
            given(contractCategoryRepository.save(isA(ContractCategory.class))).willAnswer(i -> i.getArgument(0));
            given(contractCategoryRepository.existsById(id)).willReturn(false);

            mockMvc.perform(post("/v1/contract-categories")
            .contentType(APPLICATION_JSON_UTF8).content(getJsonCreateRequest()))
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));
      }

      @Test
      public void testUpdateContractCategoryFalseWithCompanyNotExists() throws Exception {
            given(contractCategoryRepository.findById(id)).willReturn(Optional.empty());
            mockMvc.perform(patch("/v1/contract-categories/{id}", id).contentType(APPLICATION_JSON_UTF8)
            .content(getJsonUpdateRequest())).andExpect(status().isNotFound())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã loại hợp đồng không tồn tại"));
      }
      @Test
      public void testUpdateContractCategorySuccess() throws Exception{
            ContractCategory contractCategory=
            new ContractCategory().setId(id).setName("HD001").setIsActive(false);
            given(contractCategoryRepository.findById(id)).willReturn(Optional.of(contractCategory));
            given(contractCategoryRepository.save(isA(ContractCategory.class))).willAnswer(i -> i.getArgument(0));
            mockMvc.perform(patch("/v1/contract-categories/{id}", id).contentType(APPLICATION_JSON_UTF8)
            .content(getJsonUpdateRequest())).andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
      }
}
