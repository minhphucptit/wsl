package com.ceti.wholesale.controller.api.v1;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import com.ceti.wholesale.dto.CostTrackBoardDto;
import com.ceti.wholesale.dto.CostTrackBoardTotalQuantityAndTotalItemsDto;
import com.ceti.wholesale.mapper.CostTrackBoardMapper;
import com.ceti.wholesale.model.CostTrackBoard;
import com.ceti.wholesale.repository.CostTrackBoardRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.service.CostTrackBoardService;
import com.ceti.wholesale.service.impl.CostTrackBoardServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(CostTrackBoarsController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CostTrackBoarsControllerTest {
	
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        CostTrackBoardService costTrackBoardService(){return new CostTrackBoardServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CostTrackBoardRepository costTrackBoardRepository;
    @MockBean
    private FactoryRepository factoryRepository;

    
    @MockBean
    private CostTrackBoardMapper costTrackBoardMapper;


    private String jsonCreate = "{\r\n"
            + "    \"km\": 3.8,\r\n"
            + "    \"truck_license_plate_number\": \"27M3-3333\",\r\n"
            + "    \"truck_cost_type_id\": \"truckCostTypeId\",\r\n"
            + "    \"brand\": \"brand\",\r\n"
            + "    \"made_in\": \"madeIn\",\r\n"
            + "    \"specifications\": \"specifications\",\r\n"
            + "    \"type\": \"type\",\r\n"
            + "    \"unit\": \"unit\",\r\n"
            + "    \"truck_driver_id\": \"truckDriverId\",\r\n"
            + "    \"gara\": \"gara\",\r\n"
            + "    \"note\": \"note\",\r\n"
            + "    \"unit_price\": 32.8,\r\n"
            + "    \"quantity\": 4.8,\r\n"
            + "    \"total\": 3.8,\r\n"
            + "    \"factory_id\": \"factoryId\",\r\n"
            + "    \"action_date\": 1630425621,\r\n"
            + "    \"warranty_date_to\": 1630879526,\r\n"
            + "    \"day\": 1630454400\r\n"
            + "}";
    
    private String jsonUpdate = "{\r\n"
    		+ "    \"km\": 3.8,\r\n"
    		+ "    \"truck_license_plate_number\": \"27M3-3333\",\r\n"
            + "    \"truck_cost_type_id\": \"truckCostTypeId\",\r\n"
            + "    \"brand\": \"brand\",\r\n"
            + "    \"made_in\": \"madeIn\",\r\n"
            + "    \"specifications\": \"specifications\",\r\n"
            + "    \"type\": \"type\",\r\n"
            + "    \"unit\": \"unit\",\r\n"
            + "    \"truck_driver_id\": \"truckDriverId\",\r\n"
            + "    \"gara\": \"gara\",\r\n"
            + "    \"note\": \"note\",\r\n"
            + "    \"unit_price\": 32.8,\r\n"
            + "    \"quantity\": 4.8,\r\n"
            + "    \"total\": 3.8,\r\n"
            + "    \"factory_id\": \"factoryId\",\r\n"
            + "    \"action_date\": 1630425621,\r\n"
            + "    \"warranty_date_to\": 1630879526,\r\n"
    		+ "    \"day\": 1630454400\r\n"
    		+ "}";
    
    private String licenseNumber = "27M3-3333";
    private Instant day = Instant.ofEpochSecond(1630454400l);
    private String id = "2c9c80887c30b498017c349b2d500001";

    @Test
    public void testGetAllSuccess() throws Exception {
        List<CostTrackBoardDto> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CostTrackBoardDto item = new CostTrackBoardDto().setTruckCostTypeId("TruckCostTypeId" + i).setTruckCostTypeName("TruckCostTypeName" + i).setId("Id" +i)
                    .setFactoryId("FactoryId" +i).setActionDate(Instant.ofEpochSecond(1633053853)).setBrand("Brand" + i).setFactoryName("FactoryName" + i).setGara("Gara" + i)
                    .setKm(123 + i).setMadeIn("MadeIn" + i).setNote("Note" + i).setQuantity(BigDecimal.valueOf(1 + i)).setSpecifications("Specifications" + i).setTotal(BigDecimal.valueOf(1 + i))
                    .setTruckLicensePlateNumber("TruckLicensePlateNumber" + i).setType("Type" + i).setUnit("Unit" +i).setUnitPrice(BigDecimal.valueOf(1 + i));
            list.add(item);
        }
        Pageable pageable= PageRequest.of(0,20);
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("action_date_from","1633053853");
        where.add("action_date_to","1634273946");
        where.add("truck_cost_type_id","truckCostTypeId");
        where.add("factory_id","factoryId");
        where.add("truck_license_plate_number","truckLicensePlateNumber");
        where.add("type","type");
        where.add("cost_track_board_type","TRUCK");

        given(costTrackBoardMapper.getList(Instant.ofEpochSecond(1633053853), Instant.ofEpochSecond(1634273946),"truckCostTypeId", "factoryId",
                "truckLicensePlateNumber", "type","TRUCK",null,null, pageable.getOffset(), pageable.getPageSize())).willReturn(list);
        given(costTrackBoardMapper.countList(Instant.ofEpochSecond(1633053853), Instant.ofEpochSecond(1634273946),"truckCostTypeId", "factoryId",
                "truckLicensePlateNumber", "type","TRUCK",null,null)).willReturn(5l);
        mockMvc.perform(get("/v1/cost_track_board").params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(jsonPath("$.items[0].id").value("Id0"));
    }

    @Test
    public void testGetTotalSuccess() throws Exception {
        CostTrackBoardTotalQuantityAndTotalItemsDto truckCostTrackBoardTotalQuantityAndTotalItems = new CostTrackBoardTotalQuantityAndTotalItemsDto()
                .setTotalItems(Long.valueOf(123)).setTotalQuantity(BigDecimal.valueOf(123));
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("action_date_from","1633053853");
        where.add("action_date_to","1634273946");
        where.add("truck_cost_type_id","truckCostTypeId");
        where.add("factory_id","factoryId");
        where.add("truck_license_plate_number","truckLicensePlateNumber");
        where.add("type","type");
        where.add("cost_track_board_type","TRUCK");

        given(costTrackBoardMapper.getTotal(Instant.ofEpochSecond(1633053853), Instant.ofEpochSecond(1634273946),"truckCostTypeId", "factoryId",
                "truckLicensePlateNumber", "type","TRUCK",null,null)).willReturn(truckCostTrackBoardTotalQuantityAndTotalItems);

        mockMvc.perform(get("/v1/cost_track_board").params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }


    @Test
    public void testCreateSuccess() throws Exception {
        given(costTrackBoardRepository.save(isA(CostTrackBoard.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(post("/v1/cost_track_board")
                        .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreate))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));
    }

    @Test
    public void testUpdate() throws Exception {
        //khi chi phí xe tải không tồn tại
        given(costTrackBoardRepository.findById(id)).willReturn(Optional.empty());
        mockMvc.perform(put("/v1/cost_track_board/{id}",id)
                .contentType(APPLICATION_JSON_UTF8).content(jsonUpdate))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Chi phí xe tải/thiết bị này không tồn tại"));


        //khi chi phí xe tải tồn tại và thực hiện update
        CostTrackBoard costTrackBoard = new CostTrackBoard().setTruckCostTypeId("TruckCostTypeId").setId("Id")
                .setFactoryId("FactoryId").setActionDate(Instant.ofEpochSecond(1633053853)).setBrand("Brand").setGara("Gara")
                .setKm(123 ).setMadeIn("MadeIn").setNote("Note").setQuantity(BigDecimal.valueOf(1)).setSpecifications("Specifications").setTotal(BigDecimal.valueOf(1))
                .setTruckLicensePlateNumber("TruckLicensePlateNumber").setType("Type").setUnit("Unit").setUnitPrice(BigDecimal.valueOf(1));

        given(costTrackBoardRepository.findById(id)).willReturn(Optional.ofNullable(costTrackBoard));
        mockMvc.perform(put("/v1/cost_track_board/{id}",id)
                        .contentType(APPLICATION_JSON_UTF8).content(jsonUpdate))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("note"));
    }


    @Test
    public void testDelete() throws Exception {
        //khi tồn tại chi phí xe tải
        given(costTrackBoardRepository.existsById(id)).willReturn(true);
        mockMvc.perform(delete("/v1/cost_track_board/{id}",id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));

        //khi không tồn tại chi phí xe tải
        given(costTrackBoardRepository.existsById(id)).willReturn(false);
        mockMvc.perform(delete("/v1/cost_track_board/{id}",id))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Chi phí xe tải/thiết bị này không tồn tại"));
    }

    @Test
    public void testGetTotal() throws Exception {
        CostTrackBoardTotalQuantityAndTotalItemsDto costTrackBoardTotalQuantityAndTotalItems = new CostTrackBoardTotalQuantityAndTotalItemsDto()
                .setTotalItems(Long.valueOf(123)).setTotalQuantity(BigDecimal.valueOf(123));
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("action_date_from","1633053853");
        where.add("action_date_to","1634273946");
        where.add("truck_cost_type_id","truckCostTypeId");
        where.add("factory_id","factoryId");
        where.add("truck_license_plate_number","truckLicensePlateNumber");
        where.add("type","type");
        where.add("cost_track_board_type","TRUCK");

        given(costTrackBoardMapper.getTotal(Instant.ofEpochSecond(1633053853), Instant.ofEpochSecond(1634273946),"truckCostTypeId", "factoryId",
                "truckLicensePlateNumber", "type","TRUCK",null,null)).willReturn(costTrackBoardTotalQuantityAndTotalItems);

        mockMvc.perform(get("/v1/cost_track_board/get-total").params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

}
