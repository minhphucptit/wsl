package com.ceti.wholesale.controller.api.v1;


import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.SoldVoucherMapper;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.ProductAccessory;
import com.ceti.wholesale.model.SoldVoucher;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.GoodsInOutDetailRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.PaymentVoucherRepository;
import com.ceti.wholesale.repository.ProductAccessoryRepository;
import com.ceti.wholesale.repository.ProductRepository;
import com.ceti.wholesale.repository.SoldVoucherDetailRepository;
import com.ceti.wholesale.repository.SoldVoucherRepository;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ProductAccessoryService;
import com.ceti.wholesale.service.SoldVoucherService;
import com.ceti.wholesale.service.impl.CylinderDebtServiceImpl;
import com.ceti.wholesale.service.impl.GoodsInOutServiceImpl;
import com.ceti.wholesale.service.impl.ProductAccessoryServiceImpl;
import com.ceti.wholesale.service.impl.SoldVoucherServiceImpl;
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
@WebMvcTest(SoldVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class SoldVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        SoldVoucherService soldVoucherService() {
            return new SoldVoucherServiceImpl() {
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
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SoldVoucherRepository soldVoucherRepository;

    @MockBean
    private PaymentVoucherRepository paymentVoucherRepository;

    @MockBean
    private SoldVoucherDetailRepository soldVoucherDetailRepository;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;

    @MockBean
    private CylinderDebtRepository cylinderDebtRepository;

    @MockBean
    private GoodsInOutDetailRepository goodsInOutDetailRepository;

    @MockBean
    private VoucherMapper voucherMapper;

    @MockBean
    private ProductAccessoryRepository productAccessoryRepository;

    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private VoucherUtils voucherUtils;

    @MockBean
    private SoldVoucherMapper soldVoucherMapper;
    
    @MockBean
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

    private SoldVoucher soldVoucher;

    String id = "soldvoucher";

    String jsonCreateRequest = "{\n" +
            "    \"no\":\"no\",\n" +
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
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\", \"type\":\"aaa\"," +
            "   \"in_quantity\":\"100\", " +
            "\"out_quantity\":\"50\"," +
            "\"type\":\"XBTC\"," +
            "\"product_type\":\"GAS\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonCreateRequestWithoutGoodsInout = "{\n" +
            "    \"no\":\"no\",\n" +
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
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "["+
                "]}";

    String jsonUpdateRequest = "{\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"no\":\"no\",\n" +
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
            "    \"note\":\"sold voucher\",\n" +
            "    \"update_by_id\":\"22\",\n" +
            "    \"update_by_full_name\":\"hung\",\n" +
            "    \"salesman_id\":\"12\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\", \"type\":\"aaa\"," +
            "   \"in_quantity\":\"100\", " +
            "\"out_quantity\":\"50\"," +
            "\"type\":\"XBTC\"," +
            "\"product_type\":\"GAS\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    @Test
    public void testUpdateSoldVoucherSuccess() throws Exception{
        soldVoucher = new SoldVoucher();
        soldVoucher.setNo("no");
        soldVoucher.setVoucherCode("code");
        soldVoucher.setId("id");
        soldVoucher.setFactoryId("1");
        List<ProductAccessory> listA = new ArrayList<>();
        ProductAccessory productAccessory = new ProductAccessory().setFactoryId("1").setMainProductId("1").setSubProductId("1").setSubProductName("romano").setId("1").setSubProductQuantity(BigDecimal.valueOf(1000)).setSubProductType("VO");
        listA.add(productAccessory);
        given(soldVoucherRepository.findById(id)).willReturn(Optional.ofNullable(soldVoucher));
        given(soldVoucherRepository.save(isA(SoldVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("1","1")).willReturn(listA);
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(soldVoucher.getFactoryId(), VoucherEnum.getEnumFromCode(soldVoucher.getVoucherCode()),
                Instant.ofEpochSecond(5152));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));
        mockMvc.perform(patch("/v1/sold-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.update_by_full_name").value("hung"));
    }

    @Test
    public void testUpdateSoldVoucherWithDeliverVoucherNotExist() throws Exception{

        given(soldVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/sold-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu xuất bán không tồn tại"));
    }

//    @Test
//    public void testCreateSoldVoucherSuccess() throws Exception{
//        List<ProductAccessory> list = new ArrayList<ProductAccessory>();
//        ProductAccessory productAccessory = new ProductAccessory();
//        productAccessory.setMainProductId("1");
//        productAccessory.setId("1");
//        productAccessory.setFactoryId("FAC1");
//        productAccessory.setSubProductName("name");
//        productAccessory.setSubProductType("VO");
//        list.add(productAccessory);
//
//        given(productAccessoryRepository.findByMainProductIdAndFactoryId("1","FAC1")).willReturn(list);
//        given(soldVoucherRepository.save(isA(SoldVoucher.class))).willAnswer(i-> i.getArgument(0));
//        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
//
//        mockMvc.perform(post("/v1/sold-vouchers")
//                .header("department_id","FAC1")
//                .contentType(APPLICATION_JSON_UTF8)
//                .content(jsonCreateRequest)).andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("sold voucher"));
//    }

    @Test
    public void testCreateSoldVoucherWithGoodsInOutEmpty()throws Exception{
        mockMvc.perform(post("/v1/sold-vouchers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequestWithoutGoodsInout))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phải có hàng mới tạo được phiếu"));
    }

//    @Test
//    public void testGetListSoldVoucherSuccessWithEmbedGoodInOutTrue() throws Exception {
//
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for(int i = 0; i < 5; i++){
//            soldVoucher = new SoldVoucher();
//            soldVoucher.setNo("no"+i);
//            soldVoucher.setId("id"+i);
//            soldVoucher.setVoucherAt(Instant.now());
//            soldVoucher.setVoucherCode("voucher_code"+i);
//            soldVoucher.setTotalGoods(BigDecimal.valueOf(12));
//            soldVoucher.setTotalReceivable(BigDecimal.valueOf(2));
//            soldVoucher.setTotalGoodsReturn(BigDecimal.valueOf(22));
//            soldVoucher.setTotalPaymentReceived(BigDecimal.valueOf(32));
//            soldVoucher.setSalesmanId("id"+i);
//            soldVoucher.setCreatedByFullName("do van hung"+i);
//            soldVoucher.setTruckLicensePlateNumber("0number"+i);
//            soldVoucher.setTruckDriverId("id"+i);
//            soldVoucher.setCompanyId("id"+i);
//            soldVoucher.setCustomerId("id"+i);
//            soldVoucher.setNo("no"+i);
//            soldVoucher.setNote("note"+i);
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
//            object[0] = soldVoucher;
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
//        given(soldVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);
//
//        mockMvc.perform(get("/v1/sold-vouchers").header("department_id","FAC1").params(where)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }
//
//    @Test
//    public void testGetListSoldVoucherSuccessWithEmbedGoodInOutFalse() throws Exception {
//
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for(int i = 0; i < 5; i++){
//            soldVoucher = new SoldVoucher();
//            soldVoucher.setNo("no"+i);
//            soldVoucher.setId("id"+i);
//            soldVoucher.setVoucherAt(Instant.now());
//            soldVoucher.setVoucherCode("voucher_code"+i);
//            soldVoucher.setTotalGoods(BigDecimal.valueOf(12));
//            soldVoucher.setTotalReceivable(BigDecimal.valueOf(2));
//            soldVoucher.setTotalGoodsReturn(BigDecimal.valueOf(2));
//            soldVoucher.setTotalPaymentReceived(BigDecimal.valueOf(32));
//            soldVoucher.setSalesmanId("id"+i);
//            soldVoucher.setCreatedByFullName("do van hung"+i);
//            soldVoucher.setTruckLicensePlateNumber("0number"+i);
//            soldVoucher.setTruckDriverId("id"+i);
//            soldVoucher.setCompanyId("id"+i);
//            soldVoucher.setCustomerId("id"+i);
//            soldVoucher.setNo("no"+i);
//            soldVoucher.setNote("note"+i);
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
//            object[0] = soldVoucher;
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
//        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
//        where1.add("note","note");
//        where1.add("factory_id","FAC1");
//
//
//        given(soldVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);
//
//        mockMvc.perform(get("/v1/sold-vouchers").header("department_id","FAC1").params(where)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }

    @Test
    public void testDeleteSoldVoucherWithDeliverVoucherNotExist() throws Exception{

        given(soldVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/sold-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu xuất bán hàng không tồn tại"));
    }

    @Test
    public void testDeleteSoldVoucherSuccess() throws Exception{
        SoldVoucher soldVoucher = new SoldVoucher();
        soldVoucher.setId("1");
        given(soldVoucherRepository.findById("1")).willReturn(Optional.ofNullable(soldVoucher));

        mockMvc.perform(delete("/v1/sold-vouchers/{id}", "1").contentType(APPLICATION_JSON_UTF8))
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
        String page = PageableProcess.PageToSqlQuery(pageable, "sold_voucher");
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "sold_voucher");
        HashMap<String, String> where1 = new HashMap<>();
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id","FAC1");
        given(voucherMapper.getList(where1,pagingStr, page, null, null)).willReturn(list);
        given(voucherMapper.countList(where1, page, null, null)).willReturn(5l);

        mockMvc.perform(get("/v1/sold-vouchers").header("department_id","FAC1")
                        .params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }
}
