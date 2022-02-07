package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.RecallVoucherMapper;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.ProductAccessory;
import com.ceti.wholesale.model.RecallVoucher;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.GoodsInOutDetailRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.ProductAccessoryRepository;
import com.ceti.wholesale.repository.RecallVoucherDetailRepository;
import com.ceti.wholesale.repository.RecallVoucherRepository;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ProductAccessoryService;
import com.ceti.wholesale.service.RecallVoucherService;
import com.ceti.wholesale.service.impl.GoodsInOutServiceImpl;
import com.ceti.wholesale.service.impl.ProductAccessoryServiceImpl;
import com.ceti.wholesale.service.impl.RecallVoucherServiceImpl;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RecallVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RecallVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        RecallVoucherService recallVoucherService() {
            return new RecallVoucherServiceImpl() {
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

    @Value(value = "${ceti.service.zoneId}")
    private String zoneId;

    @MockBean
    private RecallVoucherRepository recallVoucherRepository;

    @MockBean
    private RecallVoucherDetailRepository recallVoucherDetailRepository;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;

    @MockBean
    private VoucherMapper voucherMapper;

    @MockBean
    private GoodsInOutDetailRepository goodsInOutDetailRepository;

    @MockBean
    private ProductAccessoryRepository productAccessoryRepository;
    @MockBean
    private VoucherUtils voucherUtils;

    @MockBean
    private RecallVoucherMapper recallVoucherMapper;
    
    @MockBean
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    String id = "XH210309-001-XXE";

    String jsonCreateRequest = "{\n" +
            "    \"truck_driver_id\":\"1\",\n" +
            "    \"truck_license_plate_number\":\"1234\",\n" +
            "    \"total_goods_return\":\"10\",\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"note\":\"abc\",\n" +
            "    \"delivery_voucher_no\":\"voucher\",\n" +
            "    \"delivery_voucher_id\":\"1\",\n" +
            "    \"voucher_at\":\"1615463188\",\n" +
            "    \"created_by_full_name\":\"ductq\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\"," +
            " \"type\":\"aaa\"," +
            " \"stt\":1," +
            "\"product_type\":\"GAS\"," +
            " \"nxe_in_quantity\":\"1000\", " +
            "   \"in_quantity\":\"1000\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonCreateRequestWithoutGoodsInOut = "{\n" +
            "    \"truck_driver_id\":\"1\",\n" +
            "    \"truck_license_plate_number\":\"1234\",\n" +
            "    \"total_goods_return\":\"10\",\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"note\":\"abc\",\n" +
            "    \"delivery_voucher_no\":\"voucher\",\n" +
            "    \"delivery_voucher_id\":\"1\",\n" +
            "    \"voucher_at\":\"1615463188\",\n" +
            "    \"created_by_full_name\":\"ductq\",\n" +
            "    \"goods_in_out\":" +
            "[]}";

    String jsonUpdateRequest = "{\n" +
            "    \"truck_driver_id\":\"1\",\n" +
            "    \"truck_license_plate_number\":\"1234\",\n" +
            "    \"total_goods_return\":\"10\",\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"1234557\",\n" +
            "    \"note\":\"abc\",\n" +
            "    \"delivery_voucher_id\":\"1\",\n" +
            "    \"update_by_full_name\":\"hung\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\"," +
            " \"type\":\"aaa\"," +
            " \"stt\":1," +
            "\"product_type\":\"GAS\"," +
            " \"xbx_out_quantity\":\"100\", " +
            "   \"nxe_in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonUpdateNoneRequest = "{\n" +
            "    \"truck_driver_id\":\"1\",\n" +
            "    \"truck_license_plate_number\":\"1234\",\n" +
            "    \"total_goods_return\":\"10\",\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"123456513223\",\n" +
            "    \"note\":\"abc\",\n" +
            "    \"delivery_voucher_id\":\"1\",\n" +
            "    \"update_by_full_name\":\"hung\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\"," +
            " \"type\":\"aaa\"," +
            " \"stt\":1," +
            "\"product_type\":\"GAS\"," +
            " \"xbx_out_quantity\":\"0\", " +
            "   \"in_quantity\":\"0\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";
    RecallVoucher recallVoucher = new RecallVoucher();

    @Test
    public void testUpdateRecallVoucherSuccess() throws Exception{
        List<ProductAccessory> list = new ArrayList();
        ProductAccessory productAccessory = new ProductAccessory();
        productAccessory.setMainProductId("1");
        productAccessory.setId("1");
        productAccessory.setFactoryId("FAC1");
        productAccessory.setSubProductName("name");
        list.add(productAccessory);

        recallVoucher.setDeliveryVoucherId("1");
        recallVoucher.setFactoryId("FAC1");
        given(recallVoucherRepository.findById(id)).willReturn(Optional.ofNullable(recallVoucher));
        given(recallVoucherRepository.save(isA(RecallVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.getMaxXBXOutQuantityOrInQuantityOneProduct("1","1",1)).willReturn(BigDecimal.valueOf(1000));
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("1", "FAC1")).willReturn(list);
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(recallVoucher.getFactoryId(),
                VoucherEnum.VOUCHER_CODE_NHAP_THU_HOI, Instant.ofEpochSecond(15254));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));

        mockMvc.perform(patch("/v1/recall-vouchers/{id}", id)
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateNoneRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("abc"));
    }

    @Test
    public void testUpdateRecallVoucherSuccessWithXbXOutLargerThanMax() throws Exception{
        recallVoucher.setDeliveryVoucherId("1");
        recallVoucher.setFactoryId("FAC1");
        given(recallVoucherRepository.findById(id)).willReturn(Optional.ofNullable(recallVoucher));
        given(recallVoucherRepository.save(isA(RecallVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.getMaxXBXOutQuantityOrInQuantityOneProduct("1","1",1)).willReturn(BigDecimal.valueOf(10));

        mockMvc.perform(patch("/v1/recall-vouchers/{id}", id)
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Số lượng nhập thu hồi của sản phẩm romano phải nhỏ hơn hoặc bằng 10\n"));
    }

    @Test
    public void testUpdateRecallVoucherWithSoldDeliverVoucherNotExist() throws Exception{

        given(recallVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/recall-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu nhập kho không tồn tại"));
    }

    @Test
    public void testCreateRecallVoucherWithGoodsInOutEmpty()throws Exception{
        mockMvc.perform(post("/v1/recall-vouchers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequestWithoutGoodsInOut))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phải có hàng mới tạo được phiếu"));
    }

//    @Test
//    public void testCreateRecallVoucherSuccess() throws Exception{
//        recallVoucher.setDeliveryVoucherId("1");
//        recallVoucher.setFactoryId("FAC1");
//        List<ProductAccessory> list = new ArrayList();
//        ProductAccessory productAccessory = new ProductAccessory();
//        productAccessory.setMainProductId("1");
//        productAccessory.setId("FAC1");
//        productAccessory.setFactoryId("1");
//        productAccessory.setSubProductName("name");
//        list.add(productAccessory);
//
//        given(productAccessoryRepository.findByMainProductIdAndFactoryId("1", "FAC1")).willReturn(list);
//        given(recallVoucherRepository.save(isA(RecallVoucher.class))).willAnswer(i-> i.getArgument(0));
//        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
//        given(goodsInOutRepository.getMaxXBXOutQuantityOrInQuantityOneProduct("1","1",1)).willReturn(BigDecimal.valueOf(10000));
//
//        mockMvc.perform(post("/v1/recall-vouchers") .header("department_id","FAC1")
//                .contentType(APPLICATION_JSON_UTF8)
//                .content(jsonCreateRequest)).andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("abc"));
//    }

//    @Test
//    public void testCreateRecallVoucherSuccessWithRecallVoucherNoLargerThan1000() throws Exception{
//        DateTimeFormatter formatterNo = DateTimeFormatter.ofPattern("yyMMdd");
//        LocalDateTime today = LocalDateTime.ofInstant(Instant.now(), ZoneId.of(zoneId));
//        String stringToday = today.format(formatterNo);
//
//        given(recallVoucherRepository.save(isA(RecallVoucher.class))).willAnswer(i-> i.getArgument(0));
//        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));
//        given(goodsInOutRepository.getMaxXBXOutQuantityOrInQuantityOneProduct("1","1",1)).willReturn(BigDecimal.valueOf(10000));
//        given(recallVoucherRepository.countRecallVoucherInDay(stringToday)).willReturn(2000);
//
//        mockMvc.perform(post("/v1/recall-vouchers") .header("department_id","FAC1")
//                .contentType(APPLICATION_JSON_UTF8)
//                .content(jsonCreateRequest)).andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("abc"));
//    }

//    @Test
//    public void testCreateRecallVoucherWithNXEInQuantityLargerThanMax() throws Exception{
//        recallVoucher.setDeliveryVoucherId("1");
//        given(recallVoucherRepository.save(isA(RecallVoucher.class))).willAnswer(i-> i.getArgument(0));
//
//        given(goodsInOutRepository.getMaxXBXOutQuantityOrInQuantityOneProduct("1","1",1)).willReturn(BigDecimal.valueOf(10));
//
//        mockMvc.perform(post("/v1/recall-vouchers") .header("department_id","FAC1")
//                .contentType(APPLICATION_JSON_UTF8)
//                .content(jsonCreateRequest)).andExpect(status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Số lượng nhập thu hồi của sản phẩm romano phải nhỏ hơn hoặc bằng 10\n"));
//    }

//    @Test
//    public void testGetListRecallVoucherSuccessWithEmbedGoodsInOutTrue() throws Exception {
//
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for(int i = 0; i < 5; i++){
//            recallVoucher = new RecallVoucher();
//            recallVoucher.setId("id"+i);
//            recallVoucher.setVoucherCode("voucher_code"+i);
//            recallVoucher.setTotalGoodsReturn(BigDecimal.valueOf(12));
//            recallVoucher.setTotalGoodsReturn(BigDecimal.valueOf(22));
//            recallVoucher.setDeliveryVoucherId("man"+i);
//            recallVoucher.setTruckLicensePlateNumber("0number"+i);
//            recallVoucher.setTruckDriverId("truck"+i);
//            recallVoucher.setCompanyId("company"+i);
//            recallVoucher.setNo("no"+i);
//            recallVoucher.setNote("note"+i);
//
//            GoodsInOut goodsInOut = new GoodsInOut();
//            goodsInOut.setVoucherId("voucher"+i);
//            goodsInOut.setVoucherNo("voucher_no"+i);
//            list.add(goodsInOut);
//
//            Company company = new Company().setPhoneNumber("03349323"+i).setName("hung"+i).setId("id"+i).setTaxCode("taxCode"+i).setAddress("address"+i);
//
//            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver"+i).setAbbreviatedName("hung" +i).setId("id"+i).setTruckLicensePlateNumber("12b"+i).setAddress("bac giang"+i);
//
//            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343"+i);
//
//            Object[] object = new Object[10];
//            object[0] = recallVoucher;
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
//        where.add("note","note");
//        where.add("embed_goods_in_out", "true");
//
//        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
//        where1.add("note","note");
//        where1.add("embed_goods_in_out", "true");
//        where1.add("factory_id", "FAC1");
//
//
////        given(goodsInOutRepository.getByVoucherId(id)).willReturn(list);
//
//        given(recallVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);
//
//        mockMvc.perform(get("/v1/recall-vouchers") .header("department_id","FAC1")
//                .params(where)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }

//    @Test
//    public void testGetListRecallVoucherSuccessWithEmbedGoodsInOutFalse() throws Exception {
//
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for(int i = 0; i < 5; i++){
//            recallVoucher = new RecallVoucher();
//            recallVoucher.setId("id"+i);
//            recallVoucher.setVoucherCode("voucher_code"+i);
//            recallVoucher.setTotalGoodsReturn(BigDecimal.valueOf(12));
//            recallVoucher.setTotalGoodsReturn(BigDecimal.valueOf(22));
//            recallVoucher.setDeliveryVoucherId("man"+i);
//            recallVoucher.setTruckLicensePlateNumber("0number"+i);
//            recallVoucher.setTruckDriverId("truck"+i);
//            recallVoucher.setCompanyId("company"+i);
//            recallVoucher.setNo("no"+i);
//            recallVoucher.setNote("note"+i);
//
//            GoodsInOut goodsInOut = new GoodsInOut();
//            goodsInOut.setVoucherId("voucher"+i);
//            goodsInOut.setVoucherNo("voucher_no"+i);
//            list.add(goodsInOut);
//
//            Company company = new Company().setPhoneNumber("03349323"+i).setName("hung"+i).setId("id"+i).setTaxCode("taxCode"+i).setAddress("address"+i);
//
//            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver"+i).setAbbreviatedName("hung" +i).setId("id"+i).setTruckLicensePlateNumber("12b"+i).setAddress("bac giang"+i);
//
//            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343"+i);
//
//            Object[] object = new Object[10];
//            object[0] = recallVoucher;
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
//        where.add("note","note");
//
////        given(goodsInOutRepository.get(id)).willReturn(list);
//
//        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
//        where1.add("note","note");
//        where1.add("factory_id","FAC1");
//
//
//        given(recallVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);
//
//        mockMvc.perform(get("/v1/recall-vouchers")
//                .header("department_id","FAC1")
//                .params(where)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }

    @Test
    public void testDeleteRecallVoucherWithSoldDeliverVoucherNotExist() throws Exception{

        given(recallVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/recall-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu nhập kho không tồn tại"));
    }

        @Test
    public void testDeletRecallVoucherSuccess() throws Exception{
        RecallVoucher recallVoucher = new RecallVoucher();
        recallVoucher.setId("1");
        List<GoodsInOut> goodsInOuts = new ArrayList<>();
        GoodsInOut goodsInOut = new GoodsInOut();
            goodsInOut.setId("1");
            goodsInOut.setVoucherId("1");
            goodsInOuts.add(goodsInOut);
        given(recallVoucherRepository.findById("1")).willReturn(Optional.of(recallVoucher));
        given(goodsInOutRepository.findByVoucherId("1")).willReturn(goodsInOuts);

        mockMvc.perform(delete("/v1/recall-vouchers/{id}", "1").contentType(APPLICATION_JSON_UTF8))
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
        String page = PageableProcess.PageToSqlQuery(pageable, "recall_voucher");
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "recall_voucher");
        HashMap<String, String> where1 = new HashMap<>();
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id","FAC1");
        given(voucherMapper.getList(where1,pagingStr, page, null, null)).willReturn(list);
        given(voucherMapper.countList(where1, page, null, null)).willReturn(5l);

        mockMvc.perform(get("/v1/recall-vouchers").header("department_id","FAC1")
                        .params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }
}
