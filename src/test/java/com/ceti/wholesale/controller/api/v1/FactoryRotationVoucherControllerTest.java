package com.ceti.wholesale.controller.api.v1;


import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.model.*;
import com.ceti.wholesale.repository.*;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.FactoryRotationVoucherService;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ProductAccessoryService;
import com.ceti.wholesale.service.impl.CylinderDebtServiceImpl;
import com.ceti.wholesale.service.impl.FactoryRotationVoucherServiceImpl;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FactoryRotationVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class FactoryRotationVoucherControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        FactoryRotationVoucherService factoryRotationVoucherService() {
            return new FactoryRotationVoucherServiceImpl() {
            };
        }

        @Bean
        GoodsInOutService goodsInOutService() {
            return new GoodsInOutServiceImpl();
        }


    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;
    @MockBean
    private FactoryRotationVoucherRepository factoryRotationVoucherRepository;

    @MockBean
    private FactoryRotationVoucherDetailRepository factoryRotationVoucherDetailRepository;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;

    @MockBean
    private GoodsInOutDetailRepository goodsInOutDetailRepository;

    @MockBean
    private VoucherUtils voucherUtils;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

    private FactoryRotationVoucher factoryRotationVoucher = new FactoryRotationVoucher();

    String id = "LC210420-001-LCK";

    String jsonCreateRequest = "{\n" +
            "    \"company_export_id\":\"CT01\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"company_import_id\":\"CT01\",\n" +
            "    \"company_export_name\":\"company_export_name\",\n" +
            "    \"company_import_name\":\"company_import_name\",\n" +
            "    \"truck_driver_id\":\"TX01\",\n" +
            "    \"truck_license_plate_number\":\"30A-12345\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"totalGoods\":\"62626\",\n" +
            "    \"note\":\"note\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"type\":\"NKTH\", \"product_type\":\"VO\", " +
            "   \"in_quantity\":\"100\"," +
            "   \"type\":\"XDVO\"," +
            " \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonUpdateRequest = "{\n" +
            "    \"company_export_id\":\"CT01\",\n" +
            "    \"voucher_at\":\"95616\",\n" +
            "    \"company_import_id\":\"CT01\",\n" +
            "    \"company_export_name\":\"company_export_name\",\n" +
            "    \"company_import_name\":\"company_import_name\",\n" +
            "    \"truck_driver_id\":\"TX01\",\n" +
            "    \"truck_license_plate_number\":\"30A-12345\",\n" +
            "    \"created_by_full_name\":\"kim van ha\",\n" +
            "    \"totalGoods\":\"62626\",\n" +
            "    \"note\":\"note\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"S1\", \"type\":\"NKTH\", \"product_type\":\"VO\", " +
            "   \"in_quantity\":\"100\", " +
            "   \"type\":\"XDVO\"," +
            "\"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    @Test
    public void testUpdateFactoryRotationVoucherSuccess() throws Exception{
        factoryRotationVoucher = new FactoryRotationVoucher();
        given(factoryRotationVoucherRepository.findById(id)).willReturn(Optional.ofNullable(factoryRotationVoucher));
        given(factoryRotationVoucherRepository.save(isA(FactoryRotationVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));

        mockMvc.perform(patch("/v1/factory-rotation-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("note"));
    }


    @Test
    public void testUpdateFactoryRotationVoucherWithFactoryRotationVoucherNotExist() throws Exception{
        given(factoryRotationVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/factory-rotation-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu luân chuyển hàng hóa không tồn tại"));
    }

    @Test
    public void testCreateFactoryRotationVoucherSuccess() throws Exception{
        given(factoryRotationVoucherRepository.save(isA(FactoryRotationVoucher.class))).willAnswer(i-> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i-> i.getArgument(0));

        mockMvc.perform(post("/v1/factory-rotation-vouchers")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest)).andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.note").value("note"));
    }

    @Test
    public void testGetListFactoryRotationVoucherSuccessWithEmbedGoodInOutFalse() throws Exception {
        List<Object[]> objects = new ArrayList<>();
        List<FactoryRotationVoucher> list = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            factoryRotationVoucher = new FactoryRotationVoucher();
            factoryRotationVoucher.setNo("no"+i);
            factoryRotationVoucher.setId("id"+i);
            factoryRotationVoucher.setVoucherAt(Instant.now());
            factoryRotationVoucher.setVoucherCode("voucher_code"+i);
            factoryRotationVoucher.setTotalGoods(BigDecimal.valueOf(12));
            factoryRotationVoucher.setCreatedByFullName("do van hung"+i);
            factoryRotationVoucher.setTruckLicensePlateNumber("0number"+i);
            factoryRotationVoucher.setNo("no"+i);
            factoryRotationVoucher.setNote("note"+i);

            GoodsInOut goodsInOut = new GoodsInOut();
            goodsInOut.setVoucherId("voucher"+i);
            goodsInOut.setVoucherNo("voucher_no"+i);
            list.add(factoryRotationVoucher);

            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver"+i).setAbbreviatedName("hung" +i).setId("id"+i).setTruckLicensePlateNumber("12b"+i).setAddress("bac giang"+i);

            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343"+i);

            Object[] object = new Object[10];
            object[0] = factoryRotationVoucher;
            object[1] = truckDriver;
            object[2] = truck;
            objects.add(object);
        }

        Pageable pageable = PageRequest.of(0, 20);

        ResultPage<Object[]> result = new ResultPage<Object[]>();
        result.setPageList(objects);
        result.setTotalItems(5);

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("note","note");
        where.add("embed_goods_in_out", "false");


        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
        where1.add("note","note");
        where1.add("embed_goods_in_out", "false");
        where1.add("factory_id", "FAC1");

        given(factoryRotationVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);

        boolean embed_goods_in_out = true;
//        given(goodsInOutRepository.getByVoucherId(id)).willReturn(list);
        mockMvc.perform(get("/v1/factory-rotation-vouchers")
                .header("department_id","FAC1")
                .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].note").value("note0"));
    }

    @Test
    public void testGetListFactoryRotationVoucherSuccessWithEmbedGoodInOutTrue() throws Exception {
        List<Object[]> objects = new ArrayList<>();
        List<FactoryRotationVoucher> list = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            factoryRotationVoucher = new FactoryRotationVoucher();
            factoryRotationVoucher.setNo("no"+i);
            factoryRotationVoucher.setId("id"+i);
            factoryRotationVoucher.setVoucherAt(Instant.now());
            factoryRotationVoucher.setVoucherCode("voucher_code"+i);
            factoryRotationVoucher.setTotalGoods(BigDecimal.valueOf(12));
            factoryRotationVoucher.setCreatedByFullName("do van hung"+i);
            factoryRotationVoucher.setTruckLicensePlateNumber("0number"+i);
            factoryRotationVoucher.setNo("no"+i);
            factoryRotationVoucher.setNote("note"+i);

            GoodsInOut goodsInOut = new GoodsInOut();
            goodsInOut.setVoucherId("voucher"+i);
            goodsInOut.setVoucherNo("voucher_no"+i);
            list.add(factoryRotationVoucher);

            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver"+i).setAbbreviatedName("hung" +i).setId("id"+i).setTruckLicensePlateNumber("12b"+i).setAddress("bac giang"+i);

            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343"+i);

            Object[] object = new Object[10];
            object[0] = factoryRotationVoucher;
            object[1] = truckDriver;
            object[2] = truck;
            objects.add(object);
        }

        Pageable pageable = PageRequest.of(0, 20);

        ResultPage<Object[]> result = new ResultPage<Object[]>();
        result.setPageList(objects);
        result.setTotalItems(5);

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("note","note");
        where.add("embed_goods_in_out", "true");
        where.add("rotation_show_quantity", "in_quantity");

        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
        where1.add("note","note");
        where1.add("embed_goods_in_out", "true");
        where1.add("rotation_show_quantity", "in_quantity");
        where1.add("factory_id", "FAC1");

        given(factoryRotationVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);

        boolean embed_goods_in_out = true;
//        given(goodsInOutRepository.getByVoucherId(id)).willReturn(list);
        mockMvc.perform(get("/v1/factory-rotation-vouchers")
                .header("department_id","FAC1")
                .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].note").value("note0"));
    }

    @Test
    public void testGetListFactoryRotationVoucherSuccessWithEmbedGoodInOutTrueOutQuantity() throws Exception {
        List<Object[]> objects = new ArrayList<>();
        List<FactoryRotationVoucher> list = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            factoryRotationVoucher = new FactoryRotationVoucher();
            factoryRotationVoucher.setNo("no"+i);
            factoryRotationVoucher.setId("id"+i);
            factoryRotationVoucher.setVoucherAt(Instant.now());
            factoryRotationVoucher.setVoucherCode("voucher_code"+i);
            factoryRotationVoucher.setTotalGoods(BigDecimal.valueOf(12));
            factoryRotationVoucher.setCreatedByFullName("do van hung"+i);
            factoryRotationVoucher.setTruckLicensePlateNumber("0number"+i);
            factoryRotationVoucher.setNo("no"+i);
            factoryRotationVoucher.setNote("note"+i);

            GoodsInOut goodsInOut = new GoodsInOut();
            goodsInOut.setVoucherId("voucher"+i);
            goodsInOut.setVoucherNo("voucher_no"+i);
            list.add(factoryRotationVoucher);

            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver"+i).setAbbreviatedName("hung" +i).setId("id"+i).setTruckLicensePlateNumber("12b"+i).setAddress("bac giang"+i);

            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343"+i);

            Object[] object = new Object[10];
            object[0] = factoryRotationVoucher;
            object[1] = truckDriver;
            object[2] = truck;
            objects.add(object);
        }

        Pageable pageable = PageRequest.of(0, 20);

        ResultPage<Object[]> result = new ResultPage<Object[]>();
        result.setPageList(objects);
        result.setTotalItems(5);

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("note","note");
        where.add("embed_goods_in_out", "true");
        where.add("rotation_show_quantity", "out_quantity");

        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
        where1.add("note","note");
        where1.add("embed_goods_in_out", "true");
        where1.add("rotation_show_quantity", "out_quantity");
        where1.add("factory_id", "FAC1");

        given(factoryRotationVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);

        boolean embed_goods_in_out = true;
//        given(goodsInOutRepository.getByVoucherId(id)).willReturn(list);
        mockMvc.perform(get("/v1/factory-rotation-vouchers")
                .header("department_id","FAC1")
                .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].note").value("note0"));
    }

    @Test
    public void testGetListFactoryRotationVoucherSuccessWithEmbedGoodInOutTrueAll() throws Exception {
        List<Object[]> objects = new ArrayList<>();
        List<FactoryRotationVoucher> list = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            factoryRotationVoucher = new FactoryRotationVoucher();
            factoryRotationVoucher.setNo("no"+i);
            factoryRotationVoucher.setId("id"+i);
            factoryRotationVoucher.setVoucherAt(Instant.now());
            factoryRotationVoucher.setVoucherCode("voucher_code"+i);
            factoryRotationVoucher.setTotalGoods(BigDecimal.valueOf(12));
            factoryRotationVoucher.setCreatedByFullName("do van hung"+i);
            factoryRotationVoucher.setTruckLicensePlateNumber("0number"+i);
            factoryRotationVoucher.setNo("no"+i);
            factoryRotationVoucher.setNote("note"+i);

            GoodsInOut goodsInOut = new GoodsInOut();
            goodsInOut.setVoucherId("voucher"+i);
            goodsInOut.setVoucherNo("voucher_no"+i);
            list.add(factoryRotationVoucher);

            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver"+i).setAbbreviatedName("hung" +i).setId("id"+i).setTruckLicensePlateNumber("12b"+i).setAddress("bac giang"+i);

            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343"+i);

            Object[] object = new Object[10];
            object[0] = factoryRotationVoucher;
            object[1] = truckDriver;
            object[2] = truck;
            objects.add(object);
        }

        Pageable pageable = PageRequest.of(0, 20);

        ResultPage<Object[]> result = new ResultPage<Object[]>();
        result.setPageList(objects);
        result.setTotalItems(5);

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("note","note");
        where.add("embed_goods_in_out", "true");
        where.add("rotation_show_quantity", "all");

        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
        where1.add("note","note");
        where1.add("embed_goods_in_out", "true");
        where1.add("rotation_show_quantity", "all");
        where1.add("factory_id", "FAC1");

        given(factoryRotationVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);

        boolean embed_goods_in_out = true;
//        given(goodsInOutRepository.getByVoucherId(id)).willReturn(list);
        mockMvc.perform(get("/v1/factory-rotation-vouchers").header("department_id","FAC1")
                .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].note").value("note0"));
    }

    @Test
    public void testGetListFactoryRotationVoucherSuccessWithEmbedGoodInOutTrueDefault() throws Exception {
        List<Object[]> objects = new ArrayList<>();
        List<FactoryRotationVoucher> list = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            factoryRotationVoucher = new FactoryRotationVoucher();
            factoryRotationVoucher.setNo("no"+i);
            factoryRotationVoucher.setId("id"+i);
            factoryRotationVoucher.setVoucherAt(Instant.now());
            factoryRotationVoucher.setVoucherCode("voucher_code"+i);
            factoryRotationVoucher.setTotalGoods(BigDecimal.valueOf(12));
            factoryRotationVoucher.setCreatedByFullName("do van hung"+i);
            factoryRotationVoucher.setTruckLicensePlateNumber("0number"+i);
            factoryRotationVoucher.setNo("no"+i);
            factoryRotationVoucher.setNote("note"+i);

            GoodsInOut goodsInOut = new GoodsInOut();
            goodsInOut.setVoucherId("voucher"+i);
            goodsInOut.setVoucherNo("voucher_no"+i);
            list.add(factoryRotationVoucher);

            TruckDriver truckDriver = new TruckDriver().setFullName("truckDriver"+i).setAbbreviatedName("hung" +i).setId("id"+i).setTruckLicensePlateNumber("12b"+i).setAddress("bac giang"+i);

            Truck truck = new Truck().setTruckWeight(BigDecimal.valueOf(1032)).setLicensePlateNumber("2343"+i);

            Object[] object = new Object[10];
            object[0] = factoryRotationVoucher;
            object[1] = truckDriver;
            object[2] = truck;
            objects.add(object);
        }

        Pageable pageable = PageRequest.of(0, 20);

        ResultPage<Object[]> result = new ResultPage<Object[]>();
        result.setPageList(objects);
        result.setTotalItems(5);

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("note","note");
        where.add("embed_goods_in_out", "true");
        where.add("rotation_show_quantity", "sss");

        MultiValueMap<String, String> where1 = new LinkedMultiValueMap<>();
        where1.add("note","note");
        where1.add("embed_goods_in_out", "true");
        where1.add("rotation_show_quantity", "sss");
        where1.add("factory_id", "FAC1");

        given(factoryRotationVoucherDetailRepository.findAllWithFilter(pageable, where1)).willReturn(result);

        boolean embed_goods_in_out = true;
//        given(goodsInOutRepository.getByVoucherId(id)).willReturn(list);
        mockMvc.perform(get("/v1/factory-rotation-vouchers")
                .header("department_id","FAC1")
                .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].note").value("note0"));
    }

    @Test
    public void testDeleteFactoryRotationVoucherWithFactoryRotationVoucherNotExist() throws Exception{
        given(factoryRotationVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(delete("/v1/factory-rotation-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu xuất nhập luân chuyển không tồn tại"));
    }

    @Test
    public void testDeleteFactoryRotationVoucherSuccess() throws Exception{
        FactoryRotationVoucher factoryRotationVoucher = new FactoryRotationVoucher();
        factoryRotationVoucher.setId("1");
        given(factoryRotationVoucherRepository.existsById("1")).willReturn(true);

        mockMvc.perform(delete("/v1/factory-rotation-vouchers/{id}", 1).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted"));
    }
}
