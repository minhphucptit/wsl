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

import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateGoodsInOutRequest;
import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.dto.ReturnVoucherTotalGoodsDto;
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.PaymentVoucherMapper;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.PaymentVoucher;
import com.ceti.wholesale.model.ReturnVoucher;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CustomerRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.GoodsInOutDetailRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.PaymentVoucherDetailRepository;
import com.ceti.wholesale.repository.PaymentVoucherRepository;
import com.ceti.wholesale.repository.ReturnVoucherRepository;
import com.ceti.wholesale.repository.SoldDeliveryVoucherRepository;
import com.ceti.wholesale.repository.SoldVoucherRepository;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.PaymentVoucherService;
import com.ceti.wholesale.service.impl.CylinderDebtServiceImpl;
import com.ceti.wholesale.service.impl.GoodsInOutServiceImpl;
import com.ceti.wholesale.service.impl.PaymentVoucherServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(PaymentVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class PaymentVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        PaymentVoucherService paymentVoucherService() {
            return new PaymentVoucherServiceImpl() {
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
    }

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;

    @MockBean
    private PaymentVoucherRepository paymentVoucherRepository;

    @MockBean
    private PaymentVoucherDetailRepository paymentVoucherDetailRepository;

    @MockBean
    private SoldVoucherRepository soldVoucherRepository;

    @MockBean
    private VoucherMapper voucherMapper;
    @MockBean
    private VoucherUtils voucherUtils;

    @MockBean
    private SoldDeliveryVoucherRepository soldDeliveryVoucherRepository;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;

    @MockBean
    private GoodsInOutDetailRepository goodsInOutDetailRepository;

    @MockBean
    private CylinderDebtRepository cylinderDebtRepository;

    @MockBean
    private PaymentVoucherMapper paymentVoucherMapper;
    
    @MockBean
    private ReturnVoucherRepository returnVoucherRepository;
    
    @MockBean
    private CustomerRepository customerRepository;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

    private PaymentVoucher paymentVoucher;
    
    @MockBean
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    String id = "paymentvoucher";

    String jsonCreateRequest = "{\n" +
            "    \"voucher_id\":\"1\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"voucher_code\":\"PTH\",\n" +
            "    \"sold_voucher_no\":\"1\",\n" +
            "    \"sold_delivery_voucher_no\":\"1\",\n" +
            "    \"customer_id\":\"1\",\n" +
            "    \"total_receivable\":\"12\",\n" +
            "    \"total_goods_return\":\"12\",\n" +
            "    \"note\":\"note\",\n" +
            "    \"created_by_full_name\":\"do van hung\",\n" +
            "    \"payer\":\"hung\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\", \"type\":\"aaa\"," +
            "\"product_type\":\"GAS\"," +
            "    \"weight\":\"100\",\n" +
            "   \"in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonCreateRequestWithoutGoodsInOut = "{\n" +
            "    \"voucher_id\":\"1\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"voucher_code\":\"PTH\",\n" +
            "    \"sold_voucher_no\":\"1\",\n" +
            "    \"sold_delivery_voucher_no\":\"1\",\n" +
            "    \"customer_id\":\"1\",\n" +
            "    \"total_receivable\":\"12\",\n" +
            "    \"total_goods_return\":\"12\",\n" +
            "    \"note\":\"note\",\n" +
            "    \"created_by_full_name\":\"do van hung\",\n" +
            "    \"payer\":\"hung\",\n" +
            "    \"goods_in_out\":" +
            "[" +
            "]}";

    String jsonCreatePTXRequest = "{\n" +
            "    \"voucher_id\":\"1\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"voucher_code\":\"PTX\",\n" +
            "    \"customer_id\":\"1\",\n" +
            "    \"sold_voucher_no\":\"1\",\n" +
            "    \"sold_delivery_voucher_no\":\"1\",\n" +
            "    \"total_receivable\":\"12\",\n" +
            "    \"total_goods_return\":\"12\",\n" +
            "    \"note\":\"note\",\n" +
            "    \"created_by_full_name\":\"do van hung\",\n" +
            "    \"payer\":\"hung\",\n" +
            "    \"delivery_voucher_no\":\"A\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\", \"type\":\"aaa\"," +
            "\"product_type\":\"GAS\"," +
            "    \"weight\":\"100\",\n" +
            "   \"in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonUpdateRequest = "{\n" +
            "    \"voucher_id\":\"1\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"voucher_code\":\"PTH\",\n" +
            "    \"customer_id\":\"1\",\n" +
            "    \"sold_voucher_no\":\"1\",\n" +
            "    \"sold_delivery_voucher_no\":\"1\",\n" +
            "    \"total_receivable\":\"12\",\n" +
            "    \"total_goods_return\":\"12\",\n" +
            "    \"note\":\"note\",\n" +
            "    \"updated_by_full_name\":\"do van hung\",\n" +
            "    \"payer\":\"hung\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\", \"type\":\"aaa\"," +
            "\"product_type\":\"GAS\"," +
            "    \"weight\":\"100\",\n" +
            "   \"in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    @Test
    public void testUpdatePaymentVoucherSuccessWithVoucherCodePTH() throws Exception {
        paymentVoucher = new PaymentVoucher();
        paymentVoucher.setVoucherCode("PTH");
        given(paymentVoucherRepository.findById(id)).willReturn(Optional.ofNullable(paymentVoucher));
        given(paymentVoucherRepository.save(isA(PaymentVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(paymentVoucher.getFactoryId(), VoucherEnum.getEnumFromCode(paymentVoucher.getVoucherCode()),
                Instant.ofEpochSecond(5152));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));

        mockMvc.perform(patch("/v1/payment-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.voucher_id").value("1"));
    }

    @Test
    public void testUpdatePaymentVoucherSuccessWithVoucherCodePTX() throws Exception {
        paymentVoucher = new PaymentVoucher();
        paymentVoucher.setVoucherCode("PTX");    
        paymentVoucher.setDeliveryVoucherNo("A");
        
        ReturnVoucher returnVoucher = new ReturnVoucher();
        returnVoucher.setDeliveryVoucherNo(paymentVoucher.getDeliveryVoucherNo());
        
        List<List> data = new ArrayList<List>();
                
        List<CreateGoodsInOutRequest> request = new ArrayList<>();
        List<ReturnVoucherTotalGoodsDto> returnVouchers = new ArrayList<>();
        returnVouchers.add(new ReturnVoucherTotalGoodsDto().setTotalGoods(BigDecimal.ZERO));
        
        data.add(request);
        data.add(returnVouchers);
        given(paymentVoucherRepository.findById(id)).willReturn(Optional.ofNullable(paymentVoucher));
        given(paymentVoucherRepository.save(isA(PaymentVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));
        given(returnVoucherRepository.findByDeliveryVoucherNo(paymentVoucher.getDeliveryVoucherNo())).willReturn(returnVoucher);
        given(paymentVoucherMapper.getListReturnProducts(paymentVoucher.getDeliveryVoucherNo(), paymentVoucher.getFactoryId()))
        .willReturn(data);
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(paymentVoucher.getFactoryId(), VoucherEnum.getEnumFromCode(paymentVoucher.getVoucherCode()),
                Instant.ofEpochSecond(5152));
        given(accountVoucherCodeRepository.findById(id)).willReturn(Optional.ofNullable(accountVoucherCode));
        mockMvc.perform(patch("/v1/payment-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.voucher_id").value("1"));
    }

    @Test
    public void testUpdatePaymentVoucherWithPaymentVoucherNotExist() throws Exception {

        given(paymentVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/payment-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu thanh toán không tồn tại"));
    }
//
//    @Test
//    public void testCreatePaymentVoucherSuccessWithVoucherCodePTH() throws Exception {
//        CreateGoodsInOutRequest request = new CreateGoodsInOutRequest().setOutQuantity(BigDecimal.valueOf(1000)).setInQuantity(BigDecimal.valueOf(1000)).setNxeInQuantity(BigDecimal.valueOf(10)).setXbxOutQuantity(BigDecimal.valueOf(10)).setPrice(BigDecimal.valueOf(1000)).setProductId("1").setProductName("romano").setProductType("type").setUnit("don vi").setType("txt");
//        GoodsInOutDto goodsInOutDto = new GoodsInOutDto().setOutQuantity(request.getOutQuantity()).setXbxOutQuantity(request.getXbxOutQuantity()).setInQuantity(request.getInQuantity()).setNxeInQuantity(request.getNxeInQuantity()).setId(request.getProductId()).setPrice(request.getPrice()).setProductId(request.getProductId()).setProductName(request.getProductName()).setProductType(request.getProductType()).setVoucherCode("PTH").setVoucherId("1").setVoucherNo("1");
//
//        given(paymentVoucherRepository.save(isA(PaymentVoucher.class))).willAnswer(i -> i.getArgument(0));
//        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));
//        mockMvc.perform(post("/v1/payment-vouchers")
//                .header("department_id","FAC1")
//                .contentType(APPLICATION_JSON_UTF8)
//                .content(jsonCreateRequest)).andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("note"));
//    }

//    @Test
//    public void testCreatePaymentVoucherWithGoodsInOutEmpty()throws Exception{
//        mockMvc.perform(post("/v1/payment-vouchers")
//                .header("department_id","FAC1")
//                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequestWithoutGoodsInOut))
//                .andExpect(status().is4xxClientError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phải có hàng mới tạo được phiếu"));
//    }

//    @Test
//    public void testCreatePaymentVoucherSuccessWithVoucherCodePTX() throws Exception {
//        paymentVoucher = new PaymentVoucher();
//        paymentVoucher.setVoucherCode("PTX");
//        paymentVoucher.setDeliveryVoucherNo("A");
//        paymentVoucher.setFactoryId("FAC1");
//
//        ReturnVoucher returnVoucher = new ReturnVoucher();
//        returnVoucher.setDeliveryVoucherNo(paymentVoucher.getDeliveryVoucherNo());
//
//        List<List> data = new ArrayList<List>();
//
//        List<CreateGoodsInOutRequest> request = new ArrayList<>();
//        List<ReturnVoucherTotalGoodsDto> returnVouchers = new ArrayList<>();
//        returnVouchers.add(new ReturnVoucherTotalGoodsDto().setTotalGoods(BigDecimal.ZERO));
//
//        data.add(request);
//        data.add(returnVouchers);
//        given(paymentVoucherRepository.save(isA(PaymentVoucher.class))).willAnswer(i -> i.getArgument(0));
//        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));
//        given(returnVoucherRepository.findByDeliveryVoucherNo(paymentVoucher.getDeliveryVoucherNo())).willReturn(returnVoucher);
//        given(paymentVoucherMapper.getListReturnProducts(paymentVoucher.getDeliveryVoucherNo(), paymentVoucher.getFactoryId()))
//        .willReturn(data);
//
//        mockMvc.perform(post("/v1/payment-vouchers")
//                .header("department_id","FAC1")
//                .contentType(APPLICATION_JSON_UTF8)
//                .content(jsonCreatePTXRequest)).andExpect(status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("note"));
//    }

//    @Test
//    public void testGetListPaymentVoucherSuccessWithEmbedGoodInOutTrue() throws Exception {
//
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            paymentVoucher = new PaymentVoucher();
//            paymentVoucher.setNo("no" + i);
//            paymentVoucher.setId("id" + i);
//            paymentVoucher.setVoucherAt(Instant.now());
//            paymentVoucher.setVoucherCode("voucher_code" + i);
//            paymentVoucher.setTotalPaymentReceived(BigDecimal.valueOf(2));
//            paymentVoucher.setTotalGoodsReturn(BigDecimal.valueOf(22));
//            paymentVoucher.setCreatedByFullName("do van hung" + i);
//            paymentVoucher.setCustomerId("id" + i);
//            paymentVoucher.setNo("no" + i);
//            paymentVoucher.setNote("note" + i);
//
//            GoodsInOut goodsInOut = new GoodsInOut();
//            goodsInOut.setVoucherId("voucher" + i);
//            goodsInOut.setVoucherNo("voucher_no" + i);
//            list.add(goodsInOut);
//
//            Customer customer = new Customer().setTaxCode("taxCode" + i).setName("nhan" + i).setId("id" + i).setCategory("category" + i).setGroupId("groupId" + i).setPhoneNumber("03225433" + i).setAddress("viet nam" + i).setCompanyId("ceti" + i);
//
//            Object[] object = new Object[10];
//            object[0] = paymentVoucher;
//            object[1] = customer;
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
//        given(paymentVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);
//
//        boolean embed_goods_in_out = true;
//        mockMvc.perform(get("/v1/payment-vouchers")
//                .header("department_id","FAC1")
//                .params(where)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }
//
//    @Test
//    public void testGetListPaymentVoucherSuccessWithEmbedGoodInOutFalse() throws Exception {
//
//        List<Object[]> objects = new ArrayList<>();
//        List<GoodsInOut> list = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            paymentVoucher = new PaymentVoucher();
//            paymentVoucher.setNo("no" + i);
//            paymentVoucher.setId("id" + i);
//            paymentVoucher.setVoucherAt(Instant.now());
//            paymentVoucher.setVoucherCode("voucher_code" + i);
//            paymentVoucher.setTotalPaymentReceived(BigDecimal.valueOf(2));
//            paymentVoucher.setTotalGoodsReturn(BigDecimal.valueOf(22));
//            paymentVoucher.setCreatedByFullName("do van hung" + i);
//            paymentVoucher.setCustomerId("id" + i);
//            paymentVoucher.setNo("no" + i);
//            paymentVoucher.setNote("note" + i);
//
//            GoodsInOut goodsInOut = new GoodsInOut();
//            goodsInOut.setVoucherId("voucher" + i);
//            goodsInOut.setVoucherNo("voucher_no" + i);
//            list.add(goodsInOut);
//
//            Customer customer = new Customer().setTaxCode("taxCode" + i).setName("nhan" + i).setId("id" + i).setCategory("category" + i).setGroupId("groupId" + i).setPhoneNumber("03225433" + i).setAddress("viet nam" + i).setCompanyId("ceti" + i);
//
//            Object[] object = new Object[10];
//            object[0] = paymentVoucher;
//            object[1] = customer;
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
//        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
//        where1.add("note", "note");
//        where1.add("factory_id", "FAC1");
//
//        given(paymentVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);
//
//        mockMvc.perform(get("/v1/payment-vouchers")
//                .header("department_id","FAC1")
//                .params(where)).andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("R_200"))
//                .andExpect(jsonPath("$.total_items").value(5))
//                .andExpect(jsonPath("$.items[0].note").value("note0"));
//    }

    @Test
    public void testDeletePaymentVoucherWithPaymentVoucherNotExist() throws Exception {

        given(paymentVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/payment-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu thanh toán không tồn tại"));
    }

    @Test
    public void testGetPaymentVoucherWithPaymentVoucherNotExist() throws Exception {

        given(paymentVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(get("/v1/payment-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu thanh toán không tồn tại"));
    }

    @Test
    public void testCreatePaymentVoucherWithFalse()throws Exception{
        given(paymentVoucherRepository.existsByVoucherId("1")).willReturn(true);
        mockMvc.perform(post("/v1/payment-vouchers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequestWithoutGoodsInOut))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu xuất bán hàng này đã tạo phiếu thanh toán"));
    }

    @Test
    public void testDeletePaymentVoucherSuccess()throws Exception{
        PaymentVoucher paymentVoucher = new PaymentVoucher();
        paymentVoucher.setId("1");
        paymentVoucher.setVoucherCode("PTH");
        given(paymentVoucherRepository.findById("1")).willReturn(Optional.of(paymentVoucher));
        mockMvc.perform(delete("/v1/payment-vouchers/{id}", "1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }

    @Test
    public void testDeletePaymentVoucherSuccessPTX()throws Exception{
        PaymentVoucher paymentVoucher = new PaymentVoucher();
        paymentVoucher.setId("1");
        paymentVoucher.setVoucherCode("PTX");
        paymentVoucher.setDeliveryVoucherNo("A");
        
        ReturnVoucher returnVoucher = new ReturnVoucher();
        returnVoucher.setDeliveryVoucherNo(paymentVoucher.getDeliveryVoucherNo());
        
        List<List> data = new ArrayList<List>();
                
        List<CreateGoodsInOutRequest> request = new ArrayList<>();
        List<ReturnVoucherTotalGoodsDto> returnVouchers = new ArrayList<>();
        returnVouchers.add(new ReturnVoucherTotalGoodsDto().setTotalGoods(BigDecimal.ZERO));
        
        data.add(request);
        data.add(returnVouchers);
        
        given(paymentVoucherRepository.findById("1")).willReturn(Optional.of(paymentVoucher));
        given(returnVoucherRepository.findByDeliveryVoucherNo(paymentVoucher.getDeliveryVoucherNo())).willReturn(returnVoucher);
        given(paymentVoucherMapper.getListReturnProducts(paymentVoucher.getDeliveryVoucherNo(), paymentVoucher.getFactoryId()))
        .willReturn(data);
        
        mockMvc.perform(delete("/v1/payment-vouchers/{id}", "1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }

    @Test
    public void testGetDetailPaymentVoucherSuccessPTX()throws Exception{
        PaymentVoucher paymentVoucher = new PaymentVoucher();
        paymentVoucher.setId("1");
        paymentVoucher.setVoucherCode("PTX");
        
        given(paymentVoucherRepository.findById("1")).willReturn(Optional.of(paymentVoucher));
        mockMvc.perform(get("/v1/payment-vouchers/{id}", "1").contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Ok"));
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
        String page = PageableProcess.PageToSqlQuery(pageable, "payment_voucher");
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "payment_voucher");
        HashMap<String, String> where1 = new HashMap<>();
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id","FAC1");
        given(voucherMapper.getList(where1,pagingStr, page, null, null)).willReturn(list);
        given(voucherMapper.countList(where1, page, null, null)).willReturn(5l);

        mockMvc.perform(get("/v1/payment-vouchers").header("department_id","FAC1")
                        .params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }

}
