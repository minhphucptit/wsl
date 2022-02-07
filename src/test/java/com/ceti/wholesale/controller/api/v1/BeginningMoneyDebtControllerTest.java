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

import com.ceti.wholesale.mapper.BeginningMoneyDebtMapper;
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
import com.ceti.wholesale.model.BeginningMoneyDebt;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.repository.BeginningMoneyDebtRepository;
import com.ceti.wholesale.repository.MoneyDebtDetailRepository;
import com.ceti.wholesale.service.BeginningMoneyDebtService;
import com.ceti.wholesale.service.impl.BeginningMoneyDebtServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(BeginningMoneyDebtController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BeginningMoneyDebtControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        BeginningMoneyDebtService moneyDebtService() {
            return new BeginningMoneyDebtServiceImpl();
        }

    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BeginningMoneyDebtRepository moneyDebtRepository;

    @MockBean
    BeginningMoneyDebtMapper beginningMoneyDebtMapper;

    @MockBean
    MoneyDebtDetailRepository moneyDebtDetailRepository;

    private BeginningMoneyDebt beginningMoneyDebt = new BeginningMoneyDebt();

    String jsonCreateRequest = "{\n" +
            "            \"customer_id\": \"KH01\",\n" +
            "            \"inventory\": \"122\",\n" +
            "            \"create_by\": \"Do Van Hung\" , \n" +
            "            \"update_by\": \"Do Van Hung\" , \n" +
            "            \"note\": \"money debt\" , \n" +
            "            \"year\": \"2021\"\n" +
            "}";

    String jsonUpdateRequest = "{\n" +
            "            \"id\": \"12\",\n" +
            "            \"customer_id\": \"KH01\",\n" +
            "            \"inventory\": \"122\",\n" +
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
    public void testCreateMoneyDebtSuccess() throws Exception {
        given(moneyDebtRepository.existsById(id)).willReturn(false);
        given(moneyDebtRepository.save(isA(BeginningMoneyDebt.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(post("/v1/beginning-money-debts") .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.year").value("2021"));
    }

    @Test
    public void testCreateMoneyDebtFalseWithMoneyDebtNotExists() throws Exception {
        given(moneyDebtRepository.existsById("2021KH01FAC1")).willReturn(true);

        mockMvc.perform(post("/v1/beginning-money-debts")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("đã tồn tại bản ghi công nợ tiền của khách hàng KH01 năm 2021"));
    }

    @Test
    public void testUpdateMoneyDebtSuccess() throws Exception {
        given(moneyDebtRepository.findById(id)).willReturn(Optional.ofNullable(beginningMoneyDebt));
        given(moneyDebtRepository.save(isA(BeginningMoneyDebt.class))).willReturn(beginningMoneyDebt);

        mockMvc.perform(patch("/v1/beginning-money-debts/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.inventory").value("122"));
    }

    @Test
    public void testUpdateMoneyDebtWithMoneyDebtNotExist() throws Exception {
        given(moneyDebtRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/beginning-money-debts/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã công nợ không tồn tại"));
    }

    @Test
    public void testUpdateMoneyDebtWithMoneyDebTFalse() throws Exception {
        BeginningMoneyDebt beginningMoneyDebt = new BeginningMoneyDebt();
        beginningMoneyDebt.setId("2021KH0311");
        beginningMoneyDebt.setFactoryId("2");
        given(moneyDebtRepository.findById("2021KH011")).willReturn(Optional.of(beginningMoneyDebt));
        given(moneyDebtRepository.existsById("2021KH012")).willReturn(true);

        mockMvc.perform(patch("/v1/beginning-money-debts/{id}", "2021KH011").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("đã tồn tại bản ghi công nợ tiền của khách hàng KH01 năm 2021"));
    }

    @Test
    public void testGetMoneyDebtSuccess() throws Exception {
        List<Object[]> objects = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            BeginningMoneyDebt beginningMoneyDebt = new BeginningMoneyDebt();
            beginningMoneyDebt.setId("id" + i);
            beginningMoneyDebt.setCreateBy("hung" + i);
            beginningMoneyDebt.setInventory(BigDecimal.valueOf(1000));
            beginningMoneyDebt.setNote("note" + i);
            beginningMoneyDebt.setYear(200 + i);
            beginningMoneyDebt.setUpdateBy("hung" + i);

            Customer customer = new Customer().setCompanyId("company"+i)
                    .setAddress("ha noi"+i).setPhoneNumber("01233432"+i)
                    .setName("ceti"+i).setCategory("ctg"+i).setId("id"+i)
                    .setGroupId("groupId"+i).setTaxCode("code"+i);

            Object[] object = new Object[10];
            object[0] = beginningMoneyDebt;
            object[1] = customer;
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

        given(moneyDebtDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);

        mockMvc.perform(get("/v1/beginning-money-debts")
                .header("department_id","FAC1")
                .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].note").value("note0"));
    }

    @Test
    public void testDeleteMoneyDebtWithMoneyDebtNotExist() throws Exception {
        given(moneyDebtRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/beginning-money-debts/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("đầu kì công nợ tiền khách hàng không tồn tại"));
    }

    @Test
    public void testForwardBeginningMoneyDebtSuccess() throws Exception {
        given(beginningMoneyDebtMapper.setForwardToNextYear(2021,2022,"abc","Phuc")).willReturn(1);

        mockMvc.perform(post("/v1/beginning-money-debts/forward").contentType(APPLICATION_JSON_UTF8)
                .header("department_id","abc")
                .content(jsonForwardCreateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Kết chuyển thành công"));
    }

//    @Test
//    public void testForwardBeginningMoneyDebtFalse() throws Exception {
//        given(beginningMoneyDebtMapper.setForwardToNextYear(2021,2022,"abc","Phuc")).willReturn(null);
//
//        mockMvc.perform(post("/v1/beginning-money-debts/forward").contentType(APPLICATION_JSON_UTF8)
//                .header("department_id","abc")
//                .content(jsonForwardCreateRequest))
//                .andExpect(status().is5xxServerError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_500"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Lỗi dữ liệu"));
//    }

    @Test
    public void testDeleteMoneyDebt() throws Exception {
        given(moneyDebtRepository.existsById(id)).willReturn(true);

        mockMvc.perform(delete("/v1/beginning-money-debts/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }


}
