package com.ceti.wholesale.controller.api.v1;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.model.*;
import com.ceti.wholesale.repository.FactoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.ExportImportVoucherDetailRepository;
import com.ceti.wholesale.repository.ExportVoucherRepository;
import com.ceti.wholesale.repository.GoodsInOutDetailRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.ImportVoucherRepository;
import com.ceti.wholesale.repository.ProductAccessoryRepository;
import com.ceti.wholesale.repository.TruckWeighingVoucherRepository;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.ExportImportVoucherService;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ProductAccessoryService;
import com.ceti.wholesale.service.impl.CylinderDebtServiceImpl;
import com.ceti.wholesale.service.impl.ExportImportVoucherServiceImpl;
import com.ceti.wholesale.service.impl.GoodsInOutServiceImpl;
import com.ceti.wholesale.service.impl.ProductAccessoryServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(ExportImportVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ExportImportVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        ExportImportVoucherService exportImportVoucherService() {
            return new ExportImportVoucherServiceImpl();
        }

        @Bean
        GoodsInOutService goodsInOutService() {
            return new GoodsInOutServiceImpl();
        }

        @Bean
        CylinderDebtService cylinderDebtService() {
            return new CylinderDebtServiceImpl();
        }

        @Bean
        ProductAccessoryService productAccessoryService() {
            return new ProductAccessoryServiceImpl();
        }
    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TruckWeighingVoucherRepository truckWeighingVoucherRepository;

    @MockBean
    private ImportVoucherRepository importVoucherRepository;

    @MockBean
    private ExportVoucherRepository exportVoucherRepository;

    @MockBean
    private ExportImportVoucherDetailRepository exportImportVoucherDetailRepository;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;

    @MockBean
    private GoodsInOutDetailRepository goodsInOutDetailRepository;

    @MockBean
    private CylinderDebtRepository cylinderDebtRepository;
    @MockBean
    private VoucherUtils voucherUtils;

    @MockBean
    private ProductAccessoryRepository productAccessoryRepository;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

    String idExportVocuher = "export";
    String idImportVoucher = "import";

    private ExportVoucher exportVoucher = new ExportVoucher();
    private ImportVoucher importVoucher = new ImportVoucher();

    String jsonCreateRequestAllVocuher = "{" +
            " \"create_import_voucher\" : true,\n" +
            " \"create_import_voucher_request\" :" +
            "    {\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_receivable\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"type\":\"NKTH\", \"product_type\":\"VO\", " +
            "   \"in_quantity\":\"100\"," +
            "   \"type\":\"XDVO\"," +
            "\"factory_id\":\"1\"," +
            " \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}," +
            "\"create_export_voucher\" : true, " +
            " \"create_export_voucher_request\" :" +
            "    {\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_receivable\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"type\":\"NKTH\", \"product_type\":\"VO\", " +
            "   \"in_quantity\":\"100\"," +
            "   \"type\":\"XDVO\"," +
            "\"factory_id\":\"1\"," +
            " \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}" +
            "}";

    String jsonCreateRequestImportVocuher = "{" +
            " \"create_import_voucher\" : true,\n" +
            " \"create_import_voucher_request\" :" +
            "    {\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_receivable\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"type\":\"NKTH\", \"product_type\":\"VO\", " +
            "   \"in_quantity\":\"100\"," +
            "   \"type\":\"XDVO\"," +
            "\"factory_id\":\"1\"," +
            " \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]},\n" +
            "\"create_export_voucher\" : false \n" +
            "}";

    String jsonCreateRequestExportVocuher = "{" +
            " \"create_export_voucher\" : true,\n" +
            " \"create_export_voucher_request\" :" +
            "    {\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_receivable\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"type\":\"NKTH\", \"product_type\":\"VO\", " +
            "   \"in_quantity\":\"100\"," +
            "   \"type\":\"XDVO\"," +
            "\"factory_id\":\"1\"," +
            " \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]},\n" +
            "\"create_import_voucher\" : false \n" +
            "}";


    String jsonUpdateRequestWith2Voucher =
            "{\n" +
                    "    \"update_import_voucher\": true,\n" +
                    "    \"update_import_voucher_request\": {\n" +
                    "        \"id\" : \"import\",\n   " +
                    "        \"total_goods\": 567.89,\n" +
                    "        \"total_receivable\": 789.10,\n" +
                    "        \"goods_in_out\": []\n" +
                    "    },\n" +
                    "    \"update_export_voucher\": true,\n" +
                    "    \"update_export_voucher_request\": {\n" +
                    "        \"id\" : \"export\",\n   " +
                    "        \"total_goods\": 134.5,\n" +
                    "        \"total_receivable\": 167.5,\n" +
                    "        \"goods_in_out\": []\n" +
                    "    }\n" +
                    "}";

    String jsonUpdateRequestExportVocuher =
            "{\n" +
                    "    \"update_import_voucher\": false,\n" +
                    "    \"update_import_voucher_request\": {\n" +
                    "        \"id\" : \"import\",\n   " +
                    "        \"total_goods\": 567.89,\n" +
                    "        \"total_receivable\": 789.10,\n" +
                    "        \"goods_in_out\": []\n" +
                    "    },\n" +
                    "    \"update_export_voucher\": true,\n" +
                    "    \"update_export_voucher_request\": {\n" +
                    "        \"id\" : \"export\",\n   " +
                    "        \"total_goods\": 134.5,\n" +
                    "        \"total_receivable\": 167.5,\n" +
                    "        \"goods_in_out\": []\n" +
                    "    }\n" +
                    "}";

    String jsonUpdateRequestImportVocuher =
            "{\n" +
                    "    \"update_import_voucher\": true,\n" +
                    "    \"update_import_voucher_request\": {\n" +
                    "        \"id\" : \"import\",\n   " +
                    "        \"total_goods\": 567.89,\n" +
                    "        \"total_receivable\": 789.10,\n" +
                    "        \"goods_in_out\": []\n" +
                    "    },\n" +
                    "    \"update_export_voucher\": false,\n" +
                    "    \"update_export_voucher_request\": {\n" +
                    "        \"id\" : \"export\",\n   " +
                    "        \"total_goods\": 134.5,\n" +
                    "        \"total_receivable\": 167.5,\n" +
                    "        \"goods_in_out\": []\n" +
                    "    }\n" +
                    "}";

    String jsonUpdateRequestImportVocuherAndCreateExportVoucher =
            "{\n" +
                    "    \"update_import_voucher\": true,\n" +
                    "    \"update_import_voucher_request\": {\n" +
                    "        \"id\" : \"import\",\n   " +
                    "        \"total_goods\": 567.89,\n" +
                    "        \"total_receivable\": 789.10,\n" +
                    "        \"goods_in_out\": []\n" +
                    "    },\n" +
                    "    \"update_export_voucher\": true,\n" +
                    "    \"update_export_voucher_request\": {\n" +
                    "        \"total_goods\": 567.89,\n" +
                    "        \"total_receivable\": 789.10,\n" +
                    "        \"goods_in_out\": []\n" +
                    "    },\n" +
                    "    \"create_export_voucher_request\":" +
                    "    {\n" +
                    "    \"company_id\":\"ceti\",\n" +
                    "    \"voucher_at\":\"95616\",\n" +
                    "    \"customer_id\":\"hung001\",\n" +
                    "    \"truck_driver_id\":\"nhan01\",\n" +
                    "    \"truck_license_plate_number\":\"12\",\n" +
                    "    \"total_goods\":\"12\",\n" +
                    "    \"total_receivable\":\"54\",\n" +
                    "    \"note\":\"sold voucher\",\n" +
                    "    \"created_by_full_name\":\"kim van ha\",\n" +
                    "    \"goods_in_out\":" +
                    "[{ \"product_id\":\"S1\", \"type\":\"NKTH\", \"product_type\":\"VO\", " +
                    "   \"in_quantity\":\"100\"," +
                    "   \"type\":\"XDVO\"," +
                    "\"factory_id\":\"1\"," +
                    " \"out_quantity\":\"50\"," +
                    " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
                    "}]}\n" +
                    "}";
    String jsonUpdateRequestExportVocuherAndCreateImportVoucher =
            "{\n" +
                    "    \"update_import_voucher\": true,\n" +
                    "    \"update_import_voucher_request\": {\n" +
                    "        \"total_goods\": 567.89,\n" +
                    "        \"total_receivable\": 789.10,\n" +
                    "        \"goods_in_out\": []\n" +
                    "    },\n" +
                    "    \"create_import_voucher_request\":" +
                    "    {\n" +
                    "    \"company_id\":\"ceti\",\n" +
                    "    \"voucher_at\":\"95616\",\n" +
                    "    \"customer_id\":\"hung001\",\n" +
                    "    \"truck_driver_id\":\"nhan01\",\n" +
                    "    \"truck_license_plate_number\":\"12\",\n" +
                    "    \"total_goods\":\"12\",\n" +
                    "    \"total_receivable\":\"54\",\n" +
                    "    \"note\":\"sold voucher\",\n" +
                    "    \"created_by_full_name\":\"kim van ha\",\n" +
                    "    \"goods_in_out\":" +
                    "[{ \"product_id\":\"S1\", \"type\":\"NKTH\", \"product_type\":\"VO\", " +
                    "   \"in_quantity\":\"100\"," +
                    "   \"type\":\"XDVO\"," +
                    "\"factory_id\":\"1\"," +
                    " \"out_quantity\":\"50\"," +
                    " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
                    "}]},\n" +
                    "    \"update_export_voucher\": true,\n" +
                    "    \"update_export_voucher_request\": {\n" +
                    "        \"id\" : \"export\",\n   " +
                    "        \"total_goods\": 134.5,\n" +
                    "        \"total_receivable\": 167.5,\n" +
                    "        \"goods_in_out\": []\n" +
                    "    }\n" +
                    "}";


//    @Test
//    public void testGetListExportImportVoucherSuccessWithEmbedGoodInOutTrue() throws Exception {
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            exportVoucher = new ExportVoucher();
//            exportVoucher.setNo("no" + i);
//            exportVoucher.setId("id" + i);
//            exportVoucher.setVoucherAt(Instant.now());
//            exportVoucher.setVoucherCode("voucher_code" + i);
//            exportVoucher.setTotalGoods(BigDecimal.valueOf(12));
//            exportVoucher.setTotalReceivable(BigDecimal.valueOf(32));
//            exportVoucher.setCreatedByFullName("do van hung" + i);
//            exportVoucher.setTruckLicensePlateNumber("0number" + i);
//            exportVoucher.setTruckDriverId("id" + i);
//            exportVoucher.setCompanyId("id" + i);
//            exportVoucher.setCustomerId("id" + i);
//            exportVoucher.setNo("no" + i);
//            exportVoucher.setNote("note" + i);
//
//            importVoucher = new ImportVoucher();
//            importVoucher.setNo("no" + i);
//            importVoucher.setId("importId" + i);
//            importVoucher.setVoucherAt(Instant.now());
//            importVoucher.setExportVoucherId("id" + i);
//            importVoucher.setVoucherCode("voucher_code" + i);
//            importVoucher.setTotalGoods(BigDecimal.valueOf(12));
//            importVoucher.setTotalReceivable(BigDecimal.valueOf(32));
//            importVoucher.setCreatedByFullName("do van hung" + i);
//            importVoucher.setTruckLicensePlateNumber("0number" + i);
//            importVoucher.setTruckDriverId("id" + i);
//            importVoucher.setCompanyId("id" + i);
//            importVoucher.setCustomerId("id" + i);
//            importVoucher.setNo("no" + i);
//            importVoucher.setNote("note" + i);
//
//            GoodsInOut goodsInOut = new GoodsInOut();
//            goodsInOut.setVoucherId("voucher" + i);
//            goodsInOut.setVoucherNo("voucher_no" + i);
//            list.add(goodsInOut);
//
//            Customer customer = new Customer().setTaxCode("taxCode" + i).setName("nhan" + i).setId("id" + i).setCategory("category" + i).setGroupId("groupId" + i).setPhoneNumber("03225433" + i).setAddress("viet nam" + i).setCompanyId("ceti" + i);
//
//            Object[] object = new Object[10];
//            object[0] = importVoucher;
//            object[1] = customer;
//            object[2] = exportVoucher;
//            objects.add(object);
//        }
//
//        Pageable pageable = PageRequest.of(0, 20);
//
//        ResultPage<Object[]> result = new ResultPage<Object[]>();
//        result.setPageList(objects);
//        result.setTotalItems(5);
//
//        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
//        where.add("embed_goods_in_out", "true");
//        where.add("note", "note");
//
//        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
//        where1.add("note", "note");
////        where1.add("factory_id", "FAC1");
//        where1.add("embed_goods_in_out", "true");
//
//        given(exportImportVoucherDetailRepository.findAllWithFilter(pageable, where1, "FAC1")).willReturn(result);
//
////        given(goodsInOutRepository.getByVoucherId(id)).willReturn(list);
//        mockMvc.perform(get("/v1/export-import-vouchers")
//                .header("department_id", "FAC1")
//                .params(where)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5));
//    }


    @Test
    public void testGetListExportImportVoucherSuccess() throws Exception {
        List<Object[]> objects = new ArrayList<>();
        List<GoodsInOut> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            exportVoucher = new ExportVoucher();
            exportVoucher.setNo("no" + i);
            exportVoucher.setId("id" + i);
            exportVoucher.setVoucherAt(Instant.now());
            exportVoucher.setVoucherCode("voucher_code" + i);
            exportVoucher.setTotalGoods(BigDecimal.valueOf(12));
            exportVoucher.setTotalReceivable(BigDecimal.valueOf(32));
            exportVoucher.setCreatedByFullName("do van hung" + i);
            exportVoucher.setTruckLicensePlateNumber("0number" + i);
            exportVoucher.setTruckDriverId("id" + i);
            exportVoucher.setCompanyId("id" + i);
            exportVoucher.setCustomerId("id" + i);
            exportVoucher.setNo("no" + i);
            exportVoucher.setNote("note" + i);

            importVoucher = new ImportVoucher();
            importVoucher.setNo("no" + i);
            importVoucher.setId("importId" + i);
            importVoucher.setVoucherAt(Instant.now());
            importVoucher.setExportVoucherId("id" + i);
            importVoucher.setVoucherCode("voucher_code" + i);
            importVoucher.setTotalGoods(BigDecimal.valueOf(12));
            importVoucher.setTotalReceivable(BigDecimal.valueOf(32));
            importVoucher.setCreatedByFullName("do van hung" + i);
            importVoucher.setTruckLicensePlateNumber("0number" + i);
            importVoucher.setTruckDriverId("id" + i);
            importVoucher.setCompanyId("id" + i);
            importVoucher.setCustomerId("id" + i);
            importVoucher.setNo("no" + i);
            importVoucher.setNote("note" + i);

            GoodsInOut goodsInOut = new GoodsInOut();
            goodsInOut.setVoucherId("voucher" + i);
            goodsInOut.setVoucherNo("voucher_no" + i);
            list.add(goodsInOut);


            Customer customer = new Customer().setTaxCode("taxCode" + i).setName("nhan" + i).setId("id" + i).setCategory("category" + i).setGroupId("groupId" + i).setPhoneNumber("03225433" + i).setAddress("viet nam" + i).setCompanyId("ceti" + i);

            Object[] object = new Object[10];
            object[0] = importVoucher;
            object[2] = customer;
            object[1] = exportVoucher;
            objects.add(object);
        }

        Pageable pageable = PageRequest.of(0, 20);
        ResultPage<Object[]> result = new ResultPage<Object[]>();
        result.setPageList(objects);
        result.setTotalItems(5);

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("note", "note");

        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
        where1.add("note", "note");
//        where1.add("factory_id", "FAC1");

        given(exportImportVoucherDetailRepository.findAllWithFilter(pageable, where1,"FAC1")).willReturn(result);

        mockMvc.perform(get("/v1/export-import-vouchers")
                .header("department_id", "FAC1").params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5));
    }

    @Test
    public void testCreateExportImportVoucherWithExportVoucherOnly() throws Exception {
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setFactoryId("1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "1")).willReturn(productAccessoryDtoList);
        given(exportVoucherRepository.save(isA(ExportVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(post("/v1/export-import-vouchers")
                .header("department_id", "FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequestExportVocuher)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Created"));
    }

    @Test
    public void testCreateExportImportVoucherWithImportVoucherOnly() throws Exception {
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setFactoryId("1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "1")).willReturn(productAccessoryDtoList);
        given(importVoucherRepository.save(isA(ImportVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(post("/v1/export-import-vouchers")
                .header("department_id", "FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequestImportVocuher)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Created"));
    }

    @Test
    public void testCreateExportImportVoucherSuccess() throws Exception {
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setFactoryId("1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "1")).willReturn(productAccessoryDtoList);
        given(exportVoucherRepository.save(isA(ExportVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(importVoucherRepository.save(isA(ImportVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(post("/v1/export-import-vouchers")
                .header("department_id", "FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequestAllVocuher)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Created"));
    }

    @Test
    public void testUpdateExportImportVoucherSuccess() throws Exception {
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setSubProductId("GBONAPCAO").setFactoryId("FAC1").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "FAC1")).willReturn(productAccessoryDtoList);
        ExportVoucher exportVoucher = new ExportVoucher();
        exportVoucher.setFactoryId("FAC1");
        exportVoucher.setId(idExportVocuher);
        ImportVoucher importVoucher = new ImportVoucher();
        importVoucher.setFactoryId("FAC1");
        importVoucher.setId(idImportVoucher);
        given(exportVoucherRepository.findById(idExportVocuher)).willReturn(Optional.ofNullable(exportVoucher));
        given(exportVoucherRepository.save(isA(ExportVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(importVoucherRepository.findById(idImportVoucher)).willReturn(Optional.ofNullable(importVoucher));
        given(importVoucherRepository.save(isA(ImportVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(patch("/v1/export-import-vouchers")
                .header("department_id", "FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequestWith2Voucher)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @Test
    public void testUpdateImportVoucherSuccess() throws Exception {
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setSubProductId("GBONAPCAO").setFactoryId("FAC1").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "FAC1")).willReturn(productAccessoryDtoList);
        ImportVoucher importVoucher = new ImportVoucher();
        importVoucher.setFactoryId("FAC1");
        importVoucher.setId(idImportVoucher);
        given(importVoucherRepository.findById(idImportVoucher)).willReturn(Optional.ofNullable(importVoucher));
        given(importVoucherRepository.save(isA(ImportVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(patch("/v1/export-import-vouchers")
                .header("department_id", "FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequestImportVocuher)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @Test
    public void testUpdateExportVoucherSuccess() throws Exception {
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setSubProductId("GBONAPCAO").setFactoryId("FAC1").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "FAC1")).willReturn(productAccessoryDtoList);
        ExportVoucher exportVoucher = new ExportVoucher();
        exportVoucher.setFactoryId("FAC1");
        exportVoucher.setId(idExportVocuher);
        given(exportVoucherRepository.findById(idExportVocuher)).willReturn(Optional.ofNullable(exportVoucher));
        given(exportVoucherRepository.save(isA(ExportVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(patch("/v1/export-import-vouchers")
                .header("department_id", "FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequestExportVocuher)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @Test
    public void testUpdateImportVoucherAndCreateExportVoucherSuccess() throws Exception {
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setSubProductId("GBONAPCAO").setFactoryId("FAC1").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "FAC1")).willReturn(productAccessoryDtoList);
        ImportVoucher importVoucher = new ImportVoucher();
        importVoucher.setFactoryId("FAC1");
        importVoucher.setId(idImportVoucher);
        given(exportVoucherRepository.save(isA(ExportVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(importVoucherRepository.findById(idImportVoucher)).willReturn(Optional.ofNullable(importVoucher));
        given(importVoucherRepository.save(isA(ImportVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(patch("/v1/export-import-vouchers")
                .header("department_id", "FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequestImportVocuherAndCreateExportVoucher)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @Test
    public void testUpdateExportVoucherAndCreateImportVoucherSuccess() throws Exception {
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setSubProductId("GBONAPCAO").setFactoryId("FAC1").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "FAC1")).willReturn(productAccessoryDtoList);
        ExportVoucher exportVoucher = new ExportVoucher();
        exportVoucher.setFactoryId("FAC1");
        exportVoucher.setId(idExportVocuher);
        given(exportVoucherRepository.findById(idExportVocuher)).willReturn(Optional.ofNullable(exportVoucher));
        given(exportVoucherRepository.save(isA(ExportVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(importVoucherRepository.save(isA(ImportVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(patch("/v1/export-import-vouchers")
                .header("department_id", "FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequestExportVocuherAndCreateImportVoucher)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

//    @Test
//    public void testDeleteExportImportVoucherWithExportVoucherNotExist() throws Exception{
//        given(exportVoucherRepository.findById(idExportVocuher)).willReturn(Optional.empty());
//
//        mockMvc.perform(delete("/v1/export-import-vouchers").contentType(APPLICATION_JSON_UTF8))
//                .andExpect(status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu nhập không tồn tại"));
//    }
//
//    @Test
//    public void testDeleteExportImportVoucherWithImportVoucherNotExist() throws Exception{
//        given(importVoucherRepository.findById(idImportVoucher)).willReturn(Optional.empty());
//
//        mockMvc.perform(delete("/v1/export-import-vouchers").contentType(APPLICATION_JSON_UTF8))
//                .andExpect(status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu nhập không tồn tại"));
//    }

    @Test
    public void testDeleteExportImportVoucher() throws Exception{
        mockMvc.perform(delete("/v1/export-import-vouchers").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }

    @Test
    public void testGetListExportImportVoucherDetail() throws Exception {
        List<Object[]> objectsImport = new ArrayList<>();
        List<Object[]> objectsExport = new ArrayList<>();
        List<GoodsInOut> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            exportVoucher = new ExportVoucher();
            exportVoucher.setNo("no" + i);
            exportVoucher.setId("exportId" + i);
            exportVoucher.setVoucherAt(Instant.now());
            exportVoucher.setVoucherCode("voucher_code" + i);
            exportVoucher.setTotalGoods(BigDecimal.valueOf(12));
            exportVoucher.setTotalReceivable(BigDecimal.valueOf(32));
            exportVoucher.setCreatedByFullName("nhat" + i);
            exportVoucher.setTruckLicensePlateNumber("0number" + i);
            exportVoucher.setTruckDriverId("id" + i);
            exportVoucher.setCompanyId("id" + i);
            exportVoucher.setCustomerId("id" + i);
            exportVoucher.setNo("no" + i);
            exportVoucher.setNote("note" + i);

            importVoucher = new ImportVoucher();
            importVoucher.setNo("no" + i);
            importVoucher.setId("importId" + i);
            importVoucher.setVoucherAt(Instant.now());
            importVoucher.setExportVoucherId("id" + i);
            importVoucher.setVoucherCode("voucher_code" + i);
            importVoucher.setTotalGoods(BigDecimal.valueOf(12));
            importVoucher.setTotalReceivable(BigDecimal.valueOf(32));
            importVoucher.setCreatedByFullName("nhat" + i);
            importVoucher.setTruckLicensePlateNumber("0number" + i);
            importVoucher.setTruckDriverId("id" + i);
            importVoucher.setCompanyId("id" + i);
            importVoucher.setCustomerId("id" + i);
            importVoucher.setNo("no" + i);
            importVoucher.setNote("note" + i);

            GoodsInOut goodsInOut = new GoodsInOut();
            goodsInOut.setVoucherId("voucher" + i);
            goodsInOut.setVoucherNo("voucher_no" + i);
            list.add(goodsInOut);

            Customer customer = new Customer().setTaxCode("taxCode" + i).setName("nhat" + i).setId("id" + i)
                    .setCategory("category" + i).setGroupId("groupId" + i).setPhoneNumber("03225433" + i)
                    .setAddress("viet nam" + i).setCompanyId("ceti" + i);

            Company company = new Company().setAddress("Address" + i).setName("Name" + i).setId("Id" + i)
                    .setTaxCode("TaxCode" + i).setPhoneNumber("PhoneNumber" + i).setIsActive(true);

            TruckDriver truckDriver = new TruckDriver().setAddress("Address" + i).setTruckLicensePlateNumber("TruckLicensePlateNumber" +i).setId("Id" + i)
                    .setAbbreviatedName("AbbreviatedName" +i).setFullName("FullName" + i).setPhoneNumber("PhoneNumber" + i).setIsActive(true);

            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(i+1)).setTruckVersion("TruckVersion" + i)
                    .setFactoryId("FactoryId" + i).setLicensePlateNumber("LicensePlateNumber" + i)
                    .setManufacturers("Manufacturers" + i).setMadeIn("MadeIn" + i).setNumberKM(BigDecimal.valueOf(i+1))
                    .setTrunkSize("TrunkSize" + i).setTrunkType("TrunkType" + i).setDORatio(BigDecimal.valueOf(i + 1))
                    .setCarInspectionTimeNext(Instant.ofEpochSecond(123 + i)).setCarInsurancePresent(Instant.ofEpochSecond(123 + i))
                    .setExplosivesTransportPaperPresent(Instant.ofEpochSecond(123 + i));

            Salesman salesman = new Salesman().setId("Id" + i).setAbbreviatedName("AbbreviatedName" + i).setFullName("FullName" + i).setAddress("Address" + i)
                    .setFactoryId("FactoryId" + i).setIsActive(true).setPhoneNumber("PhoneNumber" + i);

            Object[] objectImportVoucher = new Object[10];
            objectImportVoucher[0] = importVoucher;
            objectImportVoucher[1] = company;
            objectImportVoucher[2] = customer;
            objectImportVoucher[3] = truckDriver;
            objectImportVoucher[4] = truck;
            objectImportVoucher[5] = salesman;
            objectsImport.add(objectImportVoucher);

            Object[] objectExportVoucher = new Object[10];
            objectExportVoucher[0] = exportVoucher;
            objectExportVoucher[1] = company;
            objectExportVoucher[2] = customer;
            objectExportVoucher[3] = truckDriver;
            objectExportVoucher[4] = truck;
            objectExportVoucher[5] = salesman;
            objectsExport.add(objectExportVoucher);
        }

        Object[] objectExportVoucher = new Object[10];
        objectExportVoucher[0] = objectsImport;

        Object[] objectImportVoucher = new Object[10];
        objectImportVoucher[0] = objectsExport;

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id", "FAC1");

        given(exportImportVoucherDetailRepository.getDetailImportVoucher("importId")).willReturn(objectImportVoucher);
        given(exportImportVoucherDetailRepository.getDetailExportVoucher("exportId")).willReturn(objectExportVoucher);
        mockMvc.perform(get("/v1/export-import-vouchers/detail")
                        .header("department_id", "FAC1").params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(0));
    }

}
