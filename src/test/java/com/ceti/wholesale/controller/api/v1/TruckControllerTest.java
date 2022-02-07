package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.dto.TruckDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.TruckMapper;
import com.ceti.wholesale.model.Truck;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.TruckDetailRepository;
import com.ceti.wholesale.repository.TruckRepository;
import com.ceti.wholesale.service.TruckService;
import com.ceti.wholesale.service.impl.TruckServiceImpl;
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

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TruckController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TruckControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        TruckService truckService(){return new TruckServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;
    @MockBean
    private TruckRepository truckRepository;
    @MockBean
    private TruckMapper truckMapper;
    @MockBean
    private TruckDetailRepository truckDetailRepository;
    private String jsonCreateRequest = "{\n" +
            "            \"license_plate_number\": \"1234\",\n" +
            "            \"truck_weight\": 3.0,\n" +
            "            \"made_in\": \"VietNam\",\n" +
            "            \"truck_version\": \"2019\",\n" +
            "            \"manufacturers\": \"Kia ThaCo\",\n" +
            "            \"trunk_size\": \"2.5x8x2\",\n" +
            "            \"trunk_type\": \"Mui tran\",\n" +
            "            \"number_kM\": 1111,\n" +
            "            \"explosives_transport_paper_present\": 1630491063,\n" +
            "            \"explosives_transport_paper_next\": 1630577463,\n" +
            "            \"car_inspection_time_present\": 1630663863,\n" +
            "            \"car_inspection_time_next\": 1630750263,\n" +
            "            \"car_insurance_present\": 1630836663,\n" +
            "            \"car_insurance_next\": 1630923063\n" +
            "        }";
    private String jsonUpdateRequest= "{\n" +
            "            \"truck_weight\": 3.0,\n" +
            "            \"made_in\": \"VietNam\",\n" +
            "            \"truck_version\": \"2020\",\n" +
            "            \"manufacturers\": \"Kia ThaCo\",\n" +
            "            \"trunk_size\": \"2.5x8x2\",\n" +
            "            \"trunk_type\": \"Mui tran\",\n" +
            "            \"number_kM\": 1111,\n" +
            "            \"explosives_transport_paper_present\": 1630491063,\n" +
            "            \"explosives_transport_paper_next\": 1630577463,\n" +
            "            \"car_inspection_time_present\": 1630663863,\n" +
            "            \"car_inspection_time_next\": 1630750263,\n" +
            "            \"car_insurance_present\": 1630836663,\n" +
            "            \"car_insurance_next\": 1630923063\n" +
            "        }";
//    @Test
//    public void testGetTruckSuccess()throws Exception{
//        List<Object[]> list = new ArrayList<>();
//        for(int i=0;i<5;i++){
//            Truck truck= new Truck().setLicensePlateNumber("number"+i).setTruckWeight(BigDecimal.valueOf(12)).setMadeIn("madeIn" + i).setTruckVersion("TruckVersion" + i).
//                    setManufacturers("Manufacturers" + i).setTrunkSize("TrunkSize" + i).setTrunkType("TrunkType"+ i).setNumberKM(BigDecimal.valueOf(12));
//            Factory factory = new Factory().setId("id" + i).setName("name" + i).setAddressOnVoucher("AddressOnVoucher" + i).
//                    setNameOnVoucher("NameOnVoucher" + i).setDirectorFullName("DirectorFullName" + i);
//            Object[] object = new Object[10];
//            object[0] = truck;
//            object[1] = factory;
//            list.add(object);
//        }
//        Pageable pageable = PageRequest.of(0, 20);
//
//        ResultPage<Object[]> result = new ResultPage<Object[]>();
//        result.setPageList(list);
//        result.setTotalItems(5);
//
//        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
//        //where.add("factory_id","FAC1");
//        given(truckDetailRepository.findAllWithEmbed(pageable,where)).willReturn(result);
//        mockMvc.perform(get("/v1/trucks")
//                .header("department_id","FAC1"))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
//    }
    @Test
    public void testCreateTruckSuccess() throws Exception {
        given(truckRepository.save(isA(Truck.class))).willAnswer(i -> i.getArgument(0));
        given(truckRepository.existsById("1234")).willReturn(false);
        mockMvc.perform(post("/v1/trucks")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));
    }
    @Test
    public void testCreateTruckFalseWithLicensePlateNumberExists()throws Exception{
        given(truckRepository.existsById("1234")).willReturn(true);
        mockMvc.perform(post("/v1/trucks")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Biển số xe đã tồn tại"));
    }
    @Test
    public void testUpdateTruckSuccess()throws Exception{
        Truck truck = new Truck().setLicensePlateNumber("1234").setTruckWeight(BigDecimal.valueOf(13)).setMadeIn("madeIn").setTruckVersion("TruckVersion" ).
                setManufacturers("Manufacturers").setTrunkSize("TrunkSize").setTrunkType("TrunkType").setNumberKM(BigDecimal.valueOf(13));
    given(truckRepository.existsById("1234")).willReturn(true);
    given(truckRepository.getTruckByLicensePlateNumber("1234")).willReturn(truck);
//    given(truckRepository.save(isA(Truck.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(put("/v1/trucks/{license_place_number}","1234").contentType(APPLICATION_JSON_UTF8).content(jsonUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));

    }
    @Test
    public void testUpdateTruckFalseWithLicensePlateNumberNotExists()throws Exception{
        given(truckRepository.existsById("12346")).willReturn(false);
        mockMvc.perform(put("/v1/trucks/{license_place_number}","12346").contentType(APPLICATION_JSON_UTF8).content(jsonUpdateRequest))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Biển số xe không tồn tại"));

    }

    @Test
    public void testGetAllSuccess() throws Exception {
        List<TruckDto> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TruckDto item = new TruckDto()
                    .setTruckVersion("TruckVersion" + i).setTruckWeight(BigDecimal.valueOf(1+i)).setFactoryId("FactoryId" +i)
                    .setTrunkType("TrunkType" +i).setDORatio(BigDecimal.valueOf(1 + i)).setIsActive(true)
                    .setLicensePlateNumber("LicensePlateNumber" + i).setMadeIn("MadeIn" + i).setManufacturers("Manufacturers" + i);
            list.add(item);
        }
        Pageable pageable = PageRequest.of(0, 20);
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "truck");
        HashMap<String, String> where = new HashMap<>();
        given(truckMapper.getList(where,pagingStr)).willReturn(list);
        given(truckMapper.countList(where)).willReturn(5l);

        mockMvc.perform(get("/v1/trucks")).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(jsonPath("$.items[0].made_in").value("MadeIn0"));
    }
}
