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

import com.ceti.wholesale.mapper.GoodsInStockMapper;
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
import com.ceti.wholesale.model.GoodsInStock;
import com.ceti.wholesale.repository.GoodsInStockDetailRepository;
import com.ceti.wholesale.repository.GoodsInStockRepository;
import com.ceti.wholesale.service.GoodsInStockService;
import com.ceti.wholesale.service.impl.GoodsInStockServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(GoodsInStockController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class GoodsInStockControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        GoodsInStockService GoodsInStockService() {
            return new GoodsInStockServiceImpl();
        }

    }

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;

    @MockBean
    GoodsInStockRepository goodsInStockRepository;

    @MockBean
    GoodsInStockMapper goodsInStockMapper;

    @MockBean
    GoodsInStockDetailRepository goodsInStockDetailRepository;

    private GoodsInStock goodsInStock = new GoodsInStock();

    String jsonCreateRequest = "{\n" +
            "            \"customer_id\": \"KH01\",\n" +
            "            \"inventory\": \"122\",\n" +
            "            \"product_id\": \"romano\" , \n" +
            "            \"create_by\": \"Do Van Hung\" , \n" +
            "            \"company_id\": \"CT01\" , \n" +
            "            \"update_by\": \"Do Van Hung\" , \n" +
            "            \"note\": \"money debt\" , \n" +
            "            \"year\": \"2021\"\n" +
            "}";

    String jsonUpdateRequest = "{\n" +
            "            \"customer_id\": \"KH01\",\n" +
            "            \"inventory\": \"122\",\n" +
            "            \"product_id\": \"romano\" , \n" +
            "            \"company_id\": \"CT01\" , \n" +
            "            \"update_by\": \"Do Van Hung\" , \n" +
            "            \"note\": \"money debt\" , \n" +
            "            \"factory_id\": \"121\" , \n" +
            "            \"id\": \"22\" , \n" +
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
    public void testCreateGoodsInStockSuccess() throws Exception {
        given(goodsInStockRepository.existsById(id)).willReturn(false);
        given(goodsInStockRepository.save(isA(GoodsInStock.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(post("/v1/goods-in-stocks")
                .header("department_id","FAC1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.year").value("2021"));
    }

    @Test
    public void testCreateGoodsInStockFalseWithGoodsInStockNotExists() throws Exception {
        given(goodsInStockRepository.existsById("2021CT01romanoFAC1")).willReturn(true);

        mockMvc.perform(post("/v1/goods-in-stocks")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hàng hóa romano đã có bản ghi đầu kỳ lượng hàng trong năm 2021"));
    }

    @Test
    public void testUpdateGoodsInStockSuccess() throws Exception {
        given(goodsInStockRepository.findById(id)).willReturn(Optional.ofNullable(goodsInStock));
        given(goodsInStockRepository.save(isA(GoodsInStock.class))).willAnswer(i-> i.getArgument(0));

        mockMvc.perform(patch("/v1/goods-in-stocks/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.inventory").value("122"));
    }

    @Test
    public void testUpdateGoodsInStockWithGoodsInStockNotExist() throws Exception {
        given(goodsInStockRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/goods-in-stocks/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã không tồn tại"));
    }

    @Test
    public void testGetListGoodsInStockSuccess() throws Exception {

        List<GoodsInStock> objects = new ArrayList<>();

        for(int i = 0; i < 5; i++){

            GoodsInStock goodsInStock = new GoodsInStock();
            goodsInStock.setCompanyId("voucher"+i);
            goodsInStock.setCreateBy("voucher_no"+i);
            goodsInStock.setNote("voucher_code"+i);
            goodsInStock.setProductId("type"+i);
            goodsInStock.setId("id"+i);
            goodsInStock.setInventory(BigDecimal.valueOf(100));
            goodsInStock.setYear(2021);
            goodsInStock.setProductId("product"+i);

            objects.add(goodsInStock);
        }

        Pageable pageable = PageRequest.of(0, 20);

        ResultPage<GoodsInStock> result = new ResultPage<GoodsInStock>();
        result.setPageList(objects);
        result.setTotalItems(5);

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();

        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
        where1.add("factory_id", "FAC1");



        given(goodsInStockDetailRepository.findAllWithFilter(where1, pageable)).willReturn(result);

        mockMvc.perform(get("/v1/goods-in-stocks")
                .header("department_id","FAC1")
                .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("id0"));
    }

    @Test
    public void testDeleteGoodsInStockWithGoodsInStockNotExist() throws Exception {
        given(goodsInStockRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/goods-in-stocks/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("đầu kì hàng hóa không tồn tại"));
    }

    @Test
    public void testForwardGoodsInStockSuccess() throws Exception {
        given(goodsInStockMapper.setForwardToNextYear(2021,2022,"abc","Phuc")).willReturn(1);

        mockMvc.perform(post("/v1/goods-in-stocks/forward").contentType(APPLICATION_JSON_UTF8)
                .header("department_id","abc")
                .content(jsonForwardCreateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Kết chuyển thành công"));
    }

//    @Test
//    public void testForwardGoodsInStockFalse() throws Exception {
//        given(goodsInStockMapper.setForwardToNextYear(2021,2022,"abc","Phuc")).willReturn(null);
//
//        mockMvc.perform(post("/v1/goods-in-stocks/forward").contentType(APPLICATION_JSON_UTF8)
//                .header("department_id","abc")
//                .content(jsonForwardCreateRequest))
//                .andExpect(status().is5xxServerError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_500"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Lỗi dữ liệu"));
//    }

    @Test
    public void deleteGoodsInStock() throws Exception {
        given(goodsInStockRepository.existsById("1")).willReturn(true);

        mockMvc.perform(delete("/v1/goods-in-stocks/{id}", "1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonForwardCreateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }

    @Test
    public void testUpdateGoodsInStockFalse() throws Exception {
        GoodsInStock goodsInStock = new GoodsInStock();
        goodsInStock.setId("2021CT01romano1");
        goodsInStock.setFactoryId("12");
        given(goodsInStockRepository.findById("2021CT01romano1")).willReturn(Optional.of(goodsInStock));
        given(goodsInStockRepository.existsById("2021CT01romano12")).willReturn(true);
        mockMvc.perform(patch("/v1/goods-in-stocks/{id}", "2021CT01romano1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hàng hóa romano đã có bản ghi đầu kỳ lượng hàng trong năm 2021"));
    }

}
