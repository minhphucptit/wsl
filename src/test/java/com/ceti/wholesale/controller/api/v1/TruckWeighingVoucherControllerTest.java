package com.ceti.wholesale.controller.api.v1;


import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ceti.wholesale.common.enums.VoucherEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.Company;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.Product;
import com.ceti.wholesale.model.Truck;
import com.ceti.wholesale.model.TruckDriver;
import com.ceti.wholesale.model.TruckWeighingVoucher;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.FactoryExportVoucherRepository;
import com.ceti.wholesale.repository.FactoryImportVoucherRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.ProductRepository;
import com.ceti.wholesale.repository.TruckWeighingVoucherDetailRepository;
import com.ceti.wholesale.repository.TruckWeighingVoucherRepository;
import com.ceti.wholesale.service.TruckWeighingVoucherService;
import com.ceti.wholesale.service.impl.TruckWeighingVoucherServiceImpl;
import com.ceti.wholesale.service.impl.v2.WarehouseCommunicationServiceImpl;
import com.ceti.wholesale.service.v2.WarehouseCommunicationService;

@RunWith(SpringRunner.class)
@WebMvcTest(TruckWeighingVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class TruckWeighingVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        TruckWeighingVoucherService truckWeighingVoucherService() {
            return new TruckWeighingVoucherServiceImpl() {
            };
        }

        @Bean
        WarehouseCommunicationService WarehouseCommunicationService() {
            return new WarehouseCommunicationServiceImpl() {
            };
        }
    }

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;

    @MockBean
    private CylinderDebtRepository cylinderDebtRepository;

    @MockBean
    private TruckWeighingVoucherRepository truckWeighingVoucherRepository;

    @MockBean
    private TruckWeighingVoucherDetailRepository truckWeighingVoucherDetailRepository;

    @MockBean
    private FactoryImportVoucherRepository factoryImportVoucherRepository;

    @MockBean
    private FactoryExportVoucherRepository factoryExportVoucherRepository;
    @MockBean
    private VoucherUtils voucherUtils;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;

    @MockBean
    private ProductRepository productRepository;

    private TruckWeighingVoucher truckWeighingVoucher = new TruckWeighingVoucher();

    @MockBean
    private RestTemplate restTemplate;
    
    @MockBean
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    @Value(value = "${ceti.service.zoneId}")
    private String zoneId;

    String id = "truck";

    String jsonCreateRequest = "{\n" +
            "    \"voucher_code\":\"PCN\",\n" +
            "    \"company_id\":\"13\",\n" +
            "    \"product_id\":\"PO1\",\n" +
            "    \"customer_id\":\"1\",\n" +
            "    \"salesman_id\":\"PO1\",\n" +
            "    \"truck_driver_id\":\"1\",\n" +
            "    \"truck_license_plate_number\":\"54\",\n" +
            "    \"total_payment\":\"100\",\n" +
            "    \"total_goods\":\"159\",\n" +
            "    \"note\":\"note\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"created_at\":\"1615450688\",\n" +
            "    \"update_by_full_name\":\"kim van ha\",\n" +
            "    \"voucher_at\":\"1615452000\",\n" +
            "    \"weighing_result1\":\"100\",\n" +
            "    \"product_name\":\"romano\",\n" +
            " \"weighingResult2\":\"1\", " +
            "\"type\":\"100\", " +
            "    \"truck_weighing_voucher\":" +
            "{ \"weighing_result_2\":\"1\", " +
            "\"weighing_result_final\":\"111\"," +
            " \"payment\":\"100\", " +
            " \"productName\":\"romano\", " +
            "\"unit\":\"50\"," +
            "\"price\":\"100\"," +
            " \"type\":\"romano\" " +
            "}}"
            ;


    String jsonCreatePCXRequest = "{\n" +
            "    \"voucher_code\":\"PCX\",\n" +
            "    \"company_id\":\"13\",\n" +
            "    \"product_id\":\"PO1\",\n" +
            "    \"customer_id\":\"1\",\n" +
            "    \"salesman_id\":\"PO1\",\n" +
            "    \"truck_driver_id\":\"1\",\n" +
            "    \"truck_license_plate_number\":\"54\",\n" +
            "    \"total_payment\":\"100\",\n" +
            "    \"total_goods\":\"159\",\n" +
            "    \"note\":\"note\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"created_at\":\"1615450688\",\n" +
            "    \"update_by_full_name\":\"kim van ha\",\n" +
            "    \"voucher_at\":\"1615452000\",\n" +
            "    \"weighing_result1\":\"100\",\n" +
            "    \"product_name\":\"romano\",\n" +
            "   \"weighingResult2\":\"1\", " +
            "\"type\":\"100\", " +
            "    \"truck_weighing_voucher\":" +
            "{ \"weighing_result_2\":\"1\", " +
            "\"weighing_result_final\":\"111\"," +
            " \"payment\":\"100\", " +
            " \"productName\":\"romano\", " +
            "\"unit\":\"50\"," +
            "\"price\":\"100\"," +
            " \"type\":\"romano\" " +
            "}}"
            ;


    String jsonUpdateRequest = "{\n" +
            "    \"weighing_result2\":\"1000\",\n" +
            "    \"weighing_result_final\":\"150\",\n" +
            "    \"payment\":\"10000\",\n" +
            "    \"type\":\"aaa\",\n" +
            "    \"price\":\"12\",\n" +
            "    \"product_name\":\"clear\",\n" +
            "    \"voucher_at\":\"1254566255412\",\n" +
            "    \"unit\":\"don vi\"\n" +
            "}";

    @Test
    public void testUpdateTruckWeighingVoucherWithVoucherCodePCNSuccess() throws Exception{
        truckWeighingVoucher.setVoucherCode("PCN");
        truckWeighingVoucher.setCommandReferenceId("LX-001");
        given(truckWeighingVoucherRepository.findById(id)).willReturn(Optional.ofNullable(truckWeighingVoucher));
        given(truckWeighingVoucherRepository.save(isA(TruckWeighingVoucher.class))).willAnswer(i-> i.getArgument(0));
        Mockito.when(restTemplate.exchange(eq("http://warehouse-service/v2/truck-import-export-histories/LX-001"), eq(HttpMethod.PATCH), isA(HttpEntity.class), eq(String.class)))
        .thenReturn(null);
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(truckWeighingVoucher.getFactoryId(), VoucherEnum.getEnumFromCode(truckWeighingVoucher.getVoucherCode()),
                Instant.ofEpochSecond(10252));
        given(accountVoucherCodeRepository.findById("1")).willReturn(Optional.ofNullable(accountVoucherCode));
        mockMvc.perform(patch("/v1/truck-weighing-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.voucher_code").value("PCN"));
    }

    @Test
    public void testUpdateTruckWeighingVoucherWithVoucherCodePCXSuccess() throws Exception{
        truckWeighingVoucher.setVoucherCode("PCX");
        truckWeighingVoucher.setCommandReferenceId("LX-001");
        given(truckWeighingVoucherRepository.findById(id)).willReturn(Optional.ofNullable(truckWeighingVoucher));
        given(truckWeighingVoucherRepository.save(isA(TruckWeighingVoucher.class))).willAnswer(i-> i.getArgument(0));
        Mockito.when(restTemplate.exchange(eq("http://warehouse-service/v2/truck-import-export-histories/LX-001"), eq(HttpMethod.PATCH), isA(HttpEntity.class), eq(String.class)))
        .thenReturn(null);
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(truckWeighingVoucher.getFactoryId(), VoucherEnum.getEnumFromCode(truckWeighingVoucher.getVoucherCode()),
                Instant.ofEpochSecond(10252));
        given(accountVoucherCodeRepository.findById("1")).willReturn(Optional.ofNullable(accountVoucherCode));

        mockMvc.perform(patch("/v1/truck-weighing-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.voucher_code").value("PCX"));
    }


    @Test
    public void testUpdateTruckWeighingVoucherWithTruckWeighingVoucherNotExist() throws Exception{
        given(truckWeighingVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/truck-weighing-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu cân xe không tồn tại"));
    }

//    @Test
//    public void testCreateTruckWeighingVoucherSuccessWithVoucherCodePCN() throws Exception{
//        DateTimeFormatter formatterNo = DateTimeFormatter.ofPattern("yyMMdd");
//        LocalDateTime today = LocalDateTime.ofInstant(Instant.now(), ZoneId.of(zoneId));
//        String stringToday = today.format(formatterNo);
//
//        truckWeighingVoucher = new TruckWeighingVoucher();
//        truckWeighingVoucher.setVoucherCode("PCN");
//        truckWeighingVoucher.setId("CX"+stringToday+"-001-PCN-FAC1");
//        given(truckWeighingVoucherRepository.findById("CX"+stringToday+"-001-PCN-FAC1")).willReturn(Optional.ofNullable(truckWeighingVoucher));
//        given(truckWeighingVoucherRepository.save(isA(TruckWeighingVoucher.class))).willAnswer(i-> i.getArgument(0));
//        mockMvc.perform(post("/v1/truck-weighing-vouchers")
//                .header("department_id","FAC1").contentType(APPLICATION_JSON_UTF8)
//                .content(jsonCreateRequest)).andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("note"));
//    }
//
//    @Test
//    public void testCreateTruckWeighingVoucherSuccessWithVoucherCodePCNAndVoucherNoLargerthan1000() throws Exception{
//        DateTimeFormatter formatterNo = DateTimeFormatter.ofPattern("yyMMdd");
//        LocalDateTime today = LocalDateTime.ofInstant(Instant.now(), ZoneId.of(zoneId));
//        String stringToday = today.format(formatterNo);
//
//        truckWeighingVoucher = new TruckWeighingVoucher();
//        truckWeighingVoucher.setVoucherCode("PCN");
//        truckWeighingVoucher.setId("CX"+stringToday+"-2001-PCN");
//
//        given(truckWeighingVoucherRepository.countTruckWeighingVoucherInDay(stringToday,"FAC1")).willReturn(2000);
//        given(truckWeighingVoucherRepository.findById("CX"+stringToday+"-2001-PCN-FAC1")).willReturn(Optional.ofNullable(truckWeighingVoucher));
//        given(truckWeighingVoucherRepository.save(isA(TruckWeighingVoucher.class))).willAnswer(i-> i.getArgument(0));
//        mockMvc.perform(post("/v1/truck-weighing-vouchers")
//                .header("department_id","FAC1").contentType(APPLICATION_JSON_UTF8)
//                .content(jsonCreateRequest)).andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("note"));
//    }
//
//    @Test
//    public void testCreateTruckWeighingVoucherSuccessWithVoucherCodePCX() throws Exception{
//        DateTimeFormatter formatterNo = DateTimeFormatter.ofPattern("yyMMdd");
//        LocalDateTime today = LocalDateTime.ofInstant(Instant.now(), ZoneId.of(zoneId));
//        String stringToday = today.format(formatterNo);
//
//        truckWeighingVoucher = new TruckWeighingVoucher();
//        truckWeighingVoucher.setVoucherCode("PCX");
//        truckWeighingVoucher.setId("CX"+stringToday+"-2001-PCN-FAC1");
//
//        given(truckWeighingVoucherRepository.findById("CX"+stringToday+"-001-PCX-FAC1")).willReturn(Optional.ofNullable(truckWeighingVoucher));
//        given(truckWeighingVoucherRepository.save(isA(TruckWeighingVoucher.class))).willAnswer(i-> i.getArgument(0));
//        mockMvc.perform(post("/v1/truck-weighing-vouchers")
//                .header("department_id","FAC1").contentType(APPLICATION_JSON_UTF8)
//                .content(jsonCreatePCXRequest)).andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("note"));
//    }

    @Test
    public void testGetListTruckWeighingVoucherSuccess() throws Exception {
        List<Object[]> objects = new ArrayList<>();
        List<GoodsInOut> list = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            truckWeighingVoucher = new TruckWeighingVoucher();
            truckWeighingVoucher.setNo("no"+i);
            truckWeighingVoucher.setId("id"+i);
            truckWeighingVoucher.setVoucherAt(Instant.now());
            truckWeighingVoucher.setVoucherCode("voucher_code"+i);
            truckWeighingVoucher.setCreatedByFullName("do van hung"+i);
            truckWeighingVoucher.setTruckLicensePlateNumber("0number"+i);
            truckWeighingVoucher.setTruckDriverId("id"+i);
            truckWeighingVoucher.setCompanyId("id"+i);
            truckWeighingVoucher.setCustomerId("id"+i);
            truckWeighingVoucher.setNo("no"+i);
            truckWeighingVoucher.setNote("note"+i);

            GoodsInOut goodsInOut = new GoodsInOut();
            goodsInOut.setVoucherId("voucher"+i);
            goodsInOut.setVoucherNo("voucher_no"+i);
            list.add(goodsInOut);

            Company company = new Company().setPhoneNumber("03349323"+i).setName("hung"+i).setId("id"+i).setTaxCode("taxCode"+i).setAddress("address"+i);

            Customer customer = new Customer().setTaxCode("taxCode"+i).setName("nhan"+i).setId("id"+i).setCategory("category"+i).setGroupId("groupId"+i).setPhoneNumber("03225433"+i).setAddress("viet nam"+i).setCompanyId("ceti"+i);

            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver"+i).setAbbreviatedName("hung" +i).setId("id"+i).setTruckLicensePlateNumber("12b"+i).setAddress("bac giang"+i);

            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343"+i);

            Product product = new Product().setId("id"+i).setReferenceProductId("preference"+i).setName("clear"+i).setUnit("don vi"+i).setCategoryId("category"+i);
            Object[] object = new Object[10];
            object[0] = truckWeighingVoucher;
            object[1] = company;
            object[2] = customer;
            object[3] = truckDriver;
            object[4] = truck;
            object[5] = product;
            objects.add(object);
        }

        Pageable pageable = PageRequest.of(0, 20);

        ResultPage<Object[]> result = new ResultPage<Object[]>();
        result.setPageList(objects);
        result.setTotalItems(5);

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("note","note");
        where.add("embed_goods_in_out", "true");

        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
        where1.add("note","note");
        where1.add("embed_goods_in_out", "true");
        where1.add("factory_id", "FAC1");

        given(truckWeighingVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);

        mockMvc.perform(get("/v1/truck-weighing-vouchers")
                .header("department_id","FAC1").params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].note").value("note0"));
    }

    @Test
    public void testDeleteTruckWeighingVoucherWithTruckWeighingVoucherNotExist() throws Exception{
        given(truckWeighingVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/truck-weighing-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu cân xe không tồn tại"));
    }

    @Test
    public void testDeleteTruckWeighingVoucherWithVoucherCodePCXSuccess() throws Exception{
        TruckWeighingVoucher truckWeighingVoucher = new TruckWeighingVoucher();
        truckWeighingVoucher.setVoucherCode("PCX");
        truckWeighingVoucher.setId("1");
        given(truckWeighingVoucherRepository.existsById("1")).willReturn(true);
        given(truckWeighingVoucherRepository.findById("1")).willReturn(Optional.of(truckWeighingVoucher));
        mockMvc.perform(delete("/v1/truck-weighing-vouchers/{id}", "1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }

    @Test
    public void testDeleteTruckWeighingVoucherWithVoucherCodePCNSuccess() throws Exception{
        TruckWeighingVoucher truckWeighingVoucher = new TruckWeighingVoucher();
        truckWeighingVoucher.setVoucherCode("PCN");
        truckWeighingVoucher.setId("1");
        given(truckWeighingVoucherRepository.existsById("1")).willReturn(true);
        given(truckWeighingVoucherRepository.findById("1")).willReturn(Optional.of(truckWeighingVoucher));
        mockMvc.perform(delete("/v1/truck-weighing-vouchers/{id}", "1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }

}
