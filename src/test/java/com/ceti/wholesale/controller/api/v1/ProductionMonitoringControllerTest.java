package com.ceti.wholesale.controller.api.v1;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.ceti.wholesale.dto.plan.ProductionMonitoringStatisticDataDto;
import com.ceti.wholesale.dto.plan.ProductionMonitoringStatisticDto;
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

import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.v2.UpdateProductionMonitoringRequest;
import com.ceti.wholesale.dto.ProductionMonitoringDto;
import com.ceti.wholesale.mapper.ProductionMonitoringMapper;
import com.ceti.wholesale.model.ProductionMonitoring;
import com.ceti.wholesale.repository.CustomerRepository;
import com.ceti.wholesale.repository.ProductionMonitoringRepository;
import com.ceti.wholesale.service.ProductionMonitoringService;
import com.ceti.wholesale.service.impl.ProductionMonitoringServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductionMonitoringController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ProductionMonitoringControllerTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@TestConfiguration
	public static class ServiceTestConfiguration {
		@Bean
		ProductionMonitoringService beginningCylinderDebtService() {
			return new ProductionMonitoringServiceImpl();
		}

	}

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private FactoryRepository factoryRepository;
	@MockBean
	private ProductionMonitoringRepository productionMonitoringRepository;

	@MockBean
	private CustomerRepository customerRepository;

	@MockBean
	private ProductionMonitoringMapper productionMonitoringMapper;

	String jsonCreateRequestFalse1 = "{\r\n" + "    \"production_monitorings\": [\r\n" + "        {\r\n"
			+ "            \"customer_code\": \"1\",\r\n" + "            \"voucher_at\": 1630342800,\r\n"
			+ "            \"row_location\": 1\r\n" + "        }\r\n" + "    ]\r\n" + "}";

	String jsonCreateRequestFalse2 = "{\r\n" + "    \"production_monitorings\": [\r\n" + "        {\r\n"
			+ "            \"customer_code\": \"1\",\r\n" + "            \"voucher_at\": 1630342800,\r\n"
			+ "            \"quantity\": 15000,\r\n" + "            \"row_location\": 1\r\n" + "        }\r\n"
			+ "    ]\r\n, \"check_exists\": true "  + "}";
	String jsonCreateRequestSuccess = "{\r\n" + "    \"production_monitorings\": [\r\n" + "        {\r\n"
			+ "            \"customer_code\": \"1\",\r\n" + "            \"voucher_at\": 1630342800,\r\n"
			+ "            \"quantity\": 15000,\r\n" + "            \"row_location\": 1\r\n" + "        }\r\n"
			+ "    ]\r\n, \"check_exists\": true "  + "}";

	private String customerCode = "nhat123";
	@Test
	public void testCreateRequestFalse1() throws Exception {

		mockMvc.perform(post("/v1/production-monitorings").header("user_id", "ketoan")
				.contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequestFalse1)).andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Lỗi ở dòng 1"));
	}

	@Test
	public void testCreateFalse2() throws Exception {

		given(customerRepository.getFirstCustomerIdByCustomerCode("1")).willReturn(null);

		mockMvc.perform(post("/v1/production-monitorings").header("user_id", "ketoan")
				.contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequestFalse2)).andExpect(status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Lỗi ở dòng 1"));
	}

	@Test
	public void testCreateSuccess() throws Exception {

		given(customerRepository.getFirstCustomerIdByCustomerCode("1")).willReturn("1");

		mockMvc.perform(post("/v1/production-monitorings").header("user_id", "ketoan")
				.contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequestSuccess)).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.items[0].quantity").value(15000));
	}

	@Test
	public void testUpdatetFalse1() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		JavaTimeModule module = new JavaTimeModule();
		mapper.registerModule(module);

		UpdateProductionMonitoringRequest updateRequest = new UpdateProductionMonitoringRequest().setCustomerId("A1")
				.setVoucherAt(Instant.ofEpochSecond(1111)).setQuantity(new BigDecimal(100));
		String request = mapper.writeValueAsString(updateRequest);

		given(productionMonitoringRepository.getByCustomerIdAndVoucherAt(updateRequest.getCustomerId(),
				updateRequest.getVoucherAt())).willReturn(null);

		mockMvc.perform(patch("/v1/production-monitorings").header("user_id", "ketoan")
				.contentType(APPLICATION_JSON_UTF8).content(request)).andExpect(status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Kết quả sản lượng không tồn tại"));
	}

	@Test
	public void testUpdatetSuccess() throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		JavaTimeModule module = new JavaTimeModule();
		mapper.registerModule(module);

		UpdateProductionMonitoringRequest updateRequest = new UpdateProductionMonitoringRequest().setCustomerId("A1")
				.setVoucherAt(Instant.ofEpochSecond(1111)).setQuantity(new BigDecimal(100));
		String request = mapper.writeValueAsString(updateRequest);

		ProductionMonitoring productionMonitoring = new ProductionMonitoring()
				.setVoucherAt(updateRequest.getVoucherAt()).setCustomerId(updateRequest.getCustomerId())
				.setQuantity(new BigDecimal(1500));

		given(productionMonitoringRepository.getByCustomerIdAndVoucherAt(updateRequest.getCustomerId(),
				updateRequest.getVoucherAt())).willReturn(productionMonitoring);

		mockMvc.perform(patch("/v1/production-monitorings").header("user_id", "ketoan")
				.contentType(APPLICATION_JSON_UTF8).content(request)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.item.quantity").value(100));
	}

	@Test
	public void testGetAllSuccess() throws Exception {
		List<ProductionMonitoringDto> list = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			ProductionMonitoringDto item = new ProductionMonitoringDto().setCustomerId("A12")
					.setQuantity(new BigDecimal(i + 1));
			list.add(item);
		}
		Pageable pageable = PageRequest.of(0, 20);

		String page = PageableProcess.PageToSqlQuery(pageable, "production_monitoring");

		given(productionMonitoringMapper.getList("A12", null, null, page)).willReturn(list);
		given(productionMonitoringMapper.countList("A12", null, null)).willReturn(5l);

		mockMvc.perform(get("/v1/production-monitorings").param("customer_id", "A12")).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.items[0].quantity").value(1));
	}

//	@Test
//	public void testGetAllFromWholesaleData() throws Exception {
//		List<ProductionMonitoringDto> list = new ArrayList<>();
//		for (int i = 0; i < 5; i++) {
//			ProductionMonitoringDto item = new ProductionMonitoringDto().setCustomerCode("A12")
//					.setQuantity(new BigDecimal(i + 1));
//			list.add(item);
//		}
//
//		given(productionMonitoringMapper.getListFromWholesaleData("A12", null, null)).willReturn(list);
//
//		mockMvc.perform(get("/v1/production-monitoring-from-wholesale-data").param("customer_code", "A12"))
//				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.items[0].quantity").value(1));
//	}

//	@Test
//	public void testDelete() throws Exception {
//		MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
//		where.add("customer_code","nhat123");
//		where.add("voucher_at","1634273946");
//
//		//khi sản lượng tồn tại
//		given(productionMonitoringRepository.existsByCustomerCode(customerCode)).willReturn(true);
//		given(productionMonitoringRepository.existsByVoucherAt(Instant.ofEpochSecond(1634273946))).willReturn(true);
//		mockMvc.perform(delete("/v1/production-monitorings").params(where))
//				.andExpect(status().isOk())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
//
//		//khi sản lượng không tồn tại
//		given(productionMonitoringRepository.existsByCustomerCode(customerCode)).willReturn(false);
//		given(productionMonitoringRepository.existsByVoucherAt(Instant.ofEpochSecond(1634273946))).willReturn(false);
//		mockMvc.perform(delete("/v1/production-monitorings").params(where))
//				.andExpect(status().isNotFound())
//				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
//				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Sản lượng không tồn tại"));
//	}

	@Test
	public void getStatistic() throws Exception {
		List<List<ProductionMonitoringStatisticDataDto>> lists = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			List<ProductionMonitoringStatisticDataDto> productionMonitoringStatisticDatas = new ArrayList<>();
			for (int j = 0; j < 5; j++) {
				ProductionMonitoringStatisticDataDto  productionMonitoringStatisticData = new ProductionMonitoringStatisticDataDto();
				productionMonitoringStatisticData.setCustomerCode("CustomerCode" + i);
				productionMonitoringStatisticData.setCompanyId("CompanyId" + i);
				productionMonitoringStatisticData.setQuantity(BigDecimal.valueOf(1 + i));
				productionMonitoringStatisticData.setCustomerCategory("CustomerCategory"+ i);
				productionMonitoringStatisticData.setVoucherAt(Instant.ofEpochSecond(1633053853 + i));
				productionMonitoringStatisticData.setIsTotalCompany(true);
				productionMonitoringStatisticData.setIsTotalRegion(true);
				productionMonitoringStatisticData.setTargetQuantity(BigDecimal.valueOf(1 +  i));
				productionMonitoringStatisticData.setRegion("Region" + i);
				productionMonitoringStatisticDatas.add(productionMonitoringStatisticData);
			}

			ProductionMonitoringStatisticDto item = new ProductionMonitoringStatisticDto().setProductionMonitoringStatisticDetail(productionMonitoringStatisticDatas)
					.setProductionMonitoringStatisticGroup(productionMonitoringStatisticDatas);
			lists.add(productionMonitoringStatisticDatas);
		}
		Pageable pageable= PageRequest.of(0,20);
		MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
		where.add("voucher_at_from","1633053853");
		where.add("voucher_at_to","1634273946");
		where.add("customer_code","customerCode");
		where.add("region_id","regionId");
		where.add("company_id","companyId");
        where.add("owner_company","ownerCompany");
        where.add("is_wholesale_customer","false");
		where.add("customer_category","customerCategory");
		where.add("is_target_production","false");
		given(productionMonitoringMapper.getStatistic("customerCode", Instant.ofEpochSecond(1633053853)
				, Instant.ofEpochSecond(1634273946),"regionId","companyId","ownerCompany",false,"customerCategory",false)).willReturn(lists);

		mockMvc.perform(get("/v1/production-monitoring-statistic").params(where)).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
	}

}
