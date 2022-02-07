package com.ceti.wholesale.controller.api.v1;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.TruckDriver;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.SalesmanDetailRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

import com.ceti.wholesale.model.Salesman;
import com.ceti.wholesale.repository.SalesmanRepository;
import com.ceti.wholesale.service.SalesmanService;
import com.ceti.wholesale.service.impl.SalesmanServiceImpl;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@WebMvcTest(SalesmanController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class SalesmanControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        SalesmanService salesmanService(){return new SalesmanServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SalesmanRepository salesmanRepository;
    @MockBean
    private FactoryRepository factoryRepository;
    @MockBean
    private SalesmanDetailRepository salesmanDetailRepository;
    private String jsonCreateRequest = "{\n" +
            "            \"id\": \"1\",\n" +
            "            \"abbreviated_name\": \"Chec32\",\n" +
            "            \"full_name\": \"Phạm Thị Hiên\",\n" +
            "            \"address\": \"Hải Phòng\",\n" +
            "            \"phone_number\": \"025684521\"\n" +
            "        }";
    private String jsonUpdateRequest= "{\n" +
            "            \"abbreviated_name\": \"Chec32\",\n" +
            "            \"full_name\": \"Phạm Thị Hiên\",\n" +
            "            \"address\": \"Hải Phòng\",\n" +
            "            \"phone_number\": \"025684521\"\n" +
            "        }";
//    @Test
//    public void testGetSalesmanSuccess()throws Exception{
//        List<Salesman> list= new ArrayList<>();
//        for (int i=0;i<5;i++){
//            Salesman salesman= new Salesman().setId("A"+i).setFullName("fullname"+i).setAbbreviatedName("abbname"+i)
//                    .setAddress("address"+i).setPhoneNumber("092819"+i);
//            list.add(salesman);
//        }
//        Pageable pageable= PageRequest.of(0,20);
//        Page<Salesman> salesmen = new PageImpl<Salesman>(list,pageable, list.size());
//        given(salesmanRepository.getAllByCondition(null,null, null,null,null,null,"FAC1",null, pageable)).willReturn(salesmen);
//        mockMvc.perform(get("/v1/salesmans").header("department_id","FAC1"))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").value("A0"));
//    }

    @Test
    public void testGetListSalesmanWithEmbed() throws Exception {

        //  Get list Salesman success with embed
        List<Object[]> objects = new ArrayList<>();
        List<Salesman> listSalesman= new ArrayList<>();
        for(int i=0;i<5;i++){
            Salesman salesman= new Salesman().setId("A"+i).setFullName("fullname"+i).setAbbreviatedName("abbname"+i)
                    .setAddress("address"+i).setPhoneNumber("092819"+i);
            listSalesman.add(salesman);
            Factory factory = new Factory().setId("Id" + i).setNameOnVoucher("NameOnVoucher" + i).setName("Name" + i)
                    .setAddressOnVoucher("AddressOnVoucher" + i).setDirectorFullName("DirectorFullName" + i);
            Object[] object = new Object[2];
            object[0] = salesman;
            object[1] = factory;
            objects.add(object);
        }
        Pageable pageable = PageRequest.of(0,20);
        ResultPage<Object[]> result = new ResultPage<>();
        result.setPageList(objects);
        result.setTotalItems(5);

        MultiValueMap<String, String> whereSalesman = new LinkedMultiValueMap<>();
        whereSalesman.add("embed", "factory");

        String[] embed = new String[1];
        given(salesmanDetailRepository.findAllWithEmbed(pageable, whereSalesman)).willReturn(result);
        mockMvc.perform(get("/v1/salesmans").params(whereSalesman).header("department_id","FAC1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));

    }

    @Test
    public void testGetListAllSalesman() throws Exception {

        //  Get list all Salesman success
        List<Salesman> listSalesman= new ArrayList<>();
        for(int i=0;i<10;i++){
            Salesman salesman= new Salesman().setId("A"+i).setFullName("fullname"+i).setAbbreviatedName("abbname"+i)
                    .setAddress("address"+i).setPhoneNumber("092819"+i).setFactoryId("nhat" + i).setIsActive(true);
            listSalesman.add(salesman);
        }

        ResultPage<Salesman> result = new ResultPage<>();
        result.setTotalItems(10);
        result.setPageList(listSalesman);

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id", "nhat0");

        given(salesmanDetailRepository.findAll(isA(Pageable.class),Mockito.any())).willReturn(result);

        mockMvc.perform(get("/v1/salesmans").params(where).header("department_id","nhat0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(10))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }


    @Test
    public void testCreateSalesmanSuccess()throws Exception{
        given(salesmanRepository.save(isA(Salesman.class))).willAnswer(i->i.getArgument(0));
        given(salesmanRepository.existsById("1")).willReturn(false);
        mockMvc.perform(post("/v1/salesmans")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));

    }
    @Test
    public void testCreateSalesmanFalseWithIdExists()throws Exception{
        given(salesmanRepository.existsById("1")).willReturn(true);
        mockMvc.perform(post("/v1/salesmans")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã nhân viên đã tồn tại"));

    }
    @Test
    public void testUpdateSalesmanSuccess()throws Exception{
        Salesman salesman= new Salesman().setId("1").setFullName("bien").setAbbreviatedName("bienhd");
        given(salesmanRepository.findById("1")).willReturn(Optional.of(salesman));
        given(salesmanRepository.save(isA(Salesman.class))).willAnswer(i->i.getArgument(0));
        mockMvc.perform(patch("/v1/salesmans/{id}", "1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }
    @Test
    public void testUpdateSalesmanFalseWithIdNotExists()throws Exception{
        given(salesmanRepository.findById("d13")).willReturn(Optional.empty());
        mockMvc.perform(patch("/v1/salesmans/{id}", "d13").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã nhân viên không tồn tại"));

    }

}
