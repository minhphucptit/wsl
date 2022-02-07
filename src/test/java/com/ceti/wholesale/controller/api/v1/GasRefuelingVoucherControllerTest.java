package com.ceti.wholesale.controller.api.v1;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.repository.FactoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.DeliveryVoucherMapper;
import com.ceti.wholesale.mapper.GoodInOutMapper;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.model.GasRefuelingVoucher;
import com.ceti.wholesale.repository.GasRefuelingVoucherRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.service.GasRefuelingVoucherService;
import com.ceti.wholesale.service.impl.GasRefuelingVoucherServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(GasRefuelingVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class GasRefuelingVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        GasRefuelingVoucherService gasRefuelingVoucherService(){return new GasRefuelingVoucherServiceImpl();
        }
    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoucherMapper voucherMapper;

    @MockBean
    private GasRefuelingVoucherRepository gasRefuelingVoucherRepository;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;

    @MockBean
    private DeliveryVoucherMapper deliveryVoucherMapper;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;
    
    @MockBean
    private GoodInOutMapper goodsInOutMapper;
    @MockBean
    private VoucherUtils voucherUtils;


    private String createRequest = "{\n" +
            "    \"update_by_full_name\": \"ADMIN\",\n" +
            "    \"created_by_full_name\": \"ADMIN\",\n" +
            "    \"total_gas\": \"22000.00\",\n" +
            "    \"voucher_code\":\"CN\",\n" +
            "    \"voucher_at\":1635069600,\n" +
            "    \"create_goods_in_out_requests\":\n" +
            "    [\n" +
            "        {\"in_quantity\":100,\"product_id\":\"PTEMGIADINH\",\"product_name\":\"Tem gia dinh\",\"type\":\"NKSX\"},\n" +
            "        {\"out_quantity\":200,\"product_id\":\"PMANGCOGIADINHLON\",\"product_name\":\"P mang co gia dinh lon\",\"type\":\"XKSX\"}\n" +
            "    ]\n" +
            "    \n" +
            "}\n";
    private String updateRequest = "{\n" +
            "    \"update_by_full_name\": \"ADMIN\",\n" +
            "    \"created_by_full_name\": \"ADMIN\",\n" +
            "    \"total_gas\": \"22000.00\",\n" +
            "    \"voucher_code\":\"CN\",\n" +
            "    \"voucher_at\":1635069600,\n" +
            "    \"create_goods_in_out_requests\":\n" +
            "    [\n" +
            "        {\"in_quantity\":100,\"product_id\":\"PTEMGIADINH\",\"product_name\":\"Tem gia dinh\",\"type\":\"NKSX\"},\n" +
            "        {\"out_quantity\":200,\"product_id\":\"PMANGCOGIADINHLON\",\"product_name\":\"P mang co gia dinh lon\",\"type\":\"XKSX\"}\n" +
            "    ]\n" +
            "    \n" +
            "}\n";

    @Test
    public void testGetAllSuccess() throws Exception {
        List<VoucherDto> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            VoucherDto item = new VoucherDto().setId("A" + i)
                    .setTotalGoods(new BigDecimal(i + 1)).setCompanyId("CompanyId" + i).setFactoryId("FactoryId" + i).setNo("No" + i)
                    .setNote("Note" + i).setTruckDriverFullName("TruckDriverFullName" + i);
            list.add(item);
        }
        Pageable pageable = PageRequest.of(0, 20);
        String page = PageableProcess.PageToSqlQuery(pageable, "gas_refueling_voucher");
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "gas_refueling_voucher");
        HashMap<String, String> where1 = new HashMap<>();
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("voucher_at_from","1633053853");
        where.add("voucher_at_to","1634273946");
        where.add("factory_id","FAC1");
        given(voucherMapper.getList(where1,pagingStr, page, Instant.ofEpochSecond(1633053853), Instant.ofEpochSecond(1634273946))).willReturn(list);
        given(voucherMapper.countList(where1, page, Instant.ofEpochSecond(1633053853), Instant.ofEpochSecond(1634273946))).willReturn(5l);

        mockMvc.perform(get("/v1/gas-refueling-vouchers").header("department_id","FAC1")
                        .params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }

    @Test
    public void testDeleteVoucher() throws Exception {
        GasRefuelingVoucher gasRefuelingVoucher = new GasRefuelingVoucher();
        gasRefuelingVoucher.setFactoryId("FAC1");
        gasRefuelingVoucher.setId("1");
        gasRefuelingVoucher.setUpdateByFullName("hungss");
        gasRefuelingVoucher.setVoucherAt(Instant.now());
        given(gasRefuelingVoucherRepository.existsById("1")).willReturn(true);

        mockMvc.perform(delete("/v1/gas-refueling-vouchers/{id}" , 1).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }
    @Test
    public void testCreateGasRefuelingVoucherSuccess() throws Exception {
        GasRefuelingVoucher gasRefuelingVoucher = new GasRefuelingVoucher();
        gasRefuelingVoucher.setFactoryId("1");
        gasRefuelingVoucher.setTotalGas(BigDecimal.valueOf(22000.00));
        gasRefuelingVoucher.setId("1");
        gasRefuelingVoucher.setVoucherAt(Instant.ofEpochSecond(1635069600));

        given(gasRefuelingVoucherRepository.save(isA(GasRefuelingVoucher.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(post("/v1/gas-refueling-vouchers").header("department_id", "1").contentType(APPLICATION_JSON_UTF8)
                .content(createRequest)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"));
    }

    @Test
    public void testUpdateVoucherSuccess() throws Exception {
        Instant nowTime = Instant.now();
        GasRefuelingVoucher gasRefuelingVoucher = new GasRefuelingVoucher();
        gasRefuelingVoucher.setFactoryId("FAC1");
        gasRefuelingVoucher.setVoucherAt(Instant.ofEpochSecond(1635069600));
        gasRefuelingVoucher.setId("1");
        gasRefuelingVoucher.setUpdateByFullName("ADMIN");
        given(gasRefuelingVoucherRepository.findById("1")).willReturn(Optional.of(gasRefuelingVoucher));
        mockMvc.perform(patch("/v1/gas-refueling-vouchers/{id}" , "1").header("department_id", "1").contentType(APPLICATION_JSON_UTF8)
                .content(updateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("ADMIN"));
    }
}
