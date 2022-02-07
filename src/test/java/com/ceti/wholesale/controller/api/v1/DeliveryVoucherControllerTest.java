package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.dto.DeliveryVoucherDto;
import com.ceti.wholesale.dto.ProductionMonitoringDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.DeliveryVoucherMapper;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.DeliveryVoucher;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.ProductAccessory;
import com.ceti.wholesale.repository.*;
import com.ceti.wholesale.service.DeliveryVoucherService;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ProductAccessoryService;
import com.ceti.wholesale.service.impl.DeliveryVoucherServiceImpl;
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
import java.util.*;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DeliveryVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class DeliveryVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        DeliveryVoucherService deliveryVoucherService() {
            return new DeliveryVoucherServiceImpl() {
            };
        }

        @Bean
        ProductAccessoryService productAccessoryService() {
            return new ProductAccessoryServiceImpl();
        }

        @Bean
        GoodsInOutService goodsInOutService() {
            return new GoodsInOutServiceImpl();
        }


    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SoldDeliveryVoucherRepository soldDeliveryVoucherRepository;

    @MockBean
    private PaymentVoucherRepository paymentVoucherRepository;

    @MockBean
    private RecallVoucherRepository recallVoucherRepository;

    @MockBean
    private CylinderDebtRepository cylinderDebtRepository;

    @MockBean
    private DeliveryVoucherRepository deliveryVoucherRepository;

    @MockBean
    private DeliveryVoucherDetailRepository deliveryVoucherDetailRepository;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;
    @MockBean
    private VoucherUtils voucherUtils;
    @MockBean
    private GoodsInOutDetailRepository goodsInOutDetailRepository;

    @MockBean
    private VoucherMapper voucherMapper;

    @MockBean
    private ProductAccessoryRepository productAccessoryRepository;

    @MockBean
    private ReturnVoucherRepository returnVoucherRepository;

    @MockBean
    private DeliveryVoucherMapper deliveryVoucherMapper;
    
    @MockBean
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

    String id = "XH210309-001-XXE";

    String jsonCreateRequest = "{\n" +
            "    \"truck_driver_id\":\"1\",\n" +
            "    \"truck_license_plate_number\":\"1234\",\n" +
            "    \"total_goods\":\"10\",\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"note\":\"abc\",\n" +
            "    \"voucher_at\":\"1615463188\",\n" +
            "    \"created_by_full_name\":\"ductq\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\", \"type\":\"aaa\"," +
            "\"product_type\":\"GAS\"," +
            "\"factory_id\":\"1\"," +
            "   \"in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonCreateRequestWithoutGoodsInOut = "{\n" +
            "    \"truck_driver_id\":\"1\",\n" +
            "    \"truck_license_plate_number\":\"1234\",\n" +
            "    \"total_goods\":\"10\",\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"note\":\"abc\",\n" +
            "    \"voucher_at\":\"1615463188\",\n" +
            "    \"created_by_full_name\":\"ductq\",\n" +
            "    \"goods_in_out\":" +
            "[]}";

    String jsonUpdateRequest = "{\n" +
            "    \"truck_driver_id\":\"1\",\n" +
            "    \"truck_license_plate_number\":\"1234\",\n" +
            "    \"total_goods\":\"10\",\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"123456\",\n" +
            "    \"note\":\"abc\",\n" +
            "    \"update_by_full_name\":\"hungss\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\", \"type\":\"aaa\"," +
            "\"product_type\":\"GAS\"," +
            "\"factory_id\":\"1\"," +
            "   \"in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    DeliveryVoucher deliveryVoucher = new DeliveryVoucher();

    @Test
    public void testUpdateDeliveryVoucherSuccess() throws Exception {
        List<ProductAccessory> list = new ArrayList<ProductAccessory>();
        ProductAccessory productAccessory = new ProductAccessory();
        productAccessory.setFactoryId("1");
        productAccessory.setMainProductId("1");
        productAccessory.setId("1");
        productAccessory.setSubProductName("name");
        list.add(productAccessory);
        DeliveryVoucher deliveryVoucher = new DeliveryVoucher();
        deliveryVoucher.setFactoryId("1");
        deliveryVoucher.setId("1");
        given(deliveryVoucherRepository.findById("1")).willReturn(Optional.of(deliveryVoucher));
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("1", "1")).willReturn(list);
        given(deliveryVoucherRepository.save(isA(DeliveryVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(patch("/v1/delivery-vouchers/{id}", "1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("hungss"));
    }

    @Test
    public void testUpdateDeliveryVoucherWithDeliverVoucherNotExist() throws Exception {

        given(deliveryVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/delivery-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu xuất kho không tồn tại"));
    }

    @Test
    public void testCreateDeliveryVoucherWithGoodsInOutEmpty() throws Exception {
        mockMvc.perform(post("/v1/delivery-vouchers").header("department_id", "1").contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequestWithoutGoodsInOut))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phải có hàng mới tạo được phiếu"));
    }

    @Test
    public void testCreateDeliveryVoucherSuccess() throws Exception {
        List<ProductAccessory> list = new ArrayList<ProductAccessory>();
        ProductAccessory productAccessory = new ProductAccessory();
        productAccessory.setFactoryId("1");
        productAccessory.setMainProductId("1");
        productAccessory.setId("1");
        productAccessory.setSubProductName("name");
        list.add(productAccessory);
        given(deliveryVoucherRepository.save(isA(DeliveryVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("1", "1")).willReturn(list);
        given(voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum("1", VoucherEnum.VOUCHER_CODE_XUAT_XE,
        		Instant.ofEpochSecond(1615463188l))).willReturn(new AccountVoucherCode().setAccNo("TEST"));

        mockMvc.perform(post("/v1/delivery-vouchers").header("department_id", "1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("abc"));
    }

//    @Test
//    public void testGetListDeliveryVoucherSuccessWithEmbedGoodsInOutFalse() throws Exception {
//
//        List<Object[]> objects = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            DeliveryVoucher deliveryVoucher = new DeliveryVoucher();
//            deliveryVoucher.setTruckDriverId("truckDriverId" + i);
//            deliveryVoucher.setTruckLicensePlateNumber("truckLicensePlateNumber" + i);
//            deliveryVoucher.setTotalGoods(BigDecimal.valueOf(10));
//            deliveryVoucher.setNote("note" + i);
//            deliveryVoucher.setCreatedByFullName("ductq" + i);
//            deliveryVoucher.setFactoryId("1");
//
//
//            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver" + i).setAbbreviatedName("hung" + i).setId("id" + i).setTruckLicensePlateNumber("12b" + i).setAddress("bac giang" + i);
//
//            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343" + i);
//
//            Company company = new Company().setPhoneNumber("03349323" + i).setName("hung" + i).setId("id" + i).setTaxCode("taxCode" + i).setAddress("address" + i);
//
//            Object[] object = new Object[10];
//            object[0] = deliveryVoucher;
//            object[1] = truckDriver;
//            object[2] = truck;
//            object[3] = company;
//
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
//        where.add("note", "note");
//
//        MultiValueMap<String, String> where_ = new LinkedMultiValueMap<>();
//        where_.add("note", "note");
//        where_.add("factory_id", "1");
//
//        given(deliveryVoucherDetailRepository.findAllWithFilter(pageable, where_)).willReturn(result);
//
//        mockMvc.perform(get("/v1/delivery-vouchers")
//                .header("department_id", "1")
//                .params(where))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"))
//        ;
//    }

//    @Test
//    public void testGetListDeliveryVoucherSuccessWithEmbedGoodsInOutTrue() throws Exception {
//        List<Object[]> objects = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            DeliveryVoucher deliveryVoucher = new DeliveryVoucher();
//            deliveryVoucher.setTruckDriverId("truckDriverId" + i);
//            deliveryVoucher.setTruckLicensePlateNumber("truckLicensePlateNumber" + i);
//            deliveryVoucher.setTotalGoods(BigDecimal.valueOf(10));
//            deliveryVoucher.setNote("note" + i);
//            deliveryVoucher.setCreatedByFullName("ductq" + i);
//
//            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver" + i).setAbbreviatedName("hung" + i).setId("id" + i).setTruckLicensePlateNumber("12b" + i).setAddress("bac giang" + i);
//
//            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343" + i);
//
//            Company company = new Company().setPhoneNumber("03349323" + i).setName("hung" + i).setId("id" + i).setTaxCode("taxCode" + i).setAddress("address" + i);
//
//            Object[] object = new Object[10];
//            object[0] = deliveryVoucher;
//            object[1] = truckDriver;
//            object[2] = truck;
//            object[3] = company;
//
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
//        where.add("note", "note");
//        where.add("embed_goods_in_out", "true");
//
//        MultiValueMap<String, String> where_ = new LinkedMultiValueMap<>();
//        where_.add("note", "note");
//        where_.add("embed_goods_in_out", "true");
//        where_.add("factory_id", "1");
//
////        given(goodsInOutRepository.getByVoucherId(id)).willReturn(list);
//
//        given(deliveryVoucherDetailRepository.findAllWithFilter(pageable, where_)).willReturn(result);
//
//        mockMvc.perform(get("/v1/delivery-vouchers")
//                .header("department_id", "1")
//                .params(where))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }

    @Test
    public void testDeleteDeliveryVoucherWithDeliverVoucherNotExist() throws Exception {

        given(deliveryVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/delivery-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu xuất kho theo xe không tồn tại"));
    }

    @Test
    public void testDeleteDeliveryVoucher() throws Exception {

    	DeliveryVoucher deliveryVoucher = new DeliveryVoucher();
    	deliveryVoucher.setId(id);
        given(deliveryVoucherRepository.findById(id)).willReturn(Optional.ofNullable(deliveryVoucher));

        mockMvc.perform(delete("/v1/delivery-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8))
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
        String page = PageableProcess.PageToSqlQuery(pageable, "delivery_voucher");
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "delivery_voucher");
        HashMap<String, String> where1 = new HashMap<>();
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id","FAC1");
        given(voucherMapper.getList(where1,pagingStr, page, null, null)).willReturn(list);
        given(voucherMapper.countList(where1, page, null, null)).willReturn(5l);

        mockMvc.perform(get("/v1/delivery-vouchers").header("department_id","FAC1")
                        .params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }
}
