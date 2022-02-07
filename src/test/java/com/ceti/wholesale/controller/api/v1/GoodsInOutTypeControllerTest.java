package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.dto.GoodsInOutTypeDto;
import com.ceti.wholesale.mapper.GoodsInOutTypeMapper;
import com.ceti.wholesale.model.GoodsInOutType;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.GoodsInOutTypeRepository;
import com.ceti.wholesale.service.GoodsInOutTypeService;
import com.ceti.wholesale.service.impl.GoodsInOutTypeServiceImpl;
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GoodsInOutTypeController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class GoodsInOutTypeControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        GoodsInOutTypeService goodsInOutTypeService() {
            return new GoodsInOutTypeServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;

    @MockBean
    private GoodsInOutTypeMapper goodsInOutTypeMapper;

    @MockBean
    GoodsInOutTypeRepository goodsInOutTypeRepository;

    @Test
    public void testGetAllSuccess() throws Exception {
        List<GoodsInOutTypeDto> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GoodsInOutTypeDto item = new GoodsInOutTypeDto().setId("Id" + i).setName("Name" + i).setCode("Code" + i);
            list.add(item);
        }
        Pageable pageable = PageRequest.of(0, 20);
        String pageableString = PageableProcess.PageToSqlQuery(pageable, "giot");
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("voucher_code" , "voucherCode");
        where.add("code_not_equal" , "codeNotEqual");
        given(goodsInOutTypeMapper.getList("voucherCode", "codeNotEqual",pageable.getOffset(), pageable.getPageSize())).willReturn(list);
        given(goodsInOutTypeMapper.countList("voucherCode", "codeNotEqual")).willReturn(5l);

        mockMvc.perform(get("/v1/voucher-codes/{voucher_code}/goods-in-out-types", "voucherCode").params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(jsonPath("$.items[0].id").value("Id0"));
    }

    @Test
    public void testGetGoodsInOutTypeByCode() throws Exception {
        List<GoodsInOutType> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GoodsInOutType item = new GoodsInOutType();
            item.setId("Id" + i);
            item.setName("Name" + i);
            item.setCode("Code"+i);
            list.add(item);
        }
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("code" , "code");
        given(goodsInOutTypeRepository.findAllByCode("code")).willReturn(list);

        mockMvc.perform(get("/v1/goods-in-out-types").params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value("5"))
                .andExpect(jsonPath("$.items[0].id").value("Id0"));
    }
}
