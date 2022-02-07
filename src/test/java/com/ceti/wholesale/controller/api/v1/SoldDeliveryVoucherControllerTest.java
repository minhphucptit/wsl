package com.ceti.wholesale.controller.api.v1;


import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.PaymentVoucherMapper;
import com.ceti.wholesale.mapper.SoldDeliveryVoucherMapper;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.model.*;
import com.ceti.wholesale.repository.*;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ProductAccessoryService;
import com.ceti.wholesale.service.SoldDeliveryVoucherService;
import com.ceti.wholesale.service.impl.CylinderDebtServiceImpl;
import com.ceti.wholesale.service.impl.GoodsInOutServiceImpl;
import com.ceti.wholesale.service.impl.ProductAccessoryServiceImpl;
import com.ceti.wholesale.service.impl.SoldDeliveryVoucherServiceImpl;
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
@WebMvcTest(SoldDeliveryVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class SoldDeliveryVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        SoldDeliveryVoucherService soldDeliveryVoucherService() {
            return new SoldDeliveryVoucherServiceImpl() {
            };
        }

        @Bean
        CylinderDebtService cylinderDebtService() {
            return new CylinderDebtServiceImpl();
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
    private VoucherUtils voucherUtils;
    @MockBean
    private PaymentVoucherRepository paymentVoucherRepository;

    @MockBean
    private SoldDeliveryVoucherRepository soldDeliveryVoucherRepository;

    @MockBean
    private SoldDeliveryVoucherDetailRepository soldDeliveryVoucherDetailRepository;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;

    @MockBean
    private ReturnVoucherRepository returnVoucherRepository;

    @MockBean
    private PaymentVoucherMapper paymentVoucherMapper;

    @MockBean
    private GoodsInOutDetailRepository goodsInOutDetailRepository;

    @MockBean
    private VoucherMapper voucherMapper;

    @MockBean
    private ProductAccessoryRepository productAccessoryRepository;

    @MockBean
    private CylinderDebtRepository cylinderDebtRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private SoldDeliveryVoucherMapper soldDeliveryVoucherMapper;
    
    @MockBean
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    private SoldDeliveryVoucher soldDeliveryVoucher;

    String id = "soldvoucher";

    String jsonCreateRequest = "{\n" +
            "    \"delivery_voucher_no\":\"XH210227\",\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_receivable\":\"29\",\n" +
            "    \"total_payment_received\":\"54\",\n" +
            "    \"total_goods_return\":\"2\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"delivery_voucher_id\":\"1\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\", " +
            "\"type\":\"aaa\"," +
            " \"stt\":1," +
            " \"in_quantity\":\"100\", " +
            " \"xbx_out_quantity\":\"1000\", " +
            "\"out_quantity\":\"50\"," +
            "\"product_type\":\"GAS\"," +
            "\"type\":\"XBTC\"," +
            " \"product_name\":\"romano\", " +
            "\"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonCreateRequestWithoutGoodsInOut = "{\n" +
            "    \"delivery_voucher_no\":\"XH210227\",\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_receivable\":\"29\",\n" +
            "    \"total_payment_received\":\"54\",\n" +
            "    \"total_goods_return\":\"2\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"delivery_voucher_id\":\"1\",\n" +
            "    \"goods_in_out\":" +
            "[" +
            "]}";

    String jsonUpdateRequest = "{\n" +
            "    \"delivery_voucher_no\":\"XH210227\",\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"customer_id\":\"hung001\",\n" +
            "    \"truck_driver_id\":\"nhan01\",\n" +
            "    \"truck_license_plate_number\":\"12\",\n" +
            "    \"total_goods\":\"12\",\n" +
            "    \"total_receivable\":\"29\",\n" +
            "    \"total_payment_received\":\"54\",\n" +
            "    \"total_goods_return\":\"2\",\n" +
            "    \"note\":\"sold voucher\",\n" +
            "    \"create_by_full_name\":\"kim van ha\",\n" +
            "    \"no\":\"no\",\n" +
            "    \"update_by_id\":\"22\",\n" +
            "    \"update_by_full_name\":\"hung\",\n" +
            "    \"delivery_voucher_id\":\"1\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\"," +
            " \"xbx_out_quantity\":\"1000\", " +
            " \"type\":\"aaa\"," +
            " \"stt\":1," +
            "\"product_type\":\"GAS\"," +
            "\"type\":\"XBTC\"," +
            "   \"in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";


    @Test
    public void testUpdateSoldDeliveryVoucherSuccess() throws Exception {
        List<ProductAccessory> list = new ArrayList<ProductAccessory>();
        ProductAccessory productAccessory = new ProductAccessory();
        productAccessory.setMainProductId("1");
        productAccessory.setId("1");
        productAccessory.setFactoryId("FAC1");
        productAccessory.setSubProductName("name");
        productAccessory.setSubProductType("VO");
        list.add(productAccessory);

        soldDeliveryVoucher = new SoldDeliveryVoucher();
        soldDeliveryVoucher.setDeliveryVoucherId("1");
        soldDeliveryVoucher.setFactoryId("FAC1");
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("1","FAC1")).willReturn(list);
        given(soldDeliveryVoucherRepository.findById(id)).willReturn(Optional.ofNullable(soldDeliveryVoucher));
        given(soldDeliveryVoucherRepository.save(isA(SoldDeliveryVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(soldDeliveryVoucher.getFactoryId(), VoucherEnum.getEnumFromCode(soldDeliveryVoucher.getVoucherCode()),
                Instant.ofEpochSecond(5152));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));        given(goodsInOutRepository.getMaxXBXOutQuantityOrInQuantityOneProduct("1", "1",1)).willReturn(BigDecimal.valueOf(1000000));

        mockMvc.perform(patch("/v1/sold-delivery-vouchers/{id}", id)
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("hung"));
    }

    @Test
    public void testUpdateSoldDeliveryVoucherSuccessWithXbXOutLargerThanMax() throws Exception {
        soldDeliveryVoucher = new SoldDeliveryVoucher();
        soldDeliveryVoucher.setDeliveryVoucherId("1");
        soldDeliveryVoucher.setFactoryId("FAC1");
        given(soldDeliveryVoucherRepository.findById(id)).willReturn(Optional.ofNullable(soldDeliveryVoucher));
        given(soldDeliveryVoucherRepository.save(isA(SoldDeliveryVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(soldDeliveryVoucher.getFactoryId(), VoucherEnum.getEnumFromCode(soldDeliveryVoucher.getVoucherCode()),
                Instant.ofEpochSecond(5152));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));
        given(goodsInOutRepository.getMaxXBXOutQuantityOrInQuantityOneProduct("1", "1",1)).willReturn(BigDecimal.valueOf(10));

        mockMvc.perform(patch("/v1/sold-delivery-vouchers/{id}", id)
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Số lượng xuất bán của sản phẩm romano phải nhỏ hơn hoặc bằng 10\n"));
    }

    @Test
    public void testUpdateSoldDeliveryVoucherWithSoldDeliverVoucherNotExist() throws Exception {

        given(soldDeliveryVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/sold-delivery-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu xuất bán hàng theo xe không tồn tại"));
    }

    @Test
    public void testCreateSoldVoucherWithGoodsInOutEmpty()throws Exception{
        mockMvc.perform(post("/v1/sold-delivery-vouchers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequestWithoutGoodsInOut))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phải có hàng mới tạo được phiếu"));
    }

//    @Test
//    public void testCreateSoldDeliveryVoucherSuccess() throws Exception {
//        List<ProductAccessory> list = new ArrayList<ProductAccessory>();
//        ProductAccessory productAccessory = new ProductAccessory();
//        productAccessory.setMainProductId("1");
//        productAccessory.setId("1");
//        productAccessory.setFactoryId("FAC1");
//        productAccessory.setSubProductName("name");
//        productAccessory.setSubProductType("VO");
//        list.add(productAccessory);
//        soldDeliveryVoucher = new SoldDeliveryVoucher();
//        soldDeliveryVoucher.setDeliveryVoucherId("1");
//        given(productAccessoryRepository.findByMainProductIdAndFactoryId("1","FAC1")).willReturn(list);
//        given(soldDeliveryVoucherRepository.save(isA(SoldDeliveryVoucher.class))).willAnswer(i -> i.getArgument(0));
//        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));
//        given(goodsInOutRepository.getMaxXBXOutQuantityOrInQuantityOneProduct("1", "1",1)).willReturn(BigDecimal.valueOf(100000));
//
//        mockMvc.perform(post("/v1/sold-delivery-vouchers")
//                .header("department_id","FAC1")
//                .contentType(APPLICATION_JSON_UTF8)
//                .content(jsonCreateRequest)).andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("sold voucher"));
//    }

//    @Test
//    public void testCreateSoldDeliveryVoucherWithmaxXBXOutQuantityLargerThanMaxs() throws Exception {
//        soldDeliveryVoucher = new SoldDeliveryVoucher();
//        soldDeliveryVoucher.setDeliveryVoucherId("1");
//        given(soldDeliveryVoucherRepository.save(isA(SoldDeliveryVoucher.class))).willAnswer(i -> i.getArgument(0));
//
//        given(goodsInOutRepository.getMaxXBXOutQuantityOrInQuantityOneProduct("1", "1",1)).willReturn(BigDecimal.valueOf(10));
//
//        mockMvc.perform(post("/v1/sold-delivery-vouchers")
//                .header("department_id","FAC1")
//                .contentType(APPLICATION_JSON_UTF8)
//                .content(jsonCreateRequest)).andExpect(status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Số lượng xuất bán của sản phẩm romano phải nhỏ hơn hoặc bằng 10\n"));
//    }

//    @Test
//    public void testGetListSoldDeliveryVoucherSuccessWithEmbedGoodsInOutTrue() throws Exception {
//
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            soldDeliveryVoucher = new SoldDeliveryVoucher();
//            soldDeliveryVoucher.setId("id" + i);
//            soldDeliveryVoucher.setVoucherCode("voucher_code" + i);
//            soldDeliveryVoucher.setTotalGoods(BigDecimal.valueOf(12));
//            soldDeliveryVoucher.setTotalGoodsReturn(BigDecimal.valueOf(22));
//            soldDeliveryVoucher.setTotalPaymentReceived(BigDecimal.valueOf(32));
//            soldDeliveryVoucher.setDeliveryVoucherId("man" + i);
//            soldDeliveryVoucher.setTruckLicensePlateNumber("0number" + i);
//            soldDeliveryVoucher.setTruckDriverId("truck" + i);
//            soldDeliveryVoucher.setCompanyId("company" + i);
//            soldDeliveryVoucher.setNo("no" + i);
//            soldDeliveryVoucher.setNote("note" + i);
//
//            GoodsInOut goodsInOut = new GoodsInOut();
//            goodsInOut.setVoucherId("voucher" + i);
//            goodsInOut.setVoucherNo("voucher_no" + i);
//            list.add(goodsInOut);
//
//            Company company = new Company().setPhoneNumber("03349323" + i).setName("hung" + i).setId("id" + i).setTaxCode("taxCode" + i).setAddress("address" + i);
//
//            Customer customer = new Customer().setTaxCode("taxCode" + i).setName("nhan" + i).setId("id" + i).setCategory("category" + i).setGroupId("groupId" + i).setPhoneNumber("03225433" + i).setAddress("viet nam" + i).setCompanyId("ceti" + i);
//
//            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver" + i).setAbbreviatedName("hung" + i).setId("id" + i).setTruckLicensePlateNumber("12b" + i).setAddress("bac giang" + i);
//
//            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343" + i);
//
//            Salesman salesman = new Salesman().setAbbreviatedName("hung").setFullName("do van hung").setPhoneNumber("042342345").setId("hung1").setAddress("bac giang");
//            Object[] object = new Object[10];
//            object[0] = soldDeliveryVoucher;
//            object[1] = company;
//            object[2] = customer;
//            object[3] = truckDriver;
//            object[4] = truck;
//            object[5] = salesman;
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
//        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
//        where1.add("note", "note");
//        where1.add("embed_goods_in_out", "true");
//        where1.add("factory_id", "FAC1");
//
//        given(soldDeliveryVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);
//
//        mockMvc.perform(get("/v1/sold-delivery-vouchers")
//                .header("department_id","FAC1")
//                .params(where)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }

//    @Test
//    public void testGetListSoldDeliveryVoucherSuccessWithEmbedGoodsInOutFalse() throws Exception {
//
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            soldDeliveryVoucher = new SoldDeliveryVoucher();
//            soldDeliveryVoucher.setId("id" + i);
//            soldDeliveryVoucher.setVoucherCode("voucher_code" + i);
//            soldDeliveryVoucher.setTotalGoods(BigDecimal.valueOf(22));
//            soldDeliveryVoucher.setTotalGoodsReturn(BigDecimal.valueOf(32));
//            soldDeliveryVoucher.setTotalPaymentReceived(BigDecimal.valueOf(32));
//            soldDeliveryVoucher.setDeliveryVoucherId("man" + i);
//            soldDeliveryVoucher.setTruckLicensePlateNumber("0number" + i);
//            soldDeliveryVoucher.setTruckDriverId("truck" + i);
//            soldDeliveryVoucher.setCompanyId("company" + i);
//            soldDeliveryVoucher.setNo("no" + i);
//            soldDeliveryVoucher.setNote("note" + i);
//
//            GoodsInOut goodsInOut = new GoodsInOut();
//            goodsInOut.setVoucherId("voucher" + i);
//            goodsInOut.setVoucherNo("voucher_no" + i);
//            list.add(goodsInOut);
//
//            Company company = new Company().setPhoneNumber("03349323" + i).setName("hung" + i).setId("id" + i).setTaxCode("taxCode" + i).setAddress("address" + i);
//
//            Customer customer = new Customer().setTaxCode("taxCode" + i).setName("nhan" + i).setId("id" + i).setCategory("category" + i).setGroupId("groupId" + i).setPhoneNumber("03225433" + i).setAddress("viet nam" + i).setCompanyId("ceti" + i);
//
//            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver" + i).setAbbreviatedName("hung" + i).setId("id" + i).setTruckLicensePlateNumber("12b" + i).setAddress("bac giang" + i);
//
//            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343" + i);
//
//            Object[] object = new Object[10];
//            object[0] = soldDeliveryVoucher;
//            object[1] = company;
//            object[2] = customer;
//            object[3] = truckDriver;
//            object[4] = truck;
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
//
//        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
//        where1.add("note", "note");
//        where1.add("factory_id", "FAC1");
//
//        given(soldDeliveryVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);
//
//        mockMvc.perform(get("/v1/sold-delivery-vouchers")
//                .header("department_id","FAC1")
//                .params(where)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }

    @Test
    public void testDeleteSoldDeliveryVoucherWithSoldDeliverVoucherNotExist() throws Exception {

        given(soldDeliveryVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/sold-delivery-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu xuất bán hàng theo xe không tồn tại"));
    }

//    @Test
//    public void testDeleteSoldDeliveryVoucherSuccess() throws Exception{
//        SoldDeliveryVoucher soldDeliveryVoucher = new SoldDeliveryVoucher();
//        soldDeliveryVoucher.setDeliveryVoucherId("1");
//        soldDeliveryVoucher.setId("1");
//        List<GoodsInOut> goodsInOutList = new ArrayList<>();
//        GoodsInOut goodsInOut = new GoodsInOut();
//        goodsInOut.setId("1");
//        goodsInOut.setVoucherId("1");
//        goodsInOut.setProductId("1");
//        goodsInOut.setIsMainProduct(true);
//        goodsInOutList.add(goodsInOut);
//        given(goodsInOutRepository.findByVoucherId("1")).willReturn(goodsInOutList);
//        given(soldDeliveryVoucherRepository.findById("1")).willReturn(Optional.of(soldDeliveryVoucher));
//        mockMvc.perform(delete("/v1/sold-delivery-vouchers/{id}", "1").contentType(APPLICATION_JSON_UTF8))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
//    }

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
        String page = PageableProcess.PageToSqlQuery(pageable, "sold_delivery_voucher");
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "sold_delivery_voucher");
        HashMap<String, String> where1 = new HashMap<>();
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id","FAC1");
        given(voucherMapper.getList(where1,pagingStr, page, null, null)).willReturn(list);
        given(voucherMapper.countList(where1, page, null, null)).willReturn(5l);

        mockMvc.perform(get("/v1/sold-delivery-vouchers").header("department_id","FAC1")
                        .params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }

}
