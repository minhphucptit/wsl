package com.ceti.wholesale.controller.api.v1;

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
import java.util.Collections;
import java.util.List;

import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.TruckRepository;
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

import com.ceti.wholesale.dto.GasSettlementCustomDto;
import com.ceti.wholesale.dto.GasSettlementDto;
import com.ceti.wholesale.mapper.TruckDistanceMapper;
import com.ceti.wholesale.model.TruckDistance;
import com.ceti.wholesale.repository.TruckDistanceRepository;
import com.ceti.wholesale.service.TruckDistanceService;
import com.ceti.wholesale.service.impl.TruckDistanceServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(TruckDistanceController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TruckDistanceControllerTest {
	
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        TruckDistanceService truckDistanceService(){return new TruckDistanceServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;
    @MockBean
    private TruckDistanceRepository truckDistanceRepository;

    @MockBean
    private TruckRepository truckRepository;
    
    @MockBean
    private TruckDistanceMapper truckDistanceMapper;
    
    private String jsonCreate = "{\r\n"
    		+ "    \"distance\": 2.8,\r\n"
    		+ "    \"truck_license_plate_number\": \"27M3-3333\",\r\n"
    		+ "    \"day\": 1630454400\r\n"
    		+ "}";
    
    private String jsonUpdate = "{\r\n"
    		+ "    \"distance\": 3.8,\r\n"
    		+ "    \"truck_license_plate_number\": \"27M3-3333\",\r\n"
    		+ "    \"day\": 1630454400\r\n"
    		+ "}";
    
    private String licenseNumber = "27M3-3333";
    private Instant day = Instant.ofEpochSecond(1630454400l);
    private String id = "2c9c80887c30b498017c349b2d500001";
    
    @Test
    public void testCreateFalse() throws Exception {
        given(truckDistanceRepository.existsByTruckLicensePlateNumberAndDay(licenseNumber, day)).willReturn(true);

        mockMvc.perform(post("/v1/truck-distances")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreate))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Quyết toán dầu đã tồn tại"));
    }
    
    @Test
    public void testCreateSuccess() throws Exception {
        given(truckDistanceRepository.existsByTruckLicensePlateNumberAndDay(licenseNumber, day)).willReturn(false);

        mockMvc.perform(post("/v1/truck-distances")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.distance").value(2.8));
    }
    
    @Test
    public void testUpdateFalse() throws Exception {
        given(truckDistanceRepository.getByTruckLicensePlateNumberAndDay(licenseNumber, day)).willReturn(null);
        
        mockMvc.perform(patch("/v1/truck-distances").param("day", String.valueOf(day.getEpochSecond())).param("truck_license_plate_number", licenseNumber)
                .contentType(APPLICATION_JSON_UTF8).content(jsonUpdate))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Quyết toán dầu không tồn tại"));
    }
    
    @Test
    public void testUpdateSuccess() throws Exception {
    	TruckDistance truckDistance = new TruckDistance().setDay(day).setTruckLicensePlateNumber(licenseNumber)
    			.setDistance(new BigDecimal(2.8));
    	
        given(truckDistanceRepository.getByTruckLicensePlateNumberAndDay(licenseNumber, day)).willReturn(truckDistance);
        
        mockMvc.perform(patch("/v1/truck-distances").param("day", String.valueOf(day.getEpochSecond())).param("truck_license_plate_number", licenseNumber)
                .contentType(APPLICATION_JSON_UTF8).content(jsonUpdate))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.distance").value(3.8));
    }
    
    @Test
    public void testDeleteFalse() throws Exception {
        given(truckDistanceRepository.existsByTruckLicensePlateNumberAndDay(licenseNumber, day)).willReturn(false);
        
        mockMvc.perform(delete("/v1/truck-distances").param("day", String.valueOf(day.getEpochSecond())).param("truck_license_plate_number", licenseNumber))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Quyết toán dầu không tồn tại"));
    }
    
    @Test
    public void testDeleteSuccess() throws Exception {
        given(truckDistanceRepository.existsByTruckLicensePlateNumberAndDay(licenseNumber, day)).willReturn(true);
        
        mockMvc.perform(delete("/v1/truck-distances").param("day", String.valueOf(day.getEpochSecond())).param("truck_license_plate_number", licenseNumber))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @Test
    public void testGetGasSettlementSuccess() throws Exception {

        List<List<Object>> lists = new ArrayList<>();
        List<GasSettlementDto> gasSettlementDtos = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            GasSettlementDto gasSettlementDto = new GasSettlementDto();
            gasSettlementDto.setDistance(BigDecimal.valueOf(1 + i));
            gasSettlementDto.setDay(Instant.ofEpochSecond(11+ i));
            gasSettlementDto.setTruckLicensePlateNumber("TruckDistanceId" + i);
            gasSettlementDto.setQuantity(BigDecimal.valueOf(1 + i));
            gasSettlementDtos.add(gasSettlementDto);
        }
        GasSettlementCustomDto gasSettlementCustomDto = new GasSettlementCustomDto();

        gasSettlementCustomDto.setGasSettlements(gasSettlementDtos);
        lists.add(Collections.singletonList(gasSettlementDtos));
        lists.add(Collections.singletonList(gasSettlementCustomDto));
        Pageable pageable= PageRequest.of(0,20);
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("month","11");
        where.add("year","2021");
        where.add("truck_license_plate_number","truckLicensePlateNumber");

        given(truckDistanceMapper.getList(11,2021,"truckLicensePlateNumber")).willReturn(lists);
        mockMvc.perform(get("/v1/truck-distances/gas-settlements").params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }

}
