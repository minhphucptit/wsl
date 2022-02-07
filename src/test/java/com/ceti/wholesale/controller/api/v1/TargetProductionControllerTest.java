package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.dto.TargetProductionDto;
import com.ceti.wholesale.mapper.TargetProductionMapper;
import com.ceti.wholesale.model.*;
import com.ceti.wholesale.repository.*;
import com.ceti.wholesale.service.*;
import com.ceti.wholesale.service.impl.*;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.math.BigDecimal;
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
@WebMvcTest(TargetProductionController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TargetProductionControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        TargetProductionService targetProductionService() {
            return new TargetProductionServiceImpl() {
            };
        }
    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TargetProductionRepository targetProductionRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private TargetProductionMapper targetProductionMapper;

    @MockBean
    private OtherDiscountRepository otherDiscountRepository;

    String jsonCreateRequest = "{\n" +
            "    \"customer_code\":\"customerCode\",\n" +
            "    \"quantity\":123,\n" +
            "    \"month\":12,\n" +
            "    \"year\":2021,\n" +
            "    \"rowLocation\":\"1\"\n" +
                "}";

    String jsonUpdateRequest = "{\n" +
            "    \"customer_code\":\"customerCode\",\n" +
            "    \"quantity\":123,\n" +
            "    \"month\":12,\n" +
            "    \"year\":2021\n" +
            "}";

    String jsonUpdateNullQuantityRequest = "{\n" +
            "    \"customer_code\":\"customerCode\",\n" +
            "    \"month\":12,\n" +
            "    \"year\":2021\n" +
            "}";

    String jsonUpdateNullCustomerCodeRequest = "{\n" +
            "    \"quantity\":123,\n" +
            "    \"month\":12,\n" +
            "    \"year\":2021\n" +
            "}";

    String jsonUpdateNullMonthRequest = "{\n" +
            "    \"customer_code\":\"customerCode\",\n" +
            "    \"quantity\":123,\n" +
            "    \"year\":2021\n" +
            "}";

    String jsonUpdateNulYearRequest = "{\n" +
            "    \"customer_code\":\"customerCode\",\n" +
            "    \"quantity\":123,\n" +
            "    \"month\":12\n" +
            "}";

    String jsonDeleteRequest = "{\n" +
            "    \"customer_code\":\"customerCode\",\n" +
            "    \"month\":12,\n" +
            "    \"year\":2021\n" +
            "}";

    private Instant day = Instant.ofEpochSecond(1630454400l);
    private String id = "2c9c80887c30b498017c349b2d500001";

    @Test
    public void testUpdateTargetProduction() throws Exception{
        //testUpdateTargetProductionExistsByCustomerCodeAndMonthAndYear
        given(targetProductionRepository.existsByCustomerCodeAndMonthAndYear("customerCode",11,2020)).willReturn(false);
        mockMvc.perform(patch("/v1/target-productions")
                        .header("user_id","FAC1")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Chỉ tiêu của khách customerCode ở tháng 12 năm 2021 này không tồn tại"));

        //testUpdateTargetProductionByCustomerCodeAndMonthAndYear Success
        TargetProduction targetProduction = new TargetProduction();
        targetProduction.setCustomerCode("CustomerCode");
        targetProduction.setMonth(12);
        targetProduction.setYear(2021);
        targetProduction.setQuantity(BigDecimal.valueOf(123));
        given(targetProductionRepository.existsByCustomerCodeAndMonthAndYear("customerCode",12,2021)).willReturn(true);
        given(targetProductionRepository.findByCustomerCodeAndMonthAndYear("customerCode",12,2021)).willReturn(targetProduction);
        given(targetProductionRepository.save(isA(TargetProduction.class))).willAnswer(i-> i.getArgument(0));

        mockMvc.perform(patch("/v1/target-productions")
                        .header("user_id","FAC1")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));

        //UpdateTargetProduction null quantity
        given(targetProductionRepository.existsByCustomerCodeAndMonthAndYear("customerCode",12,2021)).willReturn(true);
        mockMvc.perform(patch("/v1/target-productions")
                        .header("user_id","FAC1")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonUpdateNullQuantityRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[quantity can not be null]"));

        //UpdateTargetProduction null customerCode
        given(targetProductionRepository.existsByCustomerCodeAndMonthAndYear(null,12,2021)).willReturn(true);
        mockMvc.perform(patch("/v1/target-productions")
                        .header("user_id","FAC1")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonUpdateNullCustomerCodeRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[customerCode can not be null]"));

        //UpdateTargetProduction null month
        given(targetProductionRepository.existsByCustomerCodeAndMonthAndYear("customerCode",null,2021)).willReturn(true);
        mockMvc.perform(patch("/v1/target-productions")
                        .header("user_id","FAC1")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonUpdateNullMonthRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[month can not be null]"));

        //UpdateTargetProduction null year
        given(targetProductionRepository.existsByCustomerCodeAndMonthAndYear("customerCode",12,null)).willReturn(true);
        mockMvc.perform(patch("/v1/target-productions")
                        .header("user_id","FAC1")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonUpdateNulYearRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[year can not be null]"));

    }


    @Test
    public void testCreateTargetProduction() throws Exception{
        //testCreateTargetProduction ByCustomerCodeAndMonthAndYear Success
        given(targetProductionRepository.save(isA(TargetProduction.class))).willAnswer(i-> i.getArgument(0));

        mockMvc.perform(post("/v1/target-productions")
                .header("user_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));

        //testCreateTargetProduction ExistsByCustomerCodeAndMonthAndYear
        given(targetProductionRepository.existsByCustomerCodeAndMonthAndYear("customerCode",12,2021)).willReturn(true);

        mockMvc.perform(post("/v1/target-productions")
                        .header("user_id","FAC1")
                        .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Khách hàng customerCode ở tháng 12 năm 2021 này đã tồn tại"));
    }

    @Test
    public void testGetAllSuccess() throws Exception {
        List<TargetProductionDto> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TargetProductionDto item = new TargetProductionDto().setCreateAt(Instant.ofEpochSecond(1634273946 + i)).setCreateBy("CreateBy" + i).setCustomerCode("CustomerCode" + i)
                    .setCustomerName("CustomerName" + i).setMonth(1 + i).setQuantity(BigDecimal.valueOf(1 + i)).setYear(2021 + i);
            list.add(item);
        }
        Pageable pageable = PageRequest.of(0, 20);
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("customer_code","customerCode");
        where.add("customer_full_name","customerFullName");
        where.add("month_from","10");
        where.add("month_to","12");
        where.add("year_from","2010");
        where.add("year_to","2012");
        given(targetProductionMapper.getList("customerCode","customerFullName",10,12, 2010,2012,pageable.getOffset(), pageable.getPageSize())).willReturn(list);
        given(targetProductionMapper.countList("customerCode","customerFullName",10,12, 2010,2012)).willReturn(5l);

        mockMvc.perform(get("/v1/target-productions").params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(jsonPath("$.items[0].month").value("1"));
    }


    @Test
    public void testDelete() throws Exception {
        //khi tồn tại TargetProduction
        TargetProduction targetProduction = new TargetProduction();
        targetProduction.setCustomerCode("CustomerCode");
        targetProduction.setMonth(12);
        targetProduction.setYear(2021);
        targetProduction.setQuantity(BigDecimal.valueOf(123));
        given(targetProductionRepository.existsByCustomerCodeAndMonthAndYear("customerCode",12,2021)).willReturn(true);
        given(targetProductionRepository.findByCustomerCodeAndMonthAndYear("customerCode",12,2021)).willReturn(targetProduction);
        mockMvc.perform(delete("/v1/target-productions")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonDeleteRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));

        //khi không tồn tại TargetProduction
        given(targetProductionRepository.existsByCustomerCodeAndMonthAndYear("customerCode",12,2021)).willReturn(false);
        mockMvc.perform(delete("/v1/target-productions")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonDeleteRequest))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Khách hàng customerCode ở tháng 12 năm 2021 không tồn tại"));
    }
}
