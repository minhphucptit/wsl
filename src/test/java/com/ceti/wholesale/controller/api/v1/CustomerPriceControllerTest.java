package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.controller.api.request.UpdateCustomerPriceRequest;
import com.ceti.wholesale.model.CustomerPrice;
import com.ceti.wholesale.repository.CustomerPriceDetailRepository;
import com.ceti.wholesale.repository.CustomerPriceRepository;
import com.ceti.wholesale.repository.CustomerRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.service.CustomerPriceService;
import com.ceti.wholesale.service.impl.CustomerPriceServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerPriceController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerPriceControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {

        @Bean
        CustomerPriceService customerPriceService() {
            return new CustomerPriceServiceImpl();
        }
    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerPriceRepository customerPriceRepository;
    @MockBean
    private CustomerPriceDetailRepository customerPriceDetailRepository;
    @MockBean
    private CustomerRepository customerRepository;

    private String jsonCreateRequest = "{\n" +
            "            \"customer_id\": \"KH01\",\n" +
            "            \"customer_name\": \"Hà Ðang Biên\",\n" +
            "            \"group_id\": \"T02\",\n" +
            "            \"group_name\": null,\n" +
            "            \"created_at\": \"03-2021\",\n" +
            "            \"update_date\": \"03-2021\",\n" +
            "            \"month\": 11,\n" +
            "            \"year\": 2021,\n" +
            "            \"day01\": 1000.00,\n" +
            "            \"day02\": 2000.00,\n" +
            "            \"day03\": 3000.00,\n" +
            "            \"day04\": 4000.00\n" +
            "        }";
    private String jsonCreateRequest1 = "[{\n" +
            "            \"customer_id\": \"KH01\",\n" +
            "            \"customer_name\": \"Hà Ðang Biên\",\n" +
            "            \"group_id\": \"T02\",\n" +
            "            \"group_name\": null,\n" +
            "            \"created_at\": \"03-2021\",\n" +
            "            \"update_date\": \"03-2021\",\n" +
            "            \"day01\": 1000.00,\n" +
            "            \"day02\": 2000.00,\n" +
            "            \"day03\": 3000.00,\n" +
            "            \"day04\": 4000.00\n" +
            "        }]";
    private String jsonUpdateRequest = "[{\n" +
            "            \"id\": \"2c9c8086783a491101783f57ce8d0038\",\n" +
            "            \"customer_id\": \"KH01\",\n" +
            "            \"customer_name\": \"Hà Ðang Biên\",\n" +
            "            \"group_id\": \"T02\",\n" +
            "            \"group_name\": null,\n" +
            "            \"update_date\": \"03-2021\",\n" +
            "            \"day01\": 1000.00,\n" +
            "            \"day02\": 2000.00,\n" +
            "            \"day03\": 3000.00,\n" +
            "            \"day04\": 4000.00\n" +
            "        }]";


    String jsonForwardCreateRequest = "{\n" +
            "            \"month_from\":\"5\",\n" +
            "            \"year_from\":\"2020\",\n" +
            "            \"month_to\":\"5\",\n" +
            "            \"year_to\":\"2023\"\n" +
            "}";

    @Value(value = "${ceti.service.zoneId}")
    private String zoneId;

    private String id = "2c9c80887c30b498017c349b2d500001";

    @Test
    public void testGetCustomerPricesSuccess() throws Exception {
        List<CustomerPrice> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CustomerPrice customerPrice = new CustomerPrice();
            customerPrice.setCustomerId("A" + i);
            list.add(customerPrice);
        }
        Pageable pageable = PageRequest.of(0, 20);
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();

        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
        where1.add("factory_id", "FAC1");


        Page<CustomerPrice> result = new PageImpl<CustomerPrice>(list, pageable, list.size());
        given(customerPriceRepository.getAllByCondition(null, null, null, null, "FAC1", pageable)).willReturn(result);
        mockMvc.perform(get("/v1/customer-prices")
                .header("department_id", "FAC1")
                .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @Test
    public void testCreateCustomerPriceFalseWithCustomerIdExists() throws Exception {
        given(customerPriceRepository.existsByCustomerIdAndMonthAndYearAndFactoryId("KH01", 11, 2021, "FAC1")).willReturn(true);
        mockMvc.perform(post("/v1/customer-prices")
                .header("department_id", "FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Khách hàng đã tồn tại"));
    }

    @Test
    public void testCreateCustomerPriceSuccess() throws Exception {
        given(customerPriceRepository.save(isA(CustomerPrice.class))).willAnswer(i -> i.getArgument(0));
        given(customerPriceRepository.existsByCustomerIdAndMonthAndYearAndFactoryId("KH01", 1, 2021,"FAC1")).willReturn(false);
        mockMvc.perform(post("/v1/customer-prices")
                .header("department_id", "FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));
    }

    @Test
    public void testUpdateCustomerGroupSuccess() throws Exception {
        List<CustomerPrice> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CustomerPrice customerPrice = new CustomerPrice();
            customerPrice.setId("2c9c8086783a491101783f57ce8d0038");
            customerPrice.setDay02(BigDecimal.valueOf(10));
            list.add(customerPrice);
            customerPriceRepository.save(customerPrice);
        }
        CustomerPrice customerPrice1 = new CustomerPrice();
        customerPrice1.setId("2c9c8086783a491101783f57ce8d0038");
        customerPrice1.setDay02(BigDecimal.valueOf(10));
        given(customerPriceRepository.save(isA(CustomerPrice.class))).willAnswer(i -> i.getArgument(0));
        given(customerPriceRepository.findById("2c9c8086783a491101783f57ce8d0038")).willReturn(Optional.of(customerPrice1));
        mockMvc.perform(patch("/v1/customer-prices").header("department_id", "FAC1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @Test
    public void testForwardCustomerPriceDebtFalse() throws Exception {
        given(customerPriceRepository.setForwardCustomerPrice(5, 2020, 5, 2023, "abc\"")).willReturn(false);

        mockMvc.perform(post("/v1/customer-prices/forward").contentType(APPLICATION_JSON_UTF8)
                .header("department_id", "abc")
                .content(jsonForwardCreateRequest))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Tháng 5 chưa có giá"));
    }

    @Test
    public void testForwardCustomerPriceDebtNull() throws Exception {
        given(customerPriceRepository.setForwardCustomerPrice(5, 2020, 5, 2023, "abc")).willReturn(null);

        mockMvc.perform(post("/v1/customer-prices/forward").contentType(APPLICATION_JSON_UTF8)
                .header("department_id", "abc")
                .content(jsonForwardCreateRequest))
                .andExpect(status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_500"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Lỗi dữ liệu"));
    }

    @Test
    public void testForwardCustomerPriceDebtSuccess() throws Exception {
        given(customerPriceRepository.setForwardCustomerPrice(5, 2020, 5, 2023, "abc")).willReturn(true);

        mockMvc.perform(post("/v1/customer-prices/forward").contentType(APPLICATION_JSON_UTF8)
                .header("department_id", "abc")
                .content(jsonForwardCreateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Kết chuyển thành công"));
    }

    @Test
    public void testApplyCustomerPriceDebtSuccess() throws Exception {
        given(customerPriceRepository.applyCustomerPrice(5, 2023, "abc")).willReturn(true);

        mockMvc.perform(post("/v1/customer-prices/apply").contentType(APPLICATION_JSON_UTF8)
                .header("department_id", "abc")
                .content(jsonForwardCreateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Áp dụng giá thành công"));
    }

    @Test
    public void testApplyCustomerPriceDebtNull() throws Exception {
        given(customerPriceRepository.applyCustomerPrice(5, 2023, "abc")).willReturn(null);

        mockMvc.perform(post("/v1/customer-prices/apply").contentType(APPLICATION_JSON_UTF8)
                .header("department_id", "abc")
                .content(jsonForwardCreateRequest))
                .andExpect(status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_500"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Lỗi dữ liệu"));
    }


    @Test
    public void testDeleteCustomerPrice() throws Exception {
        given(customerPriceRepository.existsById("2c9c8086783a491101783f57ce8d0038")).willReturn(true);

        mockMvc.perform(post("/v1/customer-prices/delete").contentType(APPLICATION_JSON_UTF8)
                        .header("department_id", "abc")
                        .content(jsonUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Xóa giá thành công"));
    }

}
