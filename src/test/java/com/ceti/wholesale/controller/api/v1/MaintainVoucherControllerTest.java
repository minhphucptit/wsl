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
import com.ceti.wholesale.dto.VoucherDto;
import com.ceti.wholesale.mapper.MaintainVoucherMapper;
import com.ceti.wholesale.mapper.VoucherMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.MaintainVoucher;
import com.ceti.wholesale.model.ProductAccessory;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.GoodsInOutMaintailDetailRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.MaintainVoucherDetailRepository;
import com.ceti.wholesale.repository.MaintainVoucherRepository;
import com.ceti.wholesale.repository.ProductAccessoryRepository;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.MaintainVoucherService;
import com.ceti.wholesale.service.impl.CylinderDebtServiceImpl;
import com.ceti.wholesale.service.impl.MaintainVoucherServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(MaintainVoucherController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class MaintainVoucherControllerTest {
	
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        MaintainVoucherService maintainVoucherService(){
            return new MaintainVoucherServiceImpl();
        }

        @Bean
        CylinderDebtService cylinderDebtService(){
            return new CylinderDebtServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;
    @MockBean
    private VoucherMapper voucherMapper;

    @MockBean
    private MaintainVoucherRepository maintainVoucherRepository;

    @MockBean
    private MaintainVoucherDetailRepository maintainVoucherDetailRepository;

    @MockBean
    private GoodsInOutRepository goodsInOutRepository;
    @MockBean
    private VoucherUtils voucherUtils;
    @MockBean
    private GoodsInOutMaintailDetailRepository goodsInOutMaintailDetailRepository;

    @MockBean
    private CylinderDebtRepository cylinderDebtRepository;

    @MockBean
    private ProductAccessoryRepository productAccessoryRepository;

    @MockBean
    private MaintainVoucherMapper maintainVoucherMapper;
    
    @MockBean
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

    String jsonCreateRequest = "{\n" +
            "    \"truck_driver_id\":\"1\",\n" +
            "    \"truck_license_plate_number\":\"1234\",\n" +
            "    \"total_goods\":\"10\",\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"note\":\"abc\",\n" +
            "    \"type\":\"type\",\n" +
            "    \"voucher_at\":\"1615463188\",\n" +
            "    \"created_by_full_name\":\"nhat\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\", \"type\":\"aaa\"," +
            "\"product_type\":\"GAS\"," +
            "\"factory_id\":\"1\"," +
            "   \"in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    String jsonUpdateRequest = "{\n" +
            "    \"truck_driver_id\":\"1\",\n" +
            "    \"truck_license_plate_number\":\"1234\",\n" +
            "    \"total_goods\":\"10\",\n" +
            "    \"company_id\":\"ceti\",\n" +
            "    \"voucher_at\":\"102255441255\",\n" +
            "    \"note\":\"abc\",\n" +
            "    \"type\":\"type\",\n" +
            "    \"update_by_full_name\":\"nhat\",\n" +
            "    \"goods_in_out\":" +
            "[{ \"product_id\":\"1\", \"type\":\"aaa\"," +
            "\"product_type\":\"GAS\"," +
            "\"factory_id\":\"1\"," +
            "   \"in_quantity\":\"100\", \"out_quantity\":\"50\"," +
            " \"product_name\":\"romano\", \"unit\":\"romano\", \"price\":\"100\"" +
            "}]}";

    MaintainVoucher maintainVoucher = new MaintainVoucher();
    
    private String licenseNumber = "27M3-3333";
    private Instant day = Instant.ofEpochSecond(1630454400l);
    private String id = "2c9c80887c30b498017c349b2d500001";

    @Test
    public void testUpdateMaintainVoucherSuccess() throws Exception {
        List<ProductAccessory> list = new ArrayList<ProductAccessory>();
        ProductAccessory productAccessory = new ProductAccessory();
        productAccessory.setFactoryId("1");
        productAccessory.setMainProductId("1");
        productAccessory.setId("1");
        productAccessory.setSubProductName("name");
        list.add(productAccessory);
        MaintainVoucher maintainVoucher = new MaintainVoucher();
        maintainVoucher.setFactoryId("1");
        maintainVoucher.setId("1");
        given(maintainVoucherRepository.findById("1")).willReturn(Optional.of(maintainVoucher));
        given(productAccessoryRepository.findByMainProductIdAndFactoryId("1", "1")).willReturn(list);
        given(maintainVoucherRepository.save(isA(MaintainVoucher.class))).willAnswer(i -> i.getArgument(0));
        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(maintainVoucher.getFactoryId(), VoucherEnum.getEnumFromCode(maintainVoucher.getVoucherCode()),
                Instant.ofEpochSecond(10252));
        given(accountVoucherCodeRepository.findById("1")).willReturn(Optional.ofNullable(accountVoucherCode));

        mockMvc.perform(patch("/v1/maintain-vouchers/{id}", "1").contentType(APPLICATION_JSON_UTF8)
                        .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("source cannot be null"));
    }

    @Test
    public void testUpdateMaintainVoucherFalse() throws Exception {

        given(maintainVoucherRepository.findById(id)).willReturn(Optional.empty());

        mockMvc.perform(patch("/v1/maintain-vouchers/{id}", id).contentType(APPLICATION_JSON_UTF8)
                        .content(jsonUpdateRequest)).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu xuất nhập bảo dưỡng không tồn tại"));
    }

//    @Test
//    public void testCreateMaintainVoucherFalse() throws Exception {
//        mockMvc.perform(post("/v1/maintain-vouchers").header("department_id", "1")
//                        .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
//                .andExpect(status().is4xxClientError())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("source cannot be null"));
//    }

//    @Test
//    public void testCreateMaintainVoucherSuccess() throws Exception {
//        List<ProductAccessory> list = new ArrayList<ProductAccessory>();
//        ProductAccessory productAccessory = new ProductAccessory();
//        productAccessory.setFactoryId("1");
//        productAccessory.setMainProductId("1");
//        productAccessory.setId("1");
//        productAccessory.setSubProductName("name");
//        list.add(productAccessory);
//
//        given(maintainVoucherRepository.save(isA(MaintainVoucher.class))).willAnswer(i -> i.getArgument(0));
//        given(goodsInOutRepository.save(isA(GoodsInOut.class))).willAnswer(i -> i.getArgument(0));
//        given(productAccessoryRepository.findByMainProductIdAndFactoryId("1", "1")).willReturn(list);
//
//        mockMvc.perform(post("/v1/maintain-vouchers").header("department_id", "1").contentType(APPLICATION_JSON_UTF8)
//                        .content(jsonCreateRequest)).andExpect(status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("source cannot be null"));
//    }
    @Test
    public void testDeleteFalse() throws Exception {
        given(maintainVoucherRepository.existsById(id)).willReturn(false);

        mockMvc.perform(delete("/v1/maintain-vouchers/{id}",id))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Phiếu xuất nhập bảo dưỡng không tồn tại"));
    }

    @Test
    public void testDeleteSuccess() throws Exception {
    	MaintainVoucher maintainVoucher = new MaintainVoucher();
    	maintainVoucher.setId(id);
        given(maintainVoucherRepository.findById(id)).willReturn(Optional.ofNullable(maintainVoucher));

        mockMvc.perform(delete("/v1/maintain-vouchers/{id}",id))
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
        String page = PageableProcess.PageToSqlQuery(pageable, "maintain_voucher");
        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "maintain_voucher");
        HashMap<String, String> where1 = new HashMap<>();
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("voucher_at_from","1633053853");
        where.add("voucher_at_to","1634273946");
        given(voucherMapper.getList(where1,pagingStr, page, Instant.ofEpochSecond(1633053853), Instant.ofEpochSecond(1634273946))).willReturn(list);
        given(voucherMapper.countList(where1, page, Instant.ofEpochSecond(1633053853), Instant.ofEpochSecond(1634273946))).willReturn(5l);

        mockMvc.perform(get("/v1/maintain-vouchers").params(where)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("0"));
    }

}
