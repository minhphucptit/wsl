package com.ceti.wholesale.controller.api.v1;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateContractRequest;
import com.ceti.wholesale.controller.api.request.UpdateContractRequest;
import com.ceti.wholesale.dto.ContractDto;
import com.ceti.wholesale.mapper.ContractMapper;
import com.ceti.wholesale.model.Contract;
import com.ceti.wholesale.repository.ContractCategoryRepository;
import com.ceti.wholesale.repository.ContractRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.service.ContractService;
import com.ceti.wholesale.service.impl.ContractServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.nio.charset.Charset;
import java.time.Instant;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(ContractController.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class ContractControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf-8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        ContractService contractService() {
            return new ContractServiceImpl();
        }
    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractRepository contractRepository;

    @MockBean
    ContractCategoryRepository contractCategoryRepository;
    
    @MockBean
    private ContractMapper contractMapper;

    //common value
    private String getJsonCreateRequest() throws JsonProcessingException {
        CreateContractRequest request =
                new CreateContractRequest().setContractNumber("111").setContractCategoryId("1").setDeliveryMethod("1").setSignDate(
                        Instant.ofEpochSecond(100000)).setExpireDate(Instant.ofEpochSecond(110000));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        String requestJson = mapper.writeValueAsString(request);

        return requestJson;
    }

    private String getJsonUpdateRequest() throws JsonProcessingException {
        UpdateContractRequest request =
                new UpdateContractRequest().setContractNumber("1").setContractCategoryId("1").setDeliveryMethod("1").setSignDate(
                        Instant.ofEpochSecond(100000)).setExpireDate(Instant.ofEpochSecond(110000)).setStatus("APPROVED");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        String requestJson = mapper.writeValueAsString(request);

        return requestJson;
    }

    String id = "111";

      @Test
      public void testGetListContractSuccess() throws Exception {
            List<ContractDto> list = new ArrayList<>();
            for(int i=0 ;i<5;i++){
            	ContractDto contract = new ContractDto().setContractNumber("11"+i).setContractCategoryId("1");
                  list.add(contract);
            }
            String pagingStr = PageableProcess.PageToSqlQuery(PageRequest.of(0, 20), "c1");
        given(contractMapper.getList(null, null, null, null, null, null, null, null, "1", null, pagingStr)).willReturn(list);
        given(contractMapper.countList(null, null, null, null, null, null, null, null, "1", null)).willReturn((long)list.size());

        mockMvc.perform(get("/v1/contracts").param("factory_id", "1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].contract_number").value("110"));
    }

    @Test
    public void testCreateContractSuccess() throws Exception {
        given(contractRepository.existsByContractNumberAndFactoryId(id, "1")).willReturn(false);
        given(contractCategoryRepository.existsById("1")).willReturn(true);
        given(contractRepository.save(isA(Contract.class))).willAnswer(i -> i.getArgument(0));


        mockMvc.perform(post("/v1/contracts")
                .header("department_id", "1")
                .contentType(APPLICATION_JSON_UTF8).content(getJsonCreateRequest()))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));
    }

    @Test
    public void testCreateContractFalseWithContractCategoryIdNotExist() throws Exception {
        given(contractRepository.existsByContractNumberAndFactoryId(id, "1")).willReturn(false);
        given(contractCategoryRepository.existsById("1")).willReturn(false);

        mockMvc.perform(post("/v1/contracts").header("department_id", "1").contentType(APPLICATION_JSON_UTF8).content(getJsonCreateRequest()))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã loại hợp đồng không tồn tại"));
    }

    @Test
    public void testUpdateContractFalseWithFalseExists() throws Exception {
        given(contractRepository.findById(id)).willReturn(Optional.empty());
        mockMvc.perform(patch("/v1/contracts/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(getJsonUpdateRequest())).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã hợp đồng không tồn tại"));
    }

    @Test
    public void testUpdateContractSuccess() throws Exception {
        Contract contract =
                new Contract().setContractNumber(id).setContractCategoryId("1").setFactoryId("1");
        given(contractRepository.findById(id)).willReturn(Optional.of(contract));
        given(contractRepository.save(isA(Contract.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(patch("/v1/contracts/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(getJsonUpdateRequest())).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }
}
