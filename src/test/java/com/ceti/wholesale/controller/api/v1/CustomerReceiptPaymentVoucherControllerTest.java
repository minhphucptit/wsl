package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.model.CustomerReceiptPaymentVoucher;
import com.ceti.wholesale.repository.CustomerReceiptPaymentVoucherRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.service.CustomerReceiptPaymentVoucherService;
import com.ceti.wholesale.service.impl.CustomerReceiptPaymentVoucherServiceImpl;
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

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerReceiptPaymentVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerReceiptPaymentVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        CustomerReceiptPaymentVoucherService customerReceiptPaymentVoucherService() {
            return new CustomerReceiptPaymentVoucherServiceImpl();
        }

    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VoucherUtils voucherUtils;

    @MockBean
    private CustomerReceiptPaymentVoucherRepository customerReceiptPaymentVoucherRepository;

    private CustomerReceiptPaymentVoucher customerReceiptPaymentVoucher;

    String jsonCreateRequest = "{\n" +
            "            \"customer_id\": \"ct_001\",\n" +
            "            \"customer_full_name\": \"do van hung\",\n" +
            "            \"payer\": \"payer01\", \n" +
            "            \"payer_method\": \"false\", \n" +
            "            \"address\": \"ha noi\", \n" +
            "            \"reason\": \"vay cong lai\", \n" +
            "            \"amount_of_money\": \"12\", \n" +
            "            \"note\": \"tien vay cong lai\", \n"+
            "            \"category\": \"rpc_001\", \n"+
            "            \"create_by\": \"hung\", \n"+
            "            \"update_by\": \"hung\", \n"+
            "            \"voucher_at\": \"123\", \n"+
            "            \"type\": \"true\" \n"+
            "}";

    String jsonCreateRequestWithTypeFalse = "{\n" +
            "            \"customer_id\": \"ct_001\",\n" +
            "            \"customer_full_name\": \"do van hung\",\n" +
            "            \"payer\": \"payer01\", \n" +
            "            \"payer_method\": \"false\", \n" +
            "            \"address\": \"ha noi\", \n" +
            "            \"reason\": \"vay cong lai\", \n" +
            "            \"amount_of_money\": \"12\", \n" +
            "            \"note\": \"tien vay cong lai\", \n"+
            "            \"category\": \"rpc_001\", \n"+
            "            \"create_by\": \"hung\", \n"+
            "            \"update_by\": \"hung\", \n"+
            "            \"voucher_at\": \"123\", \n"+
            "            \"type\": \"false\" \n"+
            "}";

    String jsonUpdateRequest = "{\n" +
            "            \"customer_id\": \"ct_001\",\n" +
            "            \"customer_full_name\": \"do van hung\",\n" +
            "            \"payer\": \"payer01\", \n" +
            "            \"payer_method\": \"false\", \n" +
            "            \"address\": \"ha noi\", \n" +
            "            \"reason\": \"vay cong lai\", \n" +
            "            \"amount_of_money\": \"12\", \n" +
            "            \"note\": \"tien vay cong lai\", \n"+
            "            \"category\": \"rpc_001\", \n"+
            "            \"update_by\": \"ha\", \n"+
            "            \"type\": \"true\" \n"+
            "}";
    String no = "rpc_001";

    @Test
    public void testCreateCustomerReceiptPaymentSuccess() throws Exception{
        BigDecimal ss= BigDecimal.valueOf(12);
        customerReceiptPaymentVoucher = new CustomerReceiptPaymentVoucher().setCustomerId("ct_001").setCustomerFullName("do van hung").setPayer("payer01")
        .setPayerMethod(false).setAddress("ha noi").setReason("vay cong lai").setCollectMoney(ss).setNote("tien vay cong lai")
        .setCategory("rpc_001").setCreateBy("hung").setUpdateBy("hung").setVoucherCode("TH");
        given(customerReceiptPaymentVoucherRepository.save(customerReceiptPaymentVoucher)).willReturn(customerReceiptPaymentVoucher);

        mockMvc.perform(post("/v1/customer-receipt-payment-vouchers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by").value("hung"));
    }

    @Test
    public void testCreateCustomerReceiptPaymentSuccessWithTypeFalse() throws Exception{
        BigDecimal ss= BigDecimal.valueOf(12);
        customerReceiptPaymentVoucher = new CustomerReceiptPaymentVoucher().setCustomerId("ct_001").setCustomerFullName("do van hung").setPayer("payer01")
                .setPayerMethod(false).setAddress("ha noi").setReason("vay cong lai").setCollectMoney(ss).setNote("tien vay cong lai")
                .setCategory("rpc_001").setCreateBy("hung").setUpdateBy("hung").setVoucherCode("TH");
        given(customerReceiptPaymentVoucherRepository.save(customerReceiptPaymentVoucher)).willReturn(customerReceiptPaymentVoucher);

        mockMvc.perform(post("/v1/customer-receipt-payment-vouchers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequestWithTypeFalse)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by").value("hung"));
    }

    @Test
    public void testUpdateCustomerReceiptPaymentSuccess() throws Exception{
        BigDecimal ss= BigDecimal.valueOf(12);
        customerReceiptPaymentVoucher = new CustomerReceiptPaymentVoucher().setCustomerId("ct_001").setCustomerFullName("do van hung").setPayer("payer01")
                .setPayerMethod(false).setAddress("ha noi").setReason("vay cong lai").setCollectMoney(ss).setNote("tien vay cong lai")
                .setCategory("rpc_001").setUpdateBy("ha").setVoucherCode("TH");
        given(customerReceiptPaymentVoucherRepository.findById(no)).willReturn(Optional.ofNullable(customerReceiptPaymentVoucher));
        given(customerReceiptPaymentVoucherRepository.save(customerReceiptPaymentVoucher)).willReturn(customerReceiptPaymentVoucher);

        mockMvc.perform(patch("/v1/customer-receipt-payment-vouchers/{no}", no)
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.address").value("ha noi"));
    }

    @Test
    public void testUpdateCustomerReceiptPaymentWithReceiptPaymentNotExist() throws Exception{
        BigDecimal ss= BigDecimal.valueOf(12);
        customerReceiptPaymentVoucher = new CustomerReceiptPaymentVoucher().setCustomerId("ct_001").setCustomerFullName("do van hung").setPayer("payer01")
                .setPayerMethod(false).setAddress("ha noi").setReason("vay cong lai").setCollectMoney(ss).setNote("tien vay cong lai")
                .setCategory("rpc_001").setUpdateBy("ha").setVoucherCode("TH");
        given(customerReceiptPaymentVoucherRepository.findById(no)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/customer-receipt-payment-vouchers/{no}", no)
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu thu chi không tồn tại"));
    }

    @Test
    public void testGetListCustomerReceiptPaymentSuccess() throws Exception {
        BigDecimal ss= BigDecimal.valueOf(12);
        List<CustomerReceiptPaymentVoucher> list = new ArrayList<>();
        Instant instant = Instant.now();
        for(int i = 0; i < 5; i++){
            CustomerReceiptPaymentVoucher receiptPayment = new CustomerReceiptPaymentVoucher().setNo("CH210308-011"+i).setCustomerId("ct_001").setCustomerFullName("do van hung").setPayer("payer01")
                    .setPayerMethod(false).setAddress("ha noi").setReason("vay cong lai").setCollectMoney(ss).setNote("tien vay cong lai")
                    .setCategory("rpc_001").setUpdateBy("ha").setVoucherCode("TH").setVoucherAt(instant).setCreateAt(instant).setUpdateAt(instant)
                    .setUpdateBy("hung").setCreateBy("hung");
            list.add(receiptPayment);
        }
        Pageable pageable = PageRequest.of(0, 20);

        Page<CustomerReceiptPaymentVoucher> rs = new PageImpl<CustomerReceiptPaymentVoucher>(list,pageable, list.size());

        given(customerReceiptPaymentVoucherRepository.getAllByCondition(null, null,null,null,null,null,"FAC1", pageable)).willReturn(rs);

        mockMvc.perform(get("/v1/customer-receipt-payment-vouchers")     .header("department_id","FAC1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].reason").value("vay cong lai"));

    }

    @Test
    public void testDeleteCustomerReceiptPaymentWithReceiptPaymentNotExist() throws Exception{
        BigDecimal ss= BigDecimal.valueOf(12);
        customerReceiptPaymentVoucher = new CustomerReceiptPaymentVoucher().setCustomerId("ct_001").setCustomerFullName("do van hung").setPayer("payer01")
                .setPayerMethod(false).setAddress("ha noi").setReason("vay cong lai").setCollectMoney(ss).setNote("tien vay cong lai")
                .setCategory("rpc_001").setUpdateBy("ha").setVoucherCode("TH");
        given(customerReceiptPaymentVoucherRepository.findById(no)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/customer-receipt-payment-vouchers/{no}", no)
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu thu chi khách hàng không tồn tại"));
    }

    @Test
    public void testDeleteCustomerReceiptPaymentSuccess() throws Exception{
        BigDecimal ss= BigDecimal.valueOf(12);
        customerReceiptPaymentVoucher = new CustomerReceiptPaymentVoucher().setCustomerId("ct_001").setCustomerFullName("do van hung").setPayer("payer01")
                .setPayerMethod(false).setAddress("ha noi").setReason("vay cong lai").setCollectMoney(ss).setNote("tien vay cong lai")
                .setCategory("rpc_001").setUpdateBy("ha").setVoucherCode("TH");
        given(customerReceiptPaymentVoucherRepository.existsById(no)).willReturn(true);

        mockMvc.perform(delete("/v1/customer-receipt-payment-vouchers/{no}", no)
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }
}
