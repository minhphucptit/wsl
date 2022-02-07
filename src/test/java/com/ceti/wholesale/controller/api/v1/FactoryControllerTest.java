package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.CustomerCategory;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.Region;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.service.FactoryService;
import com.ceti.wholesale.service.impl.FactoryServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FactoryController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class FactoryControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        FactoryService factoryService(){return new FactoryServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;

    @Test
    public void testGetFactoryById()throws Exception{
        Factory factory = new Factory().setId("Id").setName("Name").setNameOnVoucher("NameOnVoucher")
                    .setAddressOnVoucher("AddressOnVoucher").setDirectorFullName("DirectorFullName");
        String id = "Id";
        given(factoryRepository.getById(id)).willReturn(factory);
        mockMvc.perform(get("/v1/factories/{id}","Id"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }

    @Test
    public void testGetListSuccess()throws Exception{
        Factory factory = new Factory();
        List<Factory> list = new ArrayList<>();
        for(int i=0;i<5;i++){
            factory.setId("Id").setName("Name").setNameOnVoucher("NameOnVoucher")
                    .setAddressOnVoucher("AddressOnVoucher").setDirectorFullName("DirectorFullName");
            list.add(factory);
        }
        Pageable pageable = PageRequest.of(0,20);
        Page<Factory> resultPage = new Page<>() {
            @Override
            public Iterator<Factory> iterator() {
                return null;
            }

            @Override
            public int getTotalPages() {
                return 0;
            }

            @Override
            public long getTotalElements() {
                return 0;
            }

            @Override
            public int getNumber() {
                return 0;
            }

            @Override
            public int getSize() {
                return 0;
            }

            @Override
            public int getNumberOfElements() {
                return 0;
            }

            @Override
            public List<Factory> getContent() {
                return null;
            }

            @Override
            public boolean hasContent() {
                return false;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public <U> Page<U> map(Function<? super Factory, ? extends U> converter) {
                return null;
            }
        };
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();

        ExampleMatcher matcher = ExampleMatcher.matching().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreNullValues();
        Example<Factory> filter = Example.of(factory, matcher);
        given(factoryRepository.findAll(filter, pageable)).willReturn( resultPage);
        mockMvc.perform(get("/v1/factories")).andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code").value("E_0001"))
                .andExpect(jsonPath("$.total_items").value(0));
    }

}
