package com.ceti.wholesale.controller.api.v1;


import static org.mockito.ArgumentMatchers.isA;
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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.repository.FactoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.FactoryImportVoucherMapper;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.FactoryImportVoucher;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.ProductAccessory;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.FactoryImportVoucherDetailRepository;
import com.ceti.wholesale.repository.FactoryImportVoucherRepository;
import com.ceti.wholesale.repository.GoodsInOutDetailRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.ProductAccessoryRepository;
import com.ceti.wholesale.repository.TruckWeighingVoucherRepository;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.FactoryImportVoucherService;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ProductAccessoryService;
import com.ceti.wholesale.service.impl.CylinderDebtServiceImpl;
import com.ceti.wholesale.service.impl.FactoryImportVoucherServiceImpl;
import com.ceti.wholesale.service.impl.GoodsInOutServiceImpl;
import com.ceti.wholesale.service.impl.ProductAccessoryServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(FactoryImportVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class FactoryImportVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        FactoryImportVoucherService factoryImportVoucherService() {
            return new FactoryImportVoucherServiceImpl() {
            };
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

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;

    @MockBean
    private TruckWeighingVoucherRepository truckWeighingVoucherRepository;

    @MockBean
    private FactoryImportVoucherRepository factoryImportVoucherRepository;

    @MockBean
    private FactoryImportVoucherDetailRepository factoryImportVoucherDetailRepository;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;

    @MockBean
    private GoodsInOutDetailRepository goodsInOutDetailRepository;

    @MockBean
    private CylinderDebtRepository cylinderDebtRepository;

    @MockBean
    private ProductAccessoryRepository productAccessoryRepository;

    @MockBean
    private FactoryImportVoucherMapper factoryImportVoucherMapper;

    @MockBean
    private VoucherUtils voucherUtils;

    @MockBean
    private VoucherMapper  voucherMapper;
    
    @MockBean
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

    private FactoryImportVoucher factoryImportVoucher = new FactoryImportVoucher();

    String id = "factory";

    String jsonCreateRequest = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"total_payment\":\"1000\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"G1\", " +
            "\"product_type\":\"GAS\"," +
            "\"weight\":\"19\"," +
            "   \"in_quantity\":\"100\", " +
            "\"out_quantity\":\"50\"," +
            "   \"type\":\"NDVO\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonCreateRequestWithTypeNKTH = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"total_payment\":\"1000\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"G1\", " +
            "\"product_type\":\"GAS\"," +
            "\"weight\":\"19\"," +
            "   \"in_quantity\":\"100\", " +
            "\"out_quantity\":\"50\"," +
            "   \"type\":\"NKTH\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonCreateRequestWithTypeNDVO = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"total_payment\":\"1000\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"G1\", " +
            "\"product_type\":\"VO\"," +
            "\"weight\":\"19\"," +
            "   \"in_quantity\":\"100\", " +
            "\"out_quantity\":\"50\"," +
            "   \"type\":\"NDVO\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";


    String jsonCreateRequestWithoutGoodsInOut = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"total_payment\":\"1000\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[" +
            "]}";

    String jsonUpdateRequest = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"update_by_id\":\"22\",\n" +
            "    \"update_by_full_name\":\"hung\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", " +
            "\"product_type\":\"GAS\"," +
            "\"type\":\"NKTH\"," +
            "\"weight\":\"19\"," +
            "   \"in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonUpdateRequestWithGASNKTH = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"update_by_id\":\"22\",\n" +
            "    \"update_by_full_name\":\"hung\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", " +
            "\"product_type\":\"GAS\"," +
            "\"type\":\"NKTH\"," +
            "\"weight\":\"19\"," +
            "   \"in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonUpdateRequestWithVONDVO = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"update_by_id\":\"22\",\n" +
            "    \"update_by_full_name\":\"hung\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", " +
            "\"product_type\":\"VO\"," +
            "\"type\":\"NDVO\"," +
            "\"weight\":\"19\"," +
            "   \"in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonUpdateRequestWithGASNDVO = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"update_by_id\":\"22\",\n" +
            "    \"update_by_full_name\":\"hung\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", " +
            "\"product_type\":\"GAS\"," +
            "\"type\":\"NDVO\"," +
            "\"weight\":\"19\"," +
            "   \"in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    @Test
    public void testUpdateFactoryImportVoucherSuccess() throws Exception{
        factoryImportVoucher = new FactoryImportVoucher();
        given(factoryImportVoucherRepository.findById(id)).willReturn(Optional.ofNullable(factoryImportVoucher));
        given(factoryImportVoucherRepository.save(isA(FactoryImportVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum("1", VoucherEnum.VOUCHER_CODE_PHIEU_NHAP_HANG_NHA_MAY,
                Instant.ofEpochSecond(95616));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));

        mockMvc.perform(patch("/v1/factory-import-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("hung"));
    }

    @Test
    public void testUpdateFactoryImportVoucherSuccessWithGASNKTH() throws Exception{
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setFactoryId("FAC1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1","FAC1")).willReturn(productAccessoryDtoList);
        factoryImportVoucher = new FactoryImportVoucher();
        factoryImportVoucher.setFactoryId("FAC1");
        given(factoryImportVoucherRepository.findById(id)).willReturn(Optional.ofNullable(factoryImportVoucher));
        given(factoryImportVoucherRepository.save(isA(FactoryImportVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum("FAC1", VoucherEnum.VOUCHER_CODE_PHIEU_NHAP_HANG_NHA_MAY,
                Instant.ofEpochSecond(95616));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));

        mockMvc.perform(patch("/v1/factory-import-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequestWithGASNKTH)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("hung"));
    }

    @Test
    public void testUpdateFactoryImportVoucherSuccessWithVONDVO() throws Exception{
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setFactoryId("1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1","1")).willReturn(productAccessoryDtoList);
        factoryImportVoucher = new FactoryImportVoucher();
        given(factoryImportVoucherRepository.findById(id)).willReturn(Optional.ofNullable(factoryImportVoucher));
        given(factoryImportVoucherRepository.save(isA(FactoryImportVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum("1", VoucherEnum.VOUCHER_CODE_PHIEU_NHAP_HANG_NHA_MAY,
                Instant.ofEpochSecond(95616));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));

        mockMvc.perform(patch("/v1/factory-import-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequestWithVONDVO)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("hung"));
    }

    @Test
    public void testUpdateFactoryImportVoucherSuccessWithGASNDVO() throws Exception{
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setFactoryId("FAC1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1","FAC1")).willReturn(productAccessoryDtoList);
        factoryImportVoucher = new FactoryImportVoucher();
        given(factoryImportVoucherRepository.findById(id)).willReturn(Optional.ofNullable(factoryImportVoucher));
        given(factoryImportVoucherRepository.save(isA(FactoryImportVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum("FAC1", VoucherEnum.VOUCHER_CODE_PHIEU_NHAP_HANG_NHA_MAY,
                Instant.ofEpochSecond(95616));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));

        mockMvc.perform(patch("/v1/factory-import-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequestWithGASNDVO)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("hung"));
    }

    @Test
    public void testUpdateFactoryImportVoucherWithFactoryImportVoucherNotExist() throws Exception{
        given(factoryImportVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/factory-import-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu nhập hàng không tồn tại"));
    }

    @Test
    public void testCreateFactoryImportVoucherWithGoodsInOutEmpty()throws Exception{
        mockMvc.perform(post("/v1/factory-import-vouchers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequestWithoutGoodsInOut))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phải có hàng mới tạo được phiếu"));
    }

//    @Test
//    public void testCreateFactoryImportVoucherSuccess() throws Exception{
//        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
//        ProductAccessory dto1 = new ProductAccessory().setMainProductId("G1").setFactoryId("FAC1").setSubProductId("S1").setId("S1").setSubProductType("S1").setSubProductQuantity(BigDecimal.valueOf(1000));
//        productAccessoryDtoList.add(dto1);
//        given(productAccessoryRepository.findByMainProductIdAndFactoryId("G1", "FAC1")).willReturn(productAccessoryDtoList);
//        given(factoryImportVoucherRepository.save(isA(FactoryImportVoucher.class))).willAnswer(i-> i.getArgument(0));
//        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
//        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(new AccountVoucherCode().setVoucherId("S1")));
//
//
//        mockMvc.perform(post("/v1/factory-import-vouchers")
//                .header("department_id","FAC1")
//                .contentType(APPLICATION_JSON_UTF8)
//                .content(jsonCreateRequest)).andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("sold voucher"));
//    }

//    @Test
//    public void testCreateFactoryImportVoucherSuccessWithTypeNKTH() throws Exception{
//        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
//        ProductAccessory dto1 = new ProductAccessory().setMainProductId("G1").setFactoryId("FAC1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("S1").setSubProductQuantity(BigDecimal.valueOf(1000));
//        productAccessoryDtoList.add(dto1);
//        given(productAccessoryRepository.findByMainProductIdAndFactoryId("G1", "FAC1")).willReturn(productAccessoryDtoList);
//        given(factoryImportVoucherRepository.save(isA(FactoryImportVoucher.class))).willAnswer(i-> i.getArgument(0));
//        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
//
//        mockMvc.perform(post("/v1/factory-import-vouchers")
//                .contentType(APPLICATION_JSON_UTF8)
//                .header("department_id","FAC1")
//                .content(jsonCreateRequestWithTypeNKTH))
//                .andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("sold voucher"));
//    }

//    @Test
//    public void testCreateFactoryImportVoucherSuccessWithTypeNDVO() throws Exception{
//        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
//        ProductAccessory dto1 = new ProductAccessory().setMainProductId("G1").setFactoryId("1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("S1").setSubProductQuantity(BigDecimal.valueOf(1000));
//        productAccessoryDtoList.add(dto1);
//
//
//
//
//        given(productAccessoryRepository.findByMainProductIdAndFactoryId("G1","1")).willReturn(productAccessoryDtoList);
//        given(factoryImportVoucherRepository.save(isA(FactoryImportVoucher.class))).willAnswer(i-> i.getArgument(0));
//        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
//
//        mockMvc.perform(post("/v1/factory-import-vouchers")
//                .contentType(APPLICATION_JSON_UTF8)
//                .header("department_id","FAC1")
//                .content(jsonCreateRequestWithTypeNDVO))
//                .andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("sold voucher"));
//    }


//    @Test
//    public void testGetListFactoryImportVoucherSuccessWithEmbedGoodInOutTrue() throws Exception {
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for(int i = 0; i < 5; i++){
//            factoryImportVoucher = new FactoryImportVoucher();
//            factoryImportVoucher.setNo("no"+i);
//            factoryImportVoucher.setId("id"+i);
//            factoryImportVoucher.setVoucherAt(Instant.now());
//            factoryImportVoucher.setVoucherCode("voucher_code"+i);
//            factoryImportVoucher.setTotalGoods(BigDecimal.valueOf(12));
//            factoryImportVoucher.setSalesmanId("id"+i);
//            factoryImportVoucher.setCreatedByFullName("do van hung"+i);
//            factoryImportVoucher.setTruckLicensePlateNumber("0number"+i);
//            factoryImportVoucher.setTruckDriverId("id"+i);
//            factoryImportVoucher.setCompanyId("id"+i);
//            factoryImportVoucher.setCustomerId("id"+i);
//            factoryImportVoucher.setNo("no"+i);
//            factoryImportVoucher.setNote("note"+i);
//            factoryImportVoucher.setFactoryId("FAC1");
//
//            GoodsInOut goodsInOut = new GoodsInOut();
//            goodsInOut.setVoucherId("voucher"+i);
//            goodsInOut.setVoucherNo("voucher_no"+i);
//            goodsInOut.setFactoryId("FAC1");
//            list.add(goodsInOut);
//
//            Company company = new Company().setPhoneNumber("03349323"+i).setName("hung"+i).setId("id"+i).setTaxCode("taxCode"+i).setAddress("address"+i);
//
//            Customer customer = new Customer().setTaxCode("taxCode"+i).setName("nhan"+i).setId("id"+i).setCategory("category"+i).setGroupId("groupId"+i).setPhoneNumber("03225433"+i).setAddress("viet nam"+i).setCompanyId("ceti"+i);
//
//            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver"+i).setAbbreviatedName("hung" +i).setId("id"+i).setTruckLicensePlateNumber("12b"+i).setAddress("bac giang"+i);
//
//            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343"+i);
//
//            Salesman salesman = new Salesman().setId("id"+i).setPhoneNumber("0324543"+i).setFullName("do van hung"+i).setAbbreviatedName("hung"+i).setAddress("ha nam"+i);
//
//            Object[] object = new Object[10];
//            object[0] = factoryImportVoucher;
//            object[1] = company;
//            object[2] = customer;
//            object[3] = truckDriver;
//            object[4] = truck;
//            object[5] = salesman;
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
//        where.add("note","note");
//        where.add("embed_goods_in_out", "true");
//
//        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
//        where1.add("note","note");
//        where1.add("embed_goods_in_out", "true");
//        where1.add("factory_id", "FAC1");
//
//        given(factoryImportVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);
//
////        given(goodsInOutRepository.getByVoucherId(id)).willReturn(list);
//        mockMvc.perform(get("/v1/factory-import-vouchers")
//                .header("department_id","FAC1")
//                .params(where))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }
//
//    @Test
//    public void testGetListFactoryImportVoucherSuccessWithEmbedGoodInOutFalse() throws Exception {
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for(int i = 0; i < 5; i++){
//            factoryImportVoucher = new FactoryImportVoucher();
//            factoryImportVoucher.setNo("no"+i);
//            factoryImportVoucher.setId("id"+i);
//            factoryImportVoucher.setVoucherAt(Instant.now());
//            factoryImportVoucher.setVoucherCode("voucher_code"+i);
//            factoryImportVoucher.setTotalGoods(BigDecimal.valueOf(12));
//            factoryImportVoucher.setSalesmanId("id"+i);
//            factoryImportVoucher.setCreatedByFullName("do van hung"+i);
//            factoryImportVoucher.setTruckLicensePlateNumber("0number"+i);
//            factoryImportVoucher.setTruckDriverId("id"+i);
//            factoryImportVoucher.setCompanyId("id"+i);
//            factoryImportVoucher.setCustomerId("id"+i);
//            factoryImportVoucher.setNo("no"+i);
//            factoryImportVoucher.setNote("note"+i);
//            factoryImportVoucher.setFactoryId("FAC1");
//
//            GoodsInOut goodsInOut = new GoodsInOut();
//            goodsInOut.setVoucherId("voucher"+i);
//            goodsInOut.setVoucherNo("voucher_no"+i);
//            goodsInOut.setFactoryId("FAC1");
//            list.add(goodsInOut);
//
//            Company company = new Company().setPhoneNumber("03349323"+i).setName("hung"+i).setId("id"+i).setTaxCode("taxCode"+i).setAddress("address"+i);
//
//            Customer customer = new Customer().setTaxCode("taxCode"+i).setName("nhan"+i).setId("id"+i).setCategory("category"+i).setGroupId("groupId"+i).setPhoneNumber("03225433"+i).setAddress("viet nam"+i).setCompanyId("ceti"+i);
//
//            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver"+i).setAbbreviatedName("hung" +i).setId("id"+i).setTruckLicensePlateNumber("12b"+i).setAddress("bac giang"+i);
//
//            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343"+i);
//
//            Salesman salesman = new Salesman().setId("id"+i).setPhoneNumber("0324543"+i).setFullName("do van hung"+i).setAbbreviatedName("hung"+i).setAddress("ha nam"+i);
//
//            Object[] object = new Object[10];
//            object[0] = factoryImportVoucher;
//            object[1] = company;
//            object[2] = customer;
//            object[3] = truckDriver;
//            object[4] = truck;
//            object[5] = salesman;
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
//        where.add("note","note");
//
//        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
//        where1.add("note","note");
//        where1.add("factory_id", "FAC1");
//
//        given(factoryImportVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);
//
//        mockMvc.perform(get("/v1/factory-import-vouchers").params(where)
//                .header("department_id","FAC1")).andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }

    @Test
    public void testDeleteFactoryImportVoucherWithFactoryImportVoucherNotExist() throws Exception{
        given(factoryImportVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/factory-import-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu nhập nhà máy không tồn tại"));
    }

    @Test
    public void testDeletePaymentVoucherSuccess()throws Exception{
        FactoryImportVoucher factoryImportVoucher = new FactoryImportVoucher();
        factoryImportVoucher.setId("1");
        given(factoryImportVoucherRepository.findById("1")).willReturn(Optional.ofNullable(factoryImportVoucher));
        mockMvc.perform(delete("/v1/factory-import-vouchers/{id}", "1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }

    @Test
    public void testGetAllSuccess() throws Exception {
        List<VoucherDto> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            VoucherDto item = new VoucherDto().setId("A" + i)
                    .setTotalGoods(new BigDecimal(i + 1)).setCompanyId("CompanyId" + i).setFactoryId("FactoryId" + i).setNo("No" + i)
                    .setNote("Note" + i).setTruckDriverFullName("TruckDriverFullName" + i);
            list.add(item);
        }
        Pageable pageable = PageRequest.of(0, 20);
        String page = PageableProcess.PageToSqlQuery(pageable, "factory_import_voucher");
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "factory_import_voucher");
        HashMap<String, String> where1 = new HashMap<>();
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id","FAC1");
        given(voucherMapper.getList(where1,pagingStr, page, null, null)).willReturn(list);
        given(voucherMapper.countList(where1, page, null, null)).willReturn(5l);

        mockMvc.perform(get("/v1/factory-import-vouchers").header("department_id","FAC1")
                        .params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }

}
