package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.Factory;
import com.ceti.wholesale.model.TruckDriver;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.TruckDriverDetailRepository;
import com.ceti.wholesale.repository.TruckDriverRepository;
import com.ceti.wholesale.service.TruckDriverService;
import com.ceti.wholesale.service.impl.TruckDriverServiceImpl;
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TruckDriverController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TruckDriverControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        TruckDriverService truckDriverService(){return new TruckDriverServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TruckDriverRepository truckDriverRepository;
    @MockBean
    private FactoryRepository factoryRepository;

    @MockBean
    private TruckDriverDetailRepository truckDriverDetailRepository;
    private String jsonCreateRequest= "{\n" +
            "            \"id\": \"1\",\n" +
            "            \"abbreviated_name\": \"Check\",\n" +
            "            \"full_name\": \"Phạm Thị Hiên\",\n" +
            "            \"address\": \"Hà Nội\",\n" +
            "            \"phone_number\": \"02323232\",\n" +
            "            \"truck_license_plate_number\": \"1251\",\n" +
            "            \"driving_license_expire_at\": 1612890000,\n" +
            "            \"fire_protection_license_expire_at\": 1620579600,\n" +
            "            \"lpg_license_expire_at\": 1623258000\n" +
            "        }";
    private String jsonUpdateRequest="{\n" +
            "            \"abbreviated_name\": \"Check\",\n" +
            "            \"full_name\": \"Phạm Thị Hiên\",\n" +
            "            \"address\": \"Hà Nội\",\n" +
            "            \"phone_number\": \"02323232\",\n" +
            "            \"truck_license_plate_number\": \"1251\",\n" +
            "            \"driving_license_expire_at\": 1612890000,\n" +
            "            \"fire_protection_license_expire_at\": 1620579600,\n" +
            "            \"lpg_license_expire_at\": 1623258000\n" +
            "        }";

    @Test
    public void testGetListTruckDriver() throws Exception {

        // Get list TruckDriver success with null embed
        List<TruckDriver> list =new ArrayList<>();
        for(int i=0;i<5;i++){
            TruckDriver truckDriver = new TruckDriver().setId("A"+i).setAbbreviatedName("hdb"+i).setFullName("name"+i)
                    .setAddress("address"+i).setTruckLicensePlateNumber("123"+i).setPhoneNumber("0909"+i);
            list.add(truckDriver);
        }
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        ResultPage<TruckDriver> resultPage = new ResultPage<>();
        resultPage.setPageList(list);
        resultPage.setTotalItems(5);
        Pageable pageable = PageRequest.of(0,20);
        given(truckDriverDetailRepository.findAll(pageable, where)).willReturn(resultPage);

        mockMvc.perform(get("/v1/truck-drivers")).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));


        //  Get list TruckDriver success with embed
        List<Object[]> objects = new ArrayList<>();
        List<TruckDriver> listTruckDriver =new ArrayList<>();
        for(int i=0;i<5;i++){
            TruckDriver truckDriver = new TruckDriver().setId("A"+i).setAbbreviatedName("hdb"+i).setFullName("name"+i)
                    .setAddress("address"+i).setTruckLicensePlateNumber("123"+i).setPhoneNumber("0909"+i);
            listTruckDriver.add(truckDriver);
            Factory factory = new Factory().setId("Id" + i).setNameOnVoucher("NameOnVoucher" + i).setName("Name" + i)
                    .setAddressOnVoucher("AddressOnVoucher" + i).setDirectorFullName("DirectorFullName" + i);
            Object[] object = new Object[2];
            object[0] = truckDriver;
            object[1] = factory;
            objects.add(object);
        }

        ResultPage<Object[]> result = new ResultPage<>();
        result.setPageList(objects);
        result.setTotalItems(5);

        MultiValueMap<String, String> whereTruckDriver = new LinkedMultiValueMap<>();
        whereTruckDriver.add("embed", "factory");

        String[] embed = new String[1];

        given(truckDriverDetailRepository.findAllWithEmbed(pageable, whereTruckDriver)).willReturn(result);

        mockMvc.perform(get("/v1/truck-drivers").params(whereTruckDriver)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }


        @Test
        public void testCreateTruckDriverSuccess()throws Exception{
        given(truckDriverRepository.save(isA(TruckDriver.class))).willAnswer(i -> i.getArgument(0));
        given(truckDriverRepository.existsById("1")).willReturn(false);
            mockMvc.perform(post("/v1/truck-drivers").header("department_id","FAC1")
                    .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));

        }
        @Test
    public void testCreateTruckDriverFalseWithIdExists()throws Exception{
        given(truckDriverRepository.existsById("1")).willReturn(true);
            mockMvc.perform(post("/v1/truck-drivers")
                    .header("department_id","FAC1")
                    .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                    .andExpect(status().is4xxClientError())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã tài xế đã tồn tại"));
        }
        @Test
    public void testUpdateTruckDriverSuccess()throws Exception{
            TruckDriver truckDriver = new TruckDriver().setId("1").setPhoneNumber("0363023933").setAddress("Nam Tu Liem");
            given(truckDriverRepository.existsById("1")).willReturn(true);
            given(truckDriverRepository.getTruckDriverById("1")).willReturn(truckDriver);
//            given(truckDriverRepository.save(isA(TruckDriver.class))).willAnswer(i -> i.getArgument(0));
            mockMvc.perform(put("/v1/truck-drivers/{truck-driver-id}", "1").contentType(APPLICATION_JSON_UTF8)
                    .content(jsonUpdateRequest)).andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
        }
        @Test
    public void testUpdateTruckDriverFalseWithIdNotExists()throws Exception{
        given(truckDriverRepository.existsById("adqed")).willReturn(false);
            mockMvc.perform(put("/v1/truck-drivers/{truck-driver-id}", "adqed").contentType(APPLICATION_JSON_UTF8)
                    .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã tài xế không tồn tại"));
        }
        }

