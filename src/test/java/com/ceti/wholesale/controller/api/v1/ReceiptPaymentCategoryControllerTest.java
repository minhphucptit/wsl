package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.model.ReceiptPaymentCategory;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.ReceiptPaymentCategoryRepository;
import com.ceti.wholesale.service.ReceiptPaymentCategoryService;
import com.ceti.wholesale.service.impl.ReceiptPaymentCategoryServiceImpl;
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ReceiptPaymentCategoryController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ReceiptPaymentCategoryControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        ReceiptPaymentCategoryService receiptPaymentCategoryService() {
            return new ReceiptPaymentCategoryServiceImpl();
        }

    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReceiptPaymentCategoryRepository receiptPaymentCategoryRepository;

    private ReceiptPaymentCategory  receiptPaymentCategory;

    String jsonCreateRequest = "{\n" +
            "            \"id\": \"rpc_001\",\n" +
            "            \"name\": \"receipt_pr_001\",\n" +
            "            \"type\": \"true\"\n" +
            "}";
    String id = "rpc_001";


    //test create receipt payment category success
    @Test
    public void testCreateReceiptPaymentCategorySuccess() throws Exception{
        receiptPaymentCategory = new ReceiptPaymentCategory();
        receiptPaymentCategory.setId("rpc_001");
        receiptPaymentCategory.setName("receipt_pr_001");
        receiptPaymentCategory.setType(true);
        given(receiptPaymentCategoryRepository.existsById("$.item.id")).willReturn(false);
        given(receiptPaymentCategoryRepository.save(receiptPaymentCategory)).willReturn(receiptPaymentCategory);

        mockMvc.perform(post("/v1/receipt-payment-categories").contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("receipt_pr_001"));
    }

    @Test
    public void testCreateReceiptPaymentCategoryFalseWithCompanyNotExists() throws Exception{
        given(receiptPaymentCategoryRepository.existsById(id)).willReturn(true);

        mockMvc.perform(post("/v1/receipt-payment-categories").contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã loại thu chi đã tồn tại"));
    }

    @Test
    public void testUpdateReceiptPaymentCategorySuccess() throws Exception{
        receiptPaymentCategory = new ReceiptPaymentCategory();
        receiptPaymentCategory.setId("rpc_001");
        receiptPaymentCategory.setName("receipt_pr_001");
        receiptPaymentCategory.setType(true);
        given(receiptPaymentCategoryRepository.findById(id)).willReturn(Optional.ofNullable(receiptPaymentCategory));
        given(receiptPaymentCategoryRepository.save(receiptPaymentCategory)).willReturn(receiptPaymentCategory);

        mockMvc.perform(patch("/v1/receipt-payment-categories/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("receipt_pr_001"));
    }

    @Test
    public void testUpdateReceiptPaymentCategoryWithReceiptPaymentCategoryNotExist() throws Exception{
        receiptPaymentCategory = new ReceiptPaymentCategory();
        receiptPaymentCategory.setId("rpc_001");
        receiptPaymentCategory.setName("receipt_pr_001");
        receiptPaymentCategory.setType(true);
        given(receiptPaymentCategoryRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/receipt-payment-categories/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Danh mục không tồn tại"));
    }

    @Test
    public void testGetLisReceiptPaymentCategorySuccess() throws Exception {

        List<ReceiptPaymentCategory> list = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            ReceiptPaymentCategory receiptPaymentCategory = new ReceiptPaymentCategory().setId(id +i).setName("receipt").setType(true);
            list.add(receiptPaymentCategory);
        }
        Pageable pageable = PageRequest.of(0, 20);

        Page<ReceiptPaymentCategory> rs = new PageImpl<ReceiptPaymentCategory>(list,pageable, list.size());

        given(receiptPaymentCategoryRepository.getAllByCondition(null, null, null, null, pageable)).willReturn(rs);

        mockMvc.perform(get("/v1/receipt-payment-categories"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").value("rpc_0010"));

    }

}
