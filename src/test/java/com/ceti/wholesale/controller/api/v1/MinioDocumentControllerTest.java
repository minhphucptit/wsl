package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.controller.api.request.CreateContractRequest;
import com.ceti.wholesale.controller.api.request.UpdateContractRequest;
import com.ceti.wholesale.model.MinioDocument;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.MinioDocumentRepository;
import com.ceti.wholesale.service.MinioDocumentService;
import com.ceti.wholesale.service.impl.MinioDocumentServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MinioDocumentController.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class MinioDocumentControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf-8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        MinioDocumentService minioDocumentService() {
            return new MinioDocumentServiceImpl();
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;

    @MockBean
    private MinioDocumentRepository minioDocumentRepository;

    private String userId = "nhat1";
    private String entityId = "minio";

    String jsonCreateRequest = "{\n" +
            "            \"bucket\": \"legal\",\n" +
            "            \"object_key\": \"12\",\n" +
            "            \"object_name\": \"Feel it\" , \n" +
            "            \"upload_at\": \"2021-04-02T00:01:10.099999999Z\" , \n" +
            "            \"upload_by\": \"minh\" , \n" +
            "            \"entity\": \"entity12\",\n" +
            "            \"entity_id\": \"minio\" , \n" +
            "            \"file_extension\": \"nhat\" \n" +
            "}";

    String jsonUpdateRequest = "{\n" +
            "\"object_key\": \"dom\",\n" +
            "\"object_name\": \"king\",\n" +
            "\"bucket\": \"business\",\n" +
            "\"entity\": \"entity\",\n" +
            "\"entity_id\": \"minio\" , \n" +
            "\"file_extension\": \"exe\", \n" +
            "\"upload_at\": \"2021-04-02T00:01:10.099999999Z\"\n" +
            "}";

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
      public void testGetListMinioDocumentSuccess() throws Exception {
            List<MinioDocument> list = new ArrayList<>();
            for(int i=0 ;i<5;i++){
                MinioDocument minioDocument = new MinioDocument().setEntityId("entityId").setId("id"+i);
                  list.add(minioDocument);
            }
            Pageable pageable = PageRequest.of(0, 20);
            Page<MinioDocument> page = new PageImpl<>(list,pageable,list.size());
            given(minioDocumentRepository.findAll("entityId",pageable)).willReturn(page);
            mockMvc.perform(get("/v1/minio-documents").param("entity_id", "entityId").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"));
    }

    @Test
    public void createMinioDocumentSuccess() throws Exception {
        given(minioDocumentRepository.save(isA(MinioDocument.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(post(("/v1/minio-documents")).header("user_id", userId)
                        .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(("$.code")).value("R_201"))
                .andExpect(jsonPath("$.total_items").value("0"));
    }

    @Test
    public void updateMinioDocumentSuccess() throws Exception{
        MinioDocument minioDocument = new MinioDocument();
        minioDocument.setId("id1").setEntityId("minio");

        given(minioDocumentRepository.findById("id1")).willReturn(Optional.of(minioDocument));
        given(minioDocumentRepository.save(isA(MinioDocument.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(patch("/v1/minio-documents/{id}", "id1").header("user_id", userId)
                        .content(jsonUpdateRequest).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value("0"));
    }

    @Test
    public void testUpdateMinioDocumentFail() throws Exception {
        given(minioDocumentRepository.findById("id01")).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/minio-documents/{id}", "id01").header("user_id", userId)
                        .content(jsonUpdateRequest).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("R_404"))
                .andExpect(jsonPath("$.total_items").value("0"));
    }

    @Test
    public void deleteMinioDocument() throws Exception {
        //khi tồn tại tài liệu
        given(minioDocumentRepository.existsById(id)).willReturn(true);
        mockMvc.perform(delete("/v1/minio-documents/{id}",id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));

        //khi tài liệu không tồn tại
        given(minioDocumentRepository.findById("id1")).willReturn(Optional.empty());
        mockMvc.perform(delete("/v1/minio-documents/{id}", "id1")
                        .contentType(APPLICATION_JSON_UTF8)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("R_404"))
                .andExpect(jsonPath("$.message").value("Tài liệu không tồn tại"));
    }
}
