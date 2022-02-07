package com.ceti.wholesale.controller.api.v1;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.repository.FactoryRepository;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.model.InventoryVoucher;
import com.ceti.wholesale.repository.FactoryImportVoucherRepository;
import com.ceti.wholesale.repository.GasRefuelingVoucherRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.GoodsInventoryRepository;
import com.ceti.wholesale.repository.InventoryVoucherRepository;
import com.ceti.wholesale.service.InventoryVoucherService;
import com.ceti.wholesale.service.impl.InventoryVoucherServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(InventoryVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class InventoryVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType() ,
            MediaType.APPLICATION_JSON.getSubtype() , Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        InventoryVoucherService inventoryVoucherService() {
            return new InventoryVoucherServiceImpl();
        }

    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    InventoryVoucherRepository inventoryVoucherRepository;
    @MockBean
    private VoucherUtils voucherUtils;

    @MockBean
    private GoodsInventoryRepository goodsInventoryRepository;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;
    @MockBean
    private FactoryRepository factoryRepository;
    @MockBean
    private FactoryImportVoucherRepository FactoryImportVoucherRepository;
    
    @MockBean
    private GasRefuelingVoucherRepository gasRefuelingVoucherRepository;

    private String jsonCreateRequest = "{\n" +
            "    \"note\": \"nothing\",\n" +
            "    \"voucher_at\": 1622214020,\n" +
            "    \"create_by_full_name\": \"Do van Hung\",\n" +
            "    \"counter\": \"hung\",\n" +
            "    \"goods_inventories\": [\n" +
            "        {\n" +
            "            \"product_id\": \"1\",\n" +
            "            \"product_name\": \"aaa\",\n" +
            "            \"company_id\": \"ANPHA\",\n" +
            "            \"weight\": 50,\n" +
            "            \"product_type\": \"GAS\",\n" +
            "            \"inventory\": 6.23,\n" +
            "            \"unit\": \"don vi\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"product_id\": \"2\",\n" +
            "            \"product_name\": \"aaa\",\n" +
            "            \"company_id\": \"ANPHA\",\n" +
            "            \"weight\": 50,\n" +
            "            \"product_type\": \"GAS\",\n" +
            "            \"inventory\": 6.23,\n" +
            "            \"unit\": \"don vi\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    private String jsonUpdateRequest = "{\n" +
            "    \"note\": \"nothing\",\n" +
            "    \"voucher_at\": 1622214020,\n" +
            "    \"create_by_full_name\": \"Do van Hung\",\n" +
            "    \"update_by_full_name\": \"hungss\",\n" +
            "    \"counter\": \"hung\",\n" +
            "    \"goods_inventories\": [\n" +
            "        {\n" +
            "            \"product_id\": \"1\",\n" +
            "            \"product_name\": \"aaa\",\n" +
            "            \"weight\": 50,\n" +
            "            \"product_type\": \"GAS\",\n" +
            "            \"company_id\": \"ANPHA\",\n" +
            "            \"inventory\": 6.23,\n" +
            "            \"unit\": \"don vi\"\n" +
            "        }" +
            "    ]\n" +
            "}";

    @Test
    public void testGetListInventoryVoucherSuccess() throws Exception {

        List<InventoryVoucher> objects = new ArrayList<>();
        Instant nowTime = Instant.now();
        for (int i = 0; i < 5; i++) {

            InventoryVoucher inventoryVoucher = new InventoryVoucher();
            inventoryVoucher.setId("id" + i);

            inventoryVoucher.setVoucherAt(nowTime);
            inventoryVoucher.setCreateAt(nowTime);
            inventoryVoucher.setUpdateAt(nowTime);
            objects.add(inventoryVoucher);
        }

        Pageable pageable = PageRequest.of(0 , 20);

        Page<InventoryVoucher> rs = new PageImpl<InventoryVoucher>(objects , pageable , objects.size());

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("voucher_at_from" , "1619827200");
        where.add("voucher_at_to" , "1622581128");


        given(inventoryVoucherRepository.getAllByConditions(Instant.ofEpochSecond(1619827200) , Instant.ofEpochSecond(1622581128) , null,"FAC1" , pageable)).willReturn(rs);

        mockMvc.perform(get("/v1/inventory-vouchers")
                .header("department_id" , "FAC1")
                .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("id0"));
    }


    @Test
    public void testCreateInventoryVoucherSuccess() throws Exception {
        Instant nowTime = Instant.now();
        InventoryVoucher inventoryVoucher = new InventoryVoucher();
        inventoryVoucher.setFactoryId("FAC1");
        inventoryVoucher.setId("1");
        inventoryVoucher.setNote("nothing");
//        inventoryVoucher.setVoucherAt(Instant.ofEpochSecond(1622214020));
//        inventoryVoucher.setCreateAt(Instant.now());

        inventoryVoucher.setVoucherAt(nowTime);
        inventoryVoucher.setCreateAt(nowTime);
        inventoryVoucher.setUpdateAt(nowTime);
        given(inventoryVoucherRepository.existsById("1")).willReturn(false);
        given(inventoryVoucherRepository.save(inventoryVoucher)).willReturn(inventoryVoucher);

        mockMvc.perform(post("/v1/inventory-vouchers").header("department_id" , "FAC1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("nothing"));
    }

    @Test
    public void testUpdateInventoryVoucherSuccess() throws Exception {
        Instant nowTime = Instant.now();
        InventoryVoucher inventoryVoucher = new InventoryVoucher();
        inventoryVoucher.setFactoryId("FAC1");
        inventoryVoucher.setId("1");
        inventoryVoucher.setUpdateByFullName("hungss");
        inventoryVoucher.setVoucherAt(Instant.ofEpochSecond(1622214020));
        inventoryVoucher.setCreateAt(Instant.ofEpochSecond(1622214020));
        inventoryVoucher.setUpdateAt(nowTime);
        given(inventoryVoucherRepository.findById("1")).willReturn(Optional.of(inventoryVoucher));
        mockMvc.perform(patch("/v1/inventory-vouchers/{id}" , "1").header("department_id" , "FAC1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("hungss"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_at").value(nowTime.getEpochSecond()));
    }

    @Test
    public void testUpdateInventoryVoucherNotExist() throws Exception {
        given(inventoryVoucherRepository.findById("1")).willReturn(Optional.empty());
        mockMvc.perform(patch("/v1/inventory-vouchers/{id}" , 1).header("department_id" , "FAC1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("không tồn tại phiếu kiểm kê này"));
    }


    @Test
    public void testDeleteInventoryVoucher() throws Exception {
        Instant nowTime = Instant.now();
        InventoryVoucher inventoryVoucher = new InventoryVoucher();
        inventoryVoucher.setFactoryId("FAC1");
        inventoryVoucher.setId("1");
        inventoryVoucher.setUpdateByFullName("hungss");
        inventoryVoucher.setVoucherAt(nowTime);
        inventoryVoucher.setCreateAt(nowTime);
        inventoryVoucher.setUpdateAt(nowTime);
        given(inventoryVoucherRepository.findById("1")).willReturn(Optional.of(inventoryVoucher));

        mockMvc.perform(delete("/v1/inventory-vouchers/{id}" , 1).header("department_id" , "FAC1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }

    @Test
    public void testDeleteDeliveryVoucherWithDeliverVoucherNotExist() throws Exception {

        given(inventoryVoucherRepository.findById("1")).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/inventory-vouchers/{id}" , 1).header("department_id" , "FAC1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("không tồn tại phiếu kiểm kê này"));
    }


}
