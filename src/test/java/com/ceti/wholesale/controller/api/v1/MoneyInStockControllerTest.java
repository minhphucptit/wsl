package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.mapper.MoneyInStockMapper;
import com.ceti.wholesale.model.*;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.MoneyInStockRepository;
import com.ceti.wholesale.service.MoneyInStockService;
import com.ceti.wholesale.service.impl.MoneyInStockServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.ArgumentMatchers.isA;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MoneyInStockController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MoneyInStockControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        MoneyInStockService moneyInStockService() {
            return new MoneyInStockServiceImpl();
        }

    }

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;
    @MockBean
    MoneyInStockRepository moneyInStockRepository;

    @MockBean
    MoneyInStockMapper moneyInStockMapper;

    private MoneyInStock moneyInStock = new MoneyInStock();

    String jsonCreateRequest = "{\n" +
            "            \"inventory\": \"122\",\n" +
            "            \"note\": \"money debt\" , \n" +
            "            \"year\": \"2021\"\n" +
            "}";

    String jsonUpdateRequest = "{\n" +
            "            \"inventory\": \"122\",\n" +
            "            \"note\": \"money debt\"  \n" +
            "}";
    String jsonForwardCreateRequest = "{\n" +
            "            \"year_from\":\"2021\",\n" +
            "            \"year_to\":\"2022\",\n" +
            "            \"user_full_name\":\"Phuc\"\n" +
            "}";
    String id = "2021";


    //test create receipt payment category success
    @Test
    public void testCreateMoneyDebtSuccess() throws Exception {
        given(moneyInStockRepository.existsById(id)).willReturn(false);
        given(moneyInStockRepository.save(isA(MoneyInStock.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(post("/v1/money-in-stocks")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.year").value("2021"));
    }

    @Test
    public void testCreateMoneyDebtFalseWithMoneyDebtNotExists() throws Exception {
        given(moneyInStockRepository.existsById(id+"-"+"FAC1")).willReturn(true);

        mockMvc.perform(post("/v1/money-in-stocks")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Quỹ tiền mặt đã tồn tại"));
    }

    @Test
    public void testUpdateMoneyDebtSuccess() throws Exception {
        given(moneyInStockRepository.findById(id)).willReturn(Optional.ofNullable(moneyInStock));
        given(moneyInStockRepository.save(isA(MoneyInStock.class))).willReturn(moneyInStock);

        mockMvc.perform(patch("/v1/money-in-stocks/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.inventory").value("122"));
    }

    @Test
    public void testUpdateMoneyDebtWithMoneyDebtNotExist() throws Exception {
        given(moneyInStockRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/money-in-stocks/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Quỹ tiền mặt không tồn tại"));
    }

    @Test
    public void testGetLisReceiptPaymentCategorySuccess() throws Exception {

        List<MoneyInStock> list = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            MoneyInStock moneyInStock = new MoneyInStock();
            moneyInStock.setYear(2000+i);
            moneyInStock.setInventory(BigDecimal.valueOf(2000));
            moneyInStock.setNote("note"+i);
            list.add(moneyInStock);
        }
        Pageable pageable = PageRequest.of(0, 20);

        Page<MoneyInStock> rs = new PageImpl<MoneyInStock>(list,pageable, list.size());

        given(moneyInStockRepository.getAllByCondition("FAC1",pageable)).willReturn(rs);

        mockMvc.perform(get("/v1/money-in-stocks").header("department_id","FAC1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].note").value("note0"));

    }

    @Test
    public void testDeleteMoneyDebtWithMoneyDebtNotExist() throws Exception {
        given(moneyInStockRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/money-in-stocks/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("đầu kì Quỹ tiền mặt không tồn tại"));
    }

    @Test
    public void testForwardMoneyInStockSuccess() throws Exception {
        given(moneyInStockMapper.setForwardToNextYear(2021,2022,"abc","Phuc")).willReturn(1);

        mockMvc.perform(post("/v1/money-in-stocks/forward").contentType(APPLICATION_JSON_UTF8)
                .header("department_id","abc")
                .content(jsonForwardCreateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Kết chuyển thành công"));
    }

//    @Test
//    public void testForwardMoneyInStockFalse() throws Exception {
//        given(moneyInStockMapper.setForwardToNextYear(2021,2022,"abc","Phuc")).willReturn(null);
//
//        mockMvc.perform(post("/v1/money-in-stocks/forward").contentType(APPLICATION_JSON_UTF8)
//                .header("department_id","abc")
//                .content(jsonForwardCreateRequest))
//                .andExpect(status().is5xxServerError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_500"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Lỗi dữ liệu"));
//    }

    @Test
    public void deleteMoneyInStockSuccess() throws Exception {
        given(moneyInStockRepository.existsById("1")).willReturn(true);

        mockMvc.perform(delete("/v1/money-in-stocks/{id}", "1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonForwardCreateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }
}
