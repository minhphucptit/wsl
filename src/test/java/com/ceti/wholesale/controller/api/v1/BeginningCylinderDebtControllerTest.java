package com.ceti.wholesale.controller.api.v1;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ceti.wholesale.mapper.BeginningCylinderDebtMapper;
import com.ceti.wholesale.repository.FactoryRepository;
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

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.BeginningCylinderDebt;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.Product;
import com.ceti.wholesale.repository.BeginningCylinderDebtDetailRepository;
import com.ceti.wholesale.repository.BeginningCylinderDebtRepository;
import com.ceti.wholesale.service.BeginningCylinderDebtService;
import com.ceti.wholesale.service.impl.BeginningCylinderDebtServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(BeginningCylinderDebtController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BeginningCylinderDebtControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        BeginningCylinderDebtService beginningCylinderDebtService() {
            return new BeginningCylinderDebtServiceImpl();
        }

    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BeginningCylinderDebtRepository beginningCylinderDebtRepository;

    @MockBean
    BeginningCylinderDebtMapper beginningCylinderDebtMapper;

    @MockBean
    BeginningCylinderDebtDetailRepository beginningCylinderDebtDetailRepository;

    private BeginningCylinderDebt beginningCylinderDebt = new BeginningCylinderDebt();

    String jsonCreateRequest = "{\n" +
            "            \"customer_id\": \"KH01\",\n" +
            "            \"inventory\": \"122\",\n" +
            "            \"product_id\": \"romano\" , \n" +
            "            \"update_by\": \"Do Van Hung\" , \n" +
            "            \"note\": \"money debt\" , \n" +
            "            \"year\": \"2021\"\n" +
            "}";

    String jsonUpdateRequest = "{\n" +
            "            \"customer_id\": \"KH01\",\n" +
            "            \"inventory\": \"122\",\n" +
            "            \"product_id\": \"romano\" , \n" +
            "            \"update_by\": \"Do Van Hung\" , \n" +
            "            \"note\": \"money debt\" , \n" +
            "            \"factory_id\": \"1\" , \n" +
            "            \"year\": \"2021\"\n" +
            "}";

    String jsonForwardCreateRequest = "{\n" +
            "            \"year_from\":\"2021\",\n" +
            "            \"year_to\":\"2022\",\n" +
            "            \"user_full_name\":\"Phuc\"\n" +
            "}";
    String id = "KH01";


    //test create receipt payment category success
    @Test
    public void testCreateBeginningCylinderDebtSuccess() throws Exception {
        given(beginningCylinderDebtRepository.existsById(id)).willReturn(false);
        given(beginningCylinderDebtRepository.save(isA(BeginningCylinderDebt.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(post("/v1/beginning-cylinder-debts")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.year").value("2021"));
    }

    @Test
    public void testCreateBeginningCylinderDebtFalseWithBeginningCylinderDebtNotExists() throws Exception {
        given(beginningCylinderDebtRepository.existsById("2021KH01romanoFAC1")).willReturn(true);

        mockMvc.perform(post("/v1/beginning-cylinder-debts")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Đã tồn tại bản ghi đầu kì công nợ vỏ romano của khách hàng KH01"));
    }

    @Test
    public void testUpdateBeginningCylinderDebtSuccess() throws Exception {
        given(beginningCylinderDebtRepository.findById(id)).willReturn(Optional.ofNullable(beginningCylinderDebt));
        given(beginningCylinderDebtRepository.save(isA(BeginningCylinderDebt.class))).willAnswer(i-> i.getArgument(0));

        mockMvc.perform(patch("/v1/beginning-cylinder-debts/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.inventory").value("122"));
    }

    @Test
    public void testUpdateBeginningCylinderDebtWithBeginningCylinderDebtNotExist() throws Exception {
        given(beginningCylinderDebtRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/beginning-cylinder-debts/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("đầu kì công nợ vỏ khách hàng không tồn tại"));
    }

    @Test
    public void testGetBeginningCylinderDebtSuccess() throws Exception {
        List<Object[]> objects = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BeginningCylinderDebt beginningCylinderDebt = new BeginningCylinderDebt();
            beginningCylinderDebt.setId("id" + i);
            beginningCylinderDebt.setCreateBy("hung" + i);
            beginningCylinderDebt.setInventory(BigDecimal.valueOf(1000));
            beginningCylinderDebt.setNote("note" + i);
            beginningCylinderDebt.setYear(200 + i);
            beginningCylinderDebt.setUpdateBy("hung" + i);

            Customer customer = new Customer().setCompanyId("company"+i)
                    .setAddress("ha noi"+i).setPhoneNumber("01233432"+i)
                    .setName("ceti"+i).setCategory("ctg"+i).setId("id"+i)
                    .setGroupId("groupId"+i).setTaxCode("code"+i);

            Product product = new Product().setReferenceProductId("romano")
                    .setId("pd01").setCategoryId("rm01").setName("romano").setUnit("VND").setPurpose("nothing")
                    .setWeight(BigDecimal.valueOf(100)).setType("1");

            Object[] object = new Object[10];
            object[0] = beginningCylinderDebt;
            object[1] = customer;
            object[2] = product;
            objects.add(object);

        }

        Pageable pageable = PageRequest.of(0, 20);

        ResultPage<Object[]> result = new ResultPage<Object[]>();
        result.setPageList(objects);
        result.setTotalItems(5);

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("note","note");

        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
        where1.add("note","note");
        where1.add("factory_id","FAC1");

        given(beginningCylinderDebtDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);

        mockMvc.perform(get("/v1/beginning-cylinder-debts")
                .header("department_id","FAC1")
                .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].note").value("note0"));
    }

    @Test
    public void testDeleteBeginningCylinderDebtWithBeginningCylinderDebtNotExist() throws Exception {
        given(beginningCylinderDebtRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/beginning-cylinder-debts/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("đầu kì công nợ vỏ khách hàng không tồn tại"));
    }

    @Test
    public void testForwardBeginningCylinderDebtSuccess() throws Exception {
        given(beginningCylinderDebtMapper.setForwardToNextYear(2021,2022,"abc","Phuc")).willReturn(1);

        mockMvc.perform(post("/v1/beginning-cylinder-debts/forward").contentType(APPLICATION_JSON_UTF8)
                .header("department_id","abc")
                .content(jsonForwardCreateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Kết chuyển thành công"));
    }

    @Test
    public void testForwardBeginningCylinderDebtFalse() throws Exception {
        given(beginningCylinderDebtMapper.setForwardToNextYear(2021,2022,"abc","Phuc")).willReturn(null);
        mockMvc.perform(post("/v1/beginning-cylinder-debts/forward").contentType(APPLICATION_JSON_UTF8)
                        .header("department_id","abc")
                        .content(jsonForwardCreateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Kết chuyển thành công"));
    }

    @Test
    public void testDeleteBeginningCylinderDebtSucess() throws Exception {
        BeginningCylinderDebt beginningCylinderDebt = new BeginningCylinderDebt();
        beginningCylinderDebt.setId("1");
        given(beginningCylinderDebtRepository.existsById("1")).willReturn(true);

        mockMvc.perform(delete("/v1/beginning-cylinder-debts/{id}", "1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }

    @Test
    public void testUpdateBeginningCylinderDebtFalse() throws Exception {
        BeginningCylinderDebt beginningCylinderDebt = new BeginningCylinderDebt();
        beginningCylinderDebt.setId("2021KH01romano2");
        beginningCylinderDebt.setFactoryId("1");
        given(beginningCylinderDebtRepository.findById("2021KH01romano1")).willReturn(Optional.of(beginningCylinderDebt));
        given(beginningCylinderDebtRepository.existsById("2021KH01romano1")).willReturn(true);

        mockMvc.perform(patch("/v1/beginning-cylinder-debts/{id}", "2021KH01romano1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Đã tồn tại bản ghi đầu kì công nợ vỏ romano của khách hàng KH01"));
    }

}

