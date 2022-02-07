package com.ceti.wholesale.controller.api.v1;


import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.FactoryExportVoucherMapper;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.FactoryExportVoucher;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.ProductAccessory;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.FactoryExportVoucherDetailRepository;
import com.ceti.wholesale.repository.FactoryExportVoucherRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.GoodsInOutDetailRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.ProductAccessoryRepository;
import com.ceti.wholesale.repository.TruckWeighingVoucherRepository;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.FactoryExportVoucherService;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ProductAccessoryService;
import com.ceti.wholesale.service.impl.CylinderDebtServiceImpl;
import com.ceti.wholesale.service.impl.FactoryExportVoucherServiceImpl;
import com.ceti.wholesale.service.impl.GoodsInOutServiceImpl;
import com.ceti.wholesale.service.impl.ProductAccessoryServiceImpl;
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

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FactoryExportVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class FactoryExportVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        FactoryExportVoucherService factoryExportVoucherService() {
            return new FactoryExportVoucherServiceImpl() {
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
    private FactoryExportVoucherRepository factoryExportVoucherRepository;

    @MockBean
    private VoucherMapper voucherMapper;

    @MockBean
    private FactoryExportVoucherDetailRepository factoryExportVoucherDetailRepository;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;

    @MockBean
    private GoodsInOutDetailRepository goodsInOutDetailRepository;

    @MockBean
    private CylinderDebtRepository cylinderDebtRepository;

    @MockBean
    private ProductAccessoryRepository productAccessoryRepository;

    @MockBean
    private FactoryExportVoucherMapper factoryExportVoucherMapper;
    
    @MockBean
    private AccountVoucherCodeRepository accountVoucherCodeRepository;
    
    @MockBean
    private VoucherUtils voucherUtils;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

    private FactoryExportVoucher factoryExportVoucher = new FactoryExportVoucher();

    String id = "factory";

    String jsonCreateRequest = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_payment_received\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"type\":\"NKTH\", \"product_type\":\"VO\", " +
            "   \"in_quantity\":\"100\"," +
            "   \"type\":\"XDVO\"," +
            "\"factory_id\":\"1\"," +
            " \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonCreateRequestWithTypeXTTH = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_payment_received\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"type\":\"XTTH\", \"product_type\":\"GAS\", " +
            "   \"in_quantity\":\"100\"," +
            " \"out_quantity\":\"50\"," +
            "\"factory_id\":\"1\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonCreateRequestWithTypeGASXDVO = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_payment_received\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"type\":\"XDVO\", \"product_type\":\"GAS\", " +
            "   \"in_quantity\":\"100\"," +
            " \"out_quantity\":\"50\"," +
            "\"factory_id\":\"1\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonCreateRequestWithTypeVOXDVO = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_payment_received\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"type\":\"XDVO\", \"product_type\":\"VO\", " +
            "   \"in_quantity\":\"100\"," +
            " \"out_quantity\":\"50\"," +
            "\"factory_id\":\"1\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonCreateRequestWithoutGoodsInOut = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_payment_received\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[]}";

    String jsonUpdateRequest = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_payment_received\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"update_by_id\":\"22\",\n" +
            "    \"update_by_full_name\":\"hung\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"type\":\"NKTH\", \"product_type\":\"GAS\", " +
            "   \"in_quantity\":\"100\", " +
            "   \"type\":\"XDVO\"," +
            "\"factory_id\":\"1\"," +
            "\"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonUpdateRequestXDVO = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_payment_received\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"update_by_id\":\"22\",\n" +
            "    \"update_by_full_name\":\"hung\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"type\":\"NKTH\", \"product_type\":\"GAS\", " +
            "   \"in_quantity\":\"100\", " +
            "   \"type\":\"XTTH\"," +
            "\"factory_id\":\"1\"," +
            "\"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonUpdateRequestWithTypeXTTH = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_payment_received\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"update_by_id\":\"22\",\n" +
            "    \"update_by_full_name\":\"hung\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"product_type\":\"GAS\", " +
            "   \"in_quantity\":\"100\", " +
            "   \"type\":\"XTTH\"," +
            "\"factory_id\":\"1\"," +
            "\"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonUpdateRequestWithTypeXDVOGAS = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_payment_received\":\"54\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"update_by_id\":\"22\",\n" +
            "    \"update_by_full_name\":\"hung\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"product_type\":\"GAS\", " +
            "   \"in_quantity\":\"100\", " +
            "   \"type\":\"XDVO\"," +
            "\"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    @Test
    public void testUpdateFactoryExportVoucherSuccess() throws Exception{
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setSubProductId("GBONAPCAO").setFactoryId("FAC1").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "FAC1")).willReturn(productAccessoryDtoList);
        factoryExportVoucher = new FactoryExportVoucher();
        factoryExportVoucher.setFactoryId("FAC1");
        given(factoryExportVoucherRepository.findById(id)).willReturn(Optional.ofNullable(factoryExportVoucher));
        given(factoryExportVoucherRepository.save(isA(FactoryExportVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        AccountVoucherCode accountVoucherCode  = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryExportVoucher.getFactoryId(),
                VoucherEnum.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY, Instant.ofEpochSecond(95616));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));

        mockMvc.perform(patch("/v1/factory-export-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("hung"));
    }

    @Test
    public void testUpdateFactoryExportVoucherSuccessXTTH() throws Exception{
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setSubProductId("GBONAPCAO").setFactoryId("FAC1").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "FAC1")).willReturn(productAccessoryDtoList);
        factoryExportVoucher = new FactoryExportVoucher();
        factoryExportVoucher.setFactoryId("FAC1");
        given(factoryExportVoucherRepository.findById(id)).willReturn(Optional.ofNullable(factoryExportVoucher));
        given(factoryExportVoucherRepository.save(isA(FactoryExportVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        AccountVoucherCode accountVoucherCode  = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryExportVoucher.getFactoryId(),
                VoucherEnum.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY, Instant.ofEpochSecond(95616));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));

        mockMvc.perform(patch("/v1/factory-export-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequestXDVO)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("hung"));
    }

    @Test
    public void testUpdateFactoryExportVoucherSuccessWithTypeXTTH() throws Exception{
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setFactoryId("1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "1")).willReturn(productAccessoryDtoList);
        factoryExportVoucher = new FactoryExportVoucher();
        given(factoryExportVoucherRepository.findById(id)).willReturn(Optional.ofNullable(factoryExportVoucher));
        given(factoryExportVoucherRepository.save(isA(FactoryExportVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        AccountVoucherCode accountVoucherCode  = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryExportVoucher.getFactoryId(),
                VoucherEnum.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY, Instant.ofEpochSecond(95616));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));

        mockMvc.perform(patch("/v1/factory-export-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequestWithTypeXTTH)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("hung"));
    }

    @Test
    public void testUpdateFactoryExportVoucherSuccessWithTypeXDVOGAS() throws Exception{
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setFactoryId("1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1","1")).willReturn(productAccessoryDtoList);
        factoryExportVoucher = new FactoryExportVoucher();
        given(factoryExportVoucherRepository.findById(id)).willReturn(Optional.ofNullable(factoryExportVoucher));
        given(factoryExportVoucherRepository.save(isA(FactoryExportVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        AccountVoucherCode accountVoucherCode  = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryExportVoucher.getFactoryId(),
                VoucherEnum.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY, Instant.ofEpochSecond(95616));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));
        mockMvc.perform(patch("/v1/factory-export-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequestWithTypeXDVOGAS)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("hung"));
    }

    @Test
    public void testUpdateFactoryExportVoucherWithFactoryExportVoucherNotExist() throws Exception{
        given(factoryExportVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/factory-export-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu xuất hàng không tồn tại"));
    }

    @Test
    public void testCreateFactoryExportVoucherWithGoodsInOutEmpty()throws Exception{
        mockMvc.perform(post("/v1/factory-export-vouchers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequestWithoutGoodsInOut))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phải có hàng mới tạo được phiếu"));
    }

    @Test
    public void testCreateFactoryExportVoucherSuccess() throws Exception{
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setFactoryId("1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1","1")).willReturn(productAccessoryDtoList);
        given(factoryExportVoucherRepository.save(isA(FactoryExportVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        given(voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum("FAC1", VoucherEnum.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY,
        		Instant.ofEpochSecond(95616l))).willReturn(new AccountVoucherCode().setAccNo("TEST"));

        mockMvc.perform(post("/v1/factory-export-vouchers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("sold voucher"));
    }

    @Test
    public void testCreateFactoryExportVoucherSuccessWithGASNVDO() throws Exception{
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setFactoryId("FAC1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1","FAC1")).willReturn(productAccessoryDtoList);
        given(factoryExportVoucherRepository.save(isA(FactoryExportVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        given(voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum("FAC1", VoucherEnum.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY,
        		Instant.ofEpochSecond(95616l))).willReturn(new AccountVoucherCode().setAccNo("TEST"));

        mockMvc.perform(post("/v1/factory-export-vouchers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequestWithTypeGASXDVO)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("sold voucher"));
    }

    @Test
    public void testCreateFactoryExportVoucherSuccessWithVOXDVO() throws Exception{
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setFactoryId("1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("GBONAPCAO").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "1")).willReturn(productAccessoryDtoList);
        given(factoryExportVoucherRepository.save(isA(FactoryExportVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        given(voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum("FAC1", VoucherEnum.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY,
        		Instant.ofEpochSecond(95616l))).willReturn(new AccountVoucherCode().setAccNo("TEST"));

        mockMvc.perform(post("/v1/factory-export-vouchers")   .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequestWithTypeVOXDVO)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("sold voucher"));
    }

    @Test
    public void testCreateFactoryExportVoucherSuccessWithTypeXTTH() throws Exception{
        List<ProductAccessory> productAccessoryDtoList = new ArrayList<>();
        ProductAccessory dto1 = new ProductAccessory().setMainProductId("S1").setFactoryId("1").setSubProductId("GBONAPCAO").setId("GBONAPCAO").setSubProductType("S1").setSubProductQuantity(BigDecimal.valueOf(1000));
        productAccessoryDtoList.add(dto1);
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("S1", "1")).willReturn(productAccessoryDtoList);
        given(factoryExportVoucherRepository.save(isA(FactoryExportVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        given(voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum("FAC1", VoucherEnum.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY,
        		Instant.ofEpochSecond(95616l))).willReturn(new AccountVoucherCode().setAccNo("TEST"));

        mockMvc.perform(post("/v1/factory-export-vouchers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequestWithTypeXTTH)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("sold voucher"));
    }

//    @Test
//    public void testGetListFactoryExportVoucherSuccessWithEmbedGoodInOutTrue() throws Exception {
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for(int i = 0; i < 5; i++){
//            factoryExportVoucher = new FactoryExportVoucher();
//            factoryExportVoucher.setNo("no"+i);
//            factoryExportVoucher.setId("id"+i);
//            factoryExportVoucher.setVoucherAt(Instant.now());
//            factoryExportVoucher.setVoucherCode("voucher_code"+i);
//            factoryExportVoucher.setTotalGoods(BigDecimal.valueOf(12));
//            factoryExportVoucher.setTotalPaymentReceived(BigDecimal.valueOf(32));
//            factoryExportVoucher.setSalesmanId("id"+i);
//            factoryExportVoucher.setCreatedByFullName("do van hung"+i);
//            factoryExportVoucher.setTruckLicensePlateNumber("0number"+i);
//            factoryExportVoucher.setTruckDriverId("id"+i);
//            factoryExportVoucher.setCompanyId("id"+i);
//            factoryExportVoucher.setCustomerId("id"+i);
//            factoryExportVoucher.setNo("no"+i);
//            factoryExportVoucher.setNote("note"+i);
//
//            GoodsInOut goodsInOut = new GoodsInOut();
//            goodsInOut.setVoucherId("voucher"+i);
//            goodsInOut.setVoucherNo("voucher_no"+i);
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
//            object[0] = factoryExportVoucher;
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
//        given(factoryExportVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);
//
////        given(goodsInOutRepository.getByVoucherId(id)).willReturn(list);
//        mockMvc.perform(get("/v1/factory-export-vouchers")
//                .header("department_id","FAC1")
//                .params(where)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }
//
//    @Test
//    public void testGetListFactoryExportVoucherSuccessWithEmbedGoodInOutFalse() throws Exception {
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for(int i = 0; i < 5; i++){
//            factoryExportVoucher = new FactoryExportVoucher();
//            factoryExportVoucher.setNo("no"+i);
//            factoryExportVoucher.setId("id"+i);
//            factoryExportVoucher.setVoucherAt(Instant.now());
//            factoryExportVoucher.setVoucherCode("voucher_code"+i);
//            factoryExportVoucher.setTotalGoods(BigDecimal.valueOf(12));
//            factoryExportVoucher.setTotalPaymentReceived(BigDecimal.valueOf(32));
//            factoryExportVoucher.setSalesmanId("id"+i);
//            factoryExportVoucher.setCreatedByFullName("do van hung"+i);
//            factoryExportVoucher.setTruckLicensePlateNumber("0number"+i);
//            factoryExportVoucher.setTruckDriverId("id"+i);
//            factoryExportVoucher.setCompanyId("id"+i);
//            factoryExportVoucher.setCustomerId("id"+i);
//            factoryExportVoucher.setNo("no"+i);
//            factoryExportVoucher.setNote("note"+i);
//
//            GoodsInOut goodsInOut = new GoodsInOut();
//            goodsInOut.setVoucherId("voucher"+i);
//            goodsInOut.setVoucherNo("voucher_no"+i);
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
//            object[0] = factoryExportVoucher;
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
//        where1.add("factory_id","FAC1");
//
//        given(factoryExportVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);
//
//        mockMvc.perform(get("/v1/factory-export-vouchers")
//                .header("department_id","FAC1").params(where)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }

    @Test
    public void testDeleteFactoryExportVoucherWithFactoryExportVoucherNotExist() throws Exception{
        given(factoryExportVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/factory-export-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu xuất nhà máy không tồn tại"));
    }

    @Test
    public void testDeletePaymentVoucherSuccess()throws Exception{
        FactoryExportVoucher factoryExportVoucher = new FactoryExportVoucher();
        factoryExportVoucher.setId("1");
        factoryExportVoucher.setVoucherCode("PTH");
        given(factoryExportVoucherRepository.findById("1")).willReturn(Optional.ofNullable(factoryExportVoucher));
        mockMvc.perform(delete("/v1/factory-export-vouchers/{id}", "1").contentType(APPLICATION_JSON_UTF8))
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
        String page = PageableProcess.PageToSqlQuery(pageable, "factory_export_voucher");
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "factory_export_voucher");
        HashMap<String, String> where1 = new HashMap<>();
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id","FAC1");
        given(voucherMapper.getList(where1,pagingStr, page, null, null)).willReturn(list);
        given(voucherMapper.countList(where1, page, null, null)).willReturn(5l);

        mockMvc.perform(get("/v1/factory-export-vouchers").header("department_id","FAC1")
                        .params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }

}
